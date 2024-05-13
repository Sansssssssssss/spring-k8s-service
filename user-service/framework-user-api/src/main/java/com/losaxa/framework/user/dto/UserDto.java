package com.losaxa.framework.user.dto;

import com.losaxa.framework.user.enums.UserType;
import lombok.Data;

import java.util.Collection;
import java.util.List;

@Data
public class UserDto {

    private Long id;

    private String userName;

    private String password;

    private UserType type;

    private Collection<String> roles;

    private Collection<String> permission;

}
