package com.youcode.springsecuritydemo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class UserController {

    @GetMapping("/user")
    public String user() {
        return "Welcome User!";
    }

    @GetMapping("/admin")
    public String admin() {
        return "Welcome Admin!";
    }
}
