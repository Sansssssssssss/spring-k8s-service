package com.losaxa.framework.user.enums;

import com.losaxa.core.common.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public enum UserType implements IEnum<Integer> {

    ADMIN_TYPE(1, "管理员"),
    NORMAL_TYPE(2, "普通用户"),
    ;

    private Integer value;
    private String  display;

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDisplay() {
        return display;
    }

}
