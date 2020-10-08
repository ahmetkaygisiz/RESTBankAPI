package com.restbank.service;

import com.restbank.api.GenericResponse;
import com.restbank.api.Info;
import com.restbank.domain.User;
import com.restbank.repository.UserRepository;
import com.restbank.utils.Statics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User save(User user){
        String hashed = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(hashed);

        return userRepository.save(user);
    }

    public GenericResponse<List<User>> getUserList(int pageNumber, int pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<User> pageUsers = userRepository.findAll(pageable); // page object contains - content<data> & Page class variables.
        Info info = new Info(pageUsers, Statics.API_1_0_USERS);

        return new GenericResponse<>(info, pageUsers.getContent());
    }

    public void delete(User user){
        userRepository.delete(user);
    }

    public void updateUser(User user){
        BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();

        User dbUser = userRepository.findById(user.getId()).get();

        dbUser.setFirstName(user.getFirstName());
        dbUser.setLastName(user.getLastName());
        dbUser.setEmail(user.getEmail());
        dbUser.setRoles(user.getRoles());

        userRepository.save(dbUser);
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User getUserByPhoneNumber(String phoneNumber){
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }
}

