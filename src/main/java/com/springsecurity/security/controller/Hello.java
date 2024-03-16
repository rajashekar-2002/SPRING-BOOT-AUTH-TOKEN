package com.springsecurity.security.controller;

import org.springframework.web.bind.annotation.GetMapping;

    public class Hello {
        
        //allow this when u r logged in 
        @GetMapping("/hello")
        public String hello(){
           return "successfully logged in with GOOGLE 0AUTH";
        }

}
