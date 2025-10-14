package com.example.demo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/HOLA")
public class HolaControlador {
    @GetMapping(value = "/")
    public String hola(){
        return "hola";
    }

}
