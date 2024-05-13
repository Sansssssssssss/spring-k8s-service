package com.losaxa.framework.user.controller;

import com.losaxa.core.common.CollectionUtil;
import com.losaxa.core.web.Result;
import com.losaxa.framework.user.api.UserAuthRemote;
import com.losaxa.framework.user.dto.UserDto;
import com.losaxa.framework.user.service.RoleService;
import com.losaxa.framework.user.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequestMapping("/auth")
@AllArgsConstructor
@RestController
public class AuthRemoteController implements UserAuthRemote {

    private final UserService userService;
    private final RoleService roleService;

    @ApiOperation("根据用户名查询用户(提供远程接口)")
    @Override
    public Result<UserDto> getByUsername(String username) {
        UserDto rs = userService.findByUsername(username);
        if (CollectionUtil.isEmpty(rs.getRoles())) {
            return Result.ok(rs);
        }
        Collection<String> permission = rs.getPermission();
        boolean hasPermission = !CollectionUtil.isEmpty(permission);
        List<String> rolePermission = roleService.getRolePermission(rs.getRoles());
        Set<String> allPermission = new HashSet<>(hasPermission ? permission.size() : 0 + rolePermission.size());
        if (hasPermission) {
            allPermission.addAll(permission);
        }
        allPermission.addAll(rolePermission);
        rs.setPermission(allPermission);
        return Result.ok(rs);
    }

}
