package com.restbank.service;

import com.restbank.domain.User;
import com.restbank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User save(User user){
        String hashed = new BCryptPasswordEncoder().encode(user.getPassword());
        System.out.println("hashed = " + hashed);
        user.setPassword(hashed);

        return userRepository.save(user);
    }

    public List<User> getUserList(){
        return userRepository.findAll();
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

