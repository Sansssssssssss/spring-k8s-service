package com.losaxa.framework.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@ApiModel(description = "添加管理员")
@Data
public class AddAdminDto {

    @ApiModelProperty("用户名")
    @NotBlank
    private String userName;

    @ApiModelProperty("密码")
    @NotBlank
    private String password;

    @ApiModelProperty("角色")
    private List<String> roles;

    @ApiModelProperty("权限")
    private List<String> permission;

}
