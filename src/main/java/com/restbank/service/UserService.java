package com.restbank.service;

import com.restbank.api.GenericResponse;
import com.restbank.api.Info;
import com.restbank.domain.Account;
import com.restbank.domain.Role;
import com.restbank.domain.User;
import com.restbank.domain.UserRole;
import com.restbank.domain.dto.UserVM;
import com.restbank.error.exceptions.NotFoundException;
import com.restbank.repository.AccountRepository;
import com.restbank.repository.RoleRepository;
import com.restbank.repository.UserRepository;
import com.restbank.utils.Statics;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AccountRepository accountRepository;

    public User create(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional
    public User create(User user, Set<UserRole> userRoles){
        User localUser = userRepository.findByEmail(user.getEmail());

        if (localUser != null){
            log.info("save(user,userRoles) ## User already exists. Nothing will be done.");
        }else {
            for (UserRole ur : userRoles){
                Role role = roleRepository.findByName(ur.getRole().getName());

                if(role == null)
                    roleRepository.save(ur.getRole());
                else
                    ur.setRole(role);
            }

            user.getUserRoles().addAll(userRoles);
            user.setPassword(encoder.encode(user.getPassword()));

            localUser = userRepository.save(user);
        }
        return localUser;
    }

    public User getUserById(Long id){
        return userRepository.getOne(id);
    }

    public GenericResponse<UserVM> getUserVMById(Long id){
        User userInDB = userRepository.getById(id);

        if(userInDB == null){
            log.error("getUserVMById ## User not found with id =" + id);
            throw new NotFoundException("User not found with id=" + id);
        }
        return new GenericResponse<>(new UserVM(userInDB));
    }

    public GenericResponse<List<UserVM>> getUserVMList(Pageable page){
        Page<UserVM> pageUsers = userRepository.findAll(page).map(UserVM::new); // page object contains - content<data> & Page class variables.
        Info info = new Info(pageUsers, Statics.API_1_0_USERS);

        return new GenericResponse<>(info, pageUsers.getContent());
    }

    public GenericResponse updateUser(User user){
        User dbUser = userRepository.getOne(user.getId());

        if(user.getPassword() != null) {
            dbUser.setPassword(encoder.encode(user.getPassword())); // Eger requestBody içerisinde password bulunmuyorsa hashli olan parolayı 2 kere hashlememek için kontrol ediyoruz.
        }
        dbUser.setFirstName(user.getFirstName());
        dbUser.setLastName(user.getLastName());
        dbUser.setEmail(user.getEmail());
        dbUser.setUserRoles(user.getUserRoles());
        dbUser.setAccountList(user.getAccountList());
        dbUser.setActive(user.isActive());
        dbUser.setPhoneNumber(user.getPhoneNumber());

        userRepository.save(dbUser);

        return new GenericResponse("User updated");
    }

    public GenericResponse updateRoles(Long id, String[] roles) {
        Set<UserRole> userRoles = new HashSet<>();
        User user = userRepository.getOne(id);

        if ( user == null){
            log.error("updateRoles ## User not found with id =" + id);
            throw new NotFoundException("User not exists with id=" + id);
        }

        for(String role : roles) {
            Role roleDB = roleRepository.findByName(role);

            if(roleDB != null)
                userRoles.add(new UserRole(user, roleDB));
        }
        user.setUserRoles(userRoles);
        userRepository.save(user);

        return new GenericResponse("User Roles added.");
    }

    public GenericResponse deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
        }
        catch (IllegalArgumentException | EmptyResultDataAccessException e){
            log.error("deleteUser ## User not found with id=" + id);
            throw new NotFoundException("User not exists with id=" + id);
        }

        return new GenericResponse("User deleted.");
    }

    @Transactional
    public GenericResponse addUserAccount(Long id, Account account) {
        User userInDB = userRepository.getOne(id);
        account.setUser(userInDB);

        userInDB.addAccountToList(account);
        userRepository.save(userInDB);

        return new GenericResponse<>("Account created.");
    }

    public GenericResponse<List<Account>> getUserAccounts(Long id) {
        User user = userRepository.getOne(id);
        List<Account> accountList = accountRepository.findAllByUser(user);

        return new GenericResponse<>(accountList);
    }

    // deleteUserRoles
}
