package com.losaxa.framework.user.enums;

import com.losaxa.core.common.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum PermissionType implements IEnum<Integer> {
    DIRECTORY(1, "目录"),
    MENU(2, "菜单"),
    BUTTON(3, "按钮"),
    ;

    private Integer value;

    private String display;
}
