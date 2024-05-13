package com.losaxa.framework.user.controller;

import com.losaxa.core.web.Page;
import com.losaxa.core.web.Result;
import com.losaxa.framework.user.domian.Permission;
import com.losaxa.framework.user.dto.PermissionDto;
import com.losaxa.framework.user.service.PermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "权限接口")
@RequestMapping("/permission")
@RestController
public class PermissionController {

    @SuppressWarnings("all")
    @Autowired
    private PermissionService permissionService;

    @ApiOperation("根据id查询权限")
    @GetMapping
    public Result<Permission> get(@RequestParam Long id) {
        return Result.ok(permissionService.getById(id).orElse(null));
    }

    @ApiOperation("分页查询权限")
    @GetMapping("/page")
    public Result<List<Permission>> page(Page page,
                                         Permission query) {
        return Result.ok(permissionService.page(page.toPageable(), query));
    }

    @ApiOperation("添加权限")
    @PostMapping
    public Result<Permission> create(@Validated @RequestBody PermissionDto data) {
        return Result.ok(permissionService.create(data));
    }

    @ApiOperation("根据id修改权限")
    @PutMapping
    public Result<Void> update(@Validated @RequestBody PermissionDto data) {
        permissionService.update(data);
        return Result.ok();
    }

    @ApiOperation("根据id删除权限")
    @DeleteMapping
    public Result<Void> delete(@RequestParam Long id) {
        permissionService.delete(id);
        return Result.ok();
    }

}