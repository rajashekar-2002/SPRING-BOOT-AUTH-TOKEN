package com.springsecurity.security.model;

import lombok.Data;

@Data
public class PasswordModel {
    private String email;
    private String oldPAssword;
    private String newPAssword;
}
