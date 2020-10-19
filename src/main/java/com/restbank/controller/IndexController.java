package com.restbank.controller;

import com.restbank.api.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;

@RestController
public class IndexController {

    @Autowired
    private RequestMappingHandlerMapping requestHandlerMapping;

    @GetMapping(value = {"/","/endpoints"})
    public GenericResponse<List<String>> getIndexs(){
        List<String> endpoints = new ArrayList<>();
        this.requestHandlerMapping.getHandlerMethods()
                .forEach((key, value) -> endpoints.add(key.toString()));

        return new GenericResponse<>(endpoints);
    }
}
