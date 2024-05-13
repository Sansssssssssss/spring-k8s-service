package com.losaxa.framework.user.dto;

import com.losaxa.framework.user.enums.PermissionType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PermissionDto {

    @ApiModelProperty("父级id")
    private Long parentId;

    @ApiModelProperty("名称/菜单名称")
    private String name;

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("菜单类型")
    private PermissionType type;

    @ApiModelProperty("后端path")
    private String path;

    @ApiModelProperty("前端route")
    private String route;

    @ApiModelProperty("前端图标")
    private String icon;

    @ApiModelProperty("排序")
    private Integer sort;

}
