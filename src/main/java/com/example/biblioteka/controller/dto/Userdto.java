package com.example.biblioteka.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class Userdto {

    private String username;

    private String password;

    private String confirmPassword;


    public boolean isPasswordConfirmed() {
        return password != null && password.equals(confirmPassword);
    }

}
