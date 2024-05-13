package com.losaxa.framework.user.converter;

import com.losaxa.core.common.converter.ObjectConverter;
import com.losaxa.framework.user.domian.Role;
import com.losaxa.framework.user.domian.User;
import com.losaxa.framework.user.dto.RoleDto;
import com.losaxa.framework.user.dto.UpdateAdminDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleDtoConverter extends ObjectConverter<RoleDto, Role> {
}
