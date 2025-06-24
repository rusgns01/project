package com.example.crud.controller;

import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String homeP(){
        return "index";
    }

    @GetMapping("/main")
    public String mainP(){
        return "main";
    }
}
