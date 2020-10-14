package com.restbank.service;

import com.restbank.api.GenericResponse;
import com.restbank.api.Info;
import com.restbank.domain.Role;
import com.restbank.domain.User;
import com.restbank.domain.dto.UserVM;
import com.restbank.error.NotFoundException;
import com.restbank.repository.RoleRepository;
import com.restbank.repository.UserRepository;
import com.restbank.utils.Statics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private RoleRepository roleRepository;

    public User save(User user){
        String hashed = encoder.encode(user.getPassword());
        user.setPassword(hashed);

        return userRepository.save(user);
    }

    public User getUserById(Long id){
        return userRepository.findById(id).get();
    }

    public GenericResponse<UserVM> getUserVMById(Long id){
        User userInDB = userRepository.getById(id);

        if(userInDB == null)
            throw new NotFoundException("User not found with id=" + id);

        return new GenericResponse<>(new UserVM(userInDB));
    }

    public GenericResponse<List<UserVM>> getUserVMList(Pageable page){
        Page<UserVM> pageUsers = userRepository.findAll(page).map(UserVM::new); // page object contains - content<data> & Page class variables.
        Info info = new Info(pageUsers, Statics.API_1_0_USERS);

        return new GenericResponse<>(info, pageUsers.getContent());
    }

    public GenericResponse updateUser(User user){
        User dbUser = userRepository.findById(user.getId()).get();

        if(user.getPassword() != null)
            dbUser.setPassword(encoder.encode(user.getPassword())); // Eger requestBody içerisinde password bulunmuyorsa hashli olan parolayı 2 kere hashlememek için kontrol ediyoruz.

        dbUser.setFirstName(user.getFirstName());
        dbUser.setLastName(user.getLastName());
        dbUser.setEmail(user.getEmail());
        dbUser.setRoles(user.getRoles());
        userRepository.save(dbUser);

        return new GenericResponse("User updated");
    }

    public GenericResponse updateRoles(Long id, String[] roles) {
        User user = userRepository.findById(id).get();
        Set<Role> roleHashSet = new HashSet<Role>();

        for(String role : roles)
            roleHashSet.add(roleRepository.findByName(role));

        user.setRoles(roleHashSet);
        userRepository.save(user);

        return new GenericResponse("User Roles added.");
    }
}

