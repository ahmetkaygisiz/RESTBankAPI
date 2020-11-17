package com.restbank.service;

import com.restbank.api.GenericResponse;
import com.restbank.domain.Account;
import com.restbank.domain.CreditCard;
import com.restbank.domain.Transaction;
import com.restbank.domain.User;
import com.restbank.domain.dto.UserVM;
import com.restbank.error.exceptions.NotFoundException;
import com.restbank.error.exceptions.ValidationException;
import com.restbank.repository.TransactionRepository;
import com.restbank.repository.UserRepository;
import com.restbank.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CustomerService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionRepository transactionRepository;

    public User getUserByEmail(String email){
        User user = userRepository.findUserOptionalByEmail(email).orElseThrow(() ->{
           log.error("User not found with email = " + email);
            throw new NotFoundException("User not found with email = " + email);
        });

        return user;
    }

    public GenericResponse updateUser(String email, User user) {
        User dbUser = getUserByEmail(email);

        if(user.getPassword() != null) {
            user.setPassword(encoder.encode(user.getPassword()));
        }

        if(user.getEmail() != null )
            if (userRepository.findByEmail(user.getEmail()) != null )
                throw new ValidationException("email already in use");

        if(user.getPhoneNumber() != null)
            if (userRepository.findByPhoneNumber(user.getPhoneNumber()) != null)
                throw new ValidationException("phone number already in use");

        // Tesekkurler kod gemisi : https://medium.com/kodgemisi/spring-data-jpa-partial-update-782db3734ba */
        BeanUtils.copyProperties(user, dbUser, Utils.getNullProps(user)); // from, to, ignoreNullParams

        userRepository.save(dbUser);

        return new GenericResponse("User updated");
    }

    public GenericResponse<UserVM> getUserVMByEmail(String email){
        User userInDB = getUserByEmail(email);

        return new GenericResponse<>(new UserVM(userInDB));
    }

    public GenericResponse<List<Account>> getCustomerAccountList(String email) {
        User userInDB = getUserByEmail(email);
        return new GenericResponse<>(userInDB.getAccountList());
    }

    public GenericResponse<CreditCard> getCustomerCreditCardByAccountNumber(String accountNumber, String email) {

        GenericResponse<CreditCard> responseData = new GenericResponse<>();
        User userInDB = getUserByEmail(email);

        for(Account accountLoop : userInDB.getAccountList()){ // Check user account number's
            if (accountLoop.getAccountNumber().equalsIgnoreCase(accountNumber))
                responseData = accountService.getCreditCard(accountNumber);
        }

        return responseData;
    }

    public GenericResponse<List<Transaction>> getAccountTransactionByAccountNumber(String accountNumber, String email) {
        User userInDB = getUserByEmail(email);
        GenericResponse<List<Transaction>> responseData = new GenericResponse<>();

        for(Account accountLoop : userInDB.getAccountList()){ // Check user account number's
            if (accountLoop.getAccountNumber().equalsIgnoreCase(accountNumber))
                responseData.setData(transactionRepository.
                        getTransactionsByAccountNumber(accountNumber));
        }

        return responseData;
    }
}
