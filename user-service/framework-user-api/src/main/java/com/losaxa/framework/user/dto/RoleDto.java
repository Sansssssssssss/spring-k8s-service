package com.losaxa.framework.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(description = "角色dto")
@Data
public class RoleDto {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("角色名")
    private String name;

    @ApiModelProperty("角色编码")
    private String code;

    @ApiModelProperty("角色权限")
    private List<String> permission;

}
