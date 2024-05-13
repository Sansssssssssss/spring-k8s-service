package com.losaxa.framework.user.controller;

import com.losaxa.core.web.Result;
import com.losaxa.framework.user.domian.User;
import com.losaxa.framework.user.dto.AddAdminDto;
import com.losaxa.framework.user.dto.UpdateAdminDto;
import com.losaxa.framework.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "管理员接口")
@RequestMapping("/admin")
@RestController
public class AdminController{

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation("添加管理员")
    @PostMapping
    public Result<User> addAdmin(@Validated @RequestBody AddAdminDto admin) {
        return Result.ok(userService.createAdmin(admin));
    }

    @ApiOperation("修改管理员")
    @PutMapping
    public Result<Void> updateAdmin(@Validated @RequestBody UpdateAdminDto admin) {
        userService.updateAdmin(admin);
        return Result.ok();
    }

    @ApiOperation("根据用户名查询管理员")
    @GetMapping
    public Result<User> getAdmin(@RequestParam String username) {
        return Result.ok(userService.findAdmin(username));
    }


}
