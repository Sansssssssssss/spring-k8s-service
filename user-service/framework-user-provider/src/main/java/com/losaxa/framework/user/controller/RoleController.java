package com.losaxa.framework.user.controller;

import com.losaxa.core.mongo.query.QueryUtil;
import com.losaxa.core.web.Page;
import com.losaxa.core.web.Result;
import com.losaxa.framework.user.domian.Role;
import com.losaxa.framework.user.dto.RoleDto;
import com.losaxa.framework.user.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
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

@Api(tags = "角色接口")
@RequestMapping("/role")
@RestController
public class RoleController {

    @SuppressWarnings("all")
    @Autowired
    private RoleService roleService;

    @ApiOperation("根据id查询角色")
    @GetMapping
    public Result<Role> get(@RequestParam Long id) {
        return Result.ok(roleService.getById(id).orElse(null));
    }

    @ApiOperation("分页查询角色")
    @GetMapping("/page")
    public Result<List<Role>> page(Page page,
                                   Role query) {
        return Result.ok(roleService.page(page.toPageable(), query));
    }

    @ApiOperation("添加角色")
    @PostMapping
    public Result<Role> create(@Validated @RequestBody RoleDto data) {
        Role role = roleService.create(data);
        return Result.ok(role);
    }

    @ApiOperation("根据id修改角色")
    @PutMapping
    public Result<Void> update(@Validated @RequestBody RoleDto data) {
        roleService.update(data);
        return Result.ok();
    }

    @ApiOperation("根据id删除角色")
    @DeleteMapping
    public Result<Void> delete(@RequestParam Long id) {
        roleService.delete(id);
        return Result.ok();
    }

}