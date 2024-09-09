package com.example.demo.controllers;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/admin")
public class AdminController {

    @GetMapping("/admin1")
    public String admin1(){
        return "admin 1";
    }

    @GetMapping("/admin2")
    public String admin2(){
        return "admin 2";
    }
}
