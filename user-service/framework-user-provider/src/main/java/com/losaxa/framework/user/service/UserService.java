package com.losaxa.framework.user.service;

import com.losaxa.framework.user.domian.User;
import com.losaxa.framework.user.dto.AddAdminDto;
import com.losaxa.framework.user.dto.UpdateAdminDto;
import com.losaxa.framework.user.dto.UserDto;

public interface UserService {

    User createAdmin(AddAdminDto admin);

    long updateAdmin(UpdateAdminDto user);

    User findAdmin(String username);

    void changePassword(String oldPassword, String newPassword);

    boolean exists(String username);

    void delete(String username);

    UserDto findByUsername(String username);
}
