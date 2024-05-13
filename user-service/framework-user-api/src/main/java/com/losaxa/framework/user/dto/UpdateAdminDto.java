package com.losaxa.framework.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(description = "修改管理员")
@Data
public class UpdateAdminDto {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("角色")
    private List<String> roles;

    @ApiModelProperty("权限")
    private List<String> permission;

}
