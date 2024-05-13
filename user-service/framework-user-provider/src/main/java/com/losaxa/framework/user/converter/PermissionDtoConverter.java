package com.losaxa.framework.user.converter;

import com.losaxa.core.common.converter.ObjectConverter;
import com.losaxa.framework.user.domian.Permission;
import com.losaxa.framework.user.dto.PermissionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionDtoConverter extends ObjectConverter<PermissionDto, Permission> {
}
