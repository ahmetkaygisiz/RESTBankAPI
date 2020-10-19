package com.restbank.controller;

import com.restbank.utils.Statics;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Statics.API_1_0_LOGIN)
public class LoginController {

    // Buralar degerlenir ileride
    @PostMapping
    public void handleLogin(){ }
}
