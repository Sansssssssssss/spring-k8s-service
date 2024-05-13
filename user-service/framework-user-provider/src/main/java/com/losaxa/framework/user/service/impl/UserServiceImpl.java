package com.losaxa.framework.user.service.impl;

import com.losaxa.core.common.UidGenerator;
import com.losaxa.core.security.UserContextHolder;
import com.losaxa.core.web.exception.BusinessException;
import com.losaxa.framework.user.converter.AddAdminDtoConverter;
import com.losaxa.framework.user.converter.UpdateAdminDtoConverter;
import com.losaxa.framework.user.converter.UserDtoConverter;
import com.losaxa.framework.user.domian.User;
import com.losaxa.framework.user.dto.AddAdminDto;
import com.losaxa.framework.user.dto.UpdateAdminDto;
import com.losaxa.framework.user.dto.UserDto;
import com.losaxa.framework.user.enums.UserType;
import com.losaxa.framework.user.repository.UserRepository;
import com.losaxa.framework.user.service.UserService;
import com.mongodb.client.result.UpdateResult;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.losaxa.framework.user.constant.DbField.PASSWORD;
import static com.losaxa.framework.user.constant.DbField.TYPE;
import static com.losaxa.framework.user.constant.DbField.USER_NAME;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository          userRepository;
    private final PasswordEncoder         encoder;
    private final AddAdminDtoConverter    addUserDtoConverter;
    private final UpdateAdminDtoConverter updateAdminDtoConverter;
    private final UserDtoConverter        userDtoConverter;
    private final UidGenerator            uidGenerator;

    @Override
    public User createAdmin(AddAdminDto admin) {
        User adminDb = addUserDtoConverter.to(admin);
        adminDb.encodePassword(encoder);
        adminDb.setType(UserType.ADMIN_TYPE);
        return userRepository.insert(adminDb);
    }

    @Override
    public long updateAdmin(UpdateAdminDto admin) {
        User user = updateAdminDtoConverter.to(admin);
        user.setPassword(null);
        user.setType(null);
        UpdateResult updateResult = userRepository.updateById(user);
        return updateResult.getModifiedCount();
    }

    @Override
    public User findAdmin(String username) {
        return userRepository.findOne(USER_NAME, username).orElse(null);
    }

    @Override
    public void changePassword(String oldPassword,
                               String newPassword) {
        String         userName     = UserContextHolder.getUsername();
        Optional<User> userOptional = userRepository.findOne(USER_NAME, userName);
        if (!userOptional.isPresent()) {
            throw BusinessException.newInstance("用户不存在");
        }
        User user = userOptional.get();
        if (!encoder.matches(oldPassword, user.getPassword())) {
            throw BusinessException.newInstance("原密码错误");
        }
        userRepository.updateFirst(USER_NAME, userName, PASSWORD, encoder.encode(newPassword));
    }

    @Override
    public boolean exists(String username) {
        return userRepository.findOne(USER_NAME, username).isPresent();
    }

    @Override
    public void delete(String username) {
        userRepository.delete(USER_NAME, username);
    }

    @Override
    public UserDto findByUsername(String username) {
        Optional<User> user = userRepository.findOne(USER_NAME, username);
        return user.map(userDtoConverter::from).orElse(null);
    }

}
