package com.losaxa.framework.auth.service;

import com.losaxa.core.common.CollectionUtil;
import com.losaxa.core.security.LoginUser;
import com.losaxa.core.web.Result;
import com.losaxa.core.web.exception.BusinessException;
import com.losaxa.framework.user.api.UserAuthRemote;
import com.losaxa.framework.user.dto.UserDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserAuthService implements UserDetailsService {

    @Resource
    private UserAuthRemote userRemote;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Result<UserDto> userRs = userRemote.getByUsername(username);
        if (!userRs.isSuccess()) {
            throw BusinessException.newInstance(userRs.getMessage());
        }
        UserDto user = userRs.getData();
        if (user == null) {
            throw BusinessException.newInstance("用户名或密码错误");
        }
        if (CollectionUtil.isEmpty(user.getPermission())) {
            return new LoginUser(user.getUserName(), user.getPassword(), Collections.emptyList(), user.getId(), user.getType().getValue());
        }
        List<GrantedAuthority> authorities = user.getPermission().stream().distinct().map(e -> (GrantedAuthority) () -> e).collect(Collectors.toList());
        // hasRole 加上前缀 ROLE_
        //List<GrantedAuthority> authoritnew = user.getPermission().stream().distinct().map(e -> new SimpleGrantedAuthority("ROLE_" + e)).collect(Collectors.toList());
        //authorities.addAll(authoritnew);
        return new LoginUser(user.getUserName(), user.getPassword(), authorities, user.getId(), user.getType().getValue());
    }

}
