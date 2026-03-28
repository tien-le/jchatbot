package com.learn4you.jchatbot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloSpringController {

    @GetMapping("/hello")
    public String hello(){
        return "Hello Spring Boot!";
    }
}