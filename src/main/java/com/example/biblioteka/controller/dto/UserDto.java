package com.example.biblioteka.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class UserDto {

    private String username;

    private String email;

    private String password;

    private String confirmPassword;

}
