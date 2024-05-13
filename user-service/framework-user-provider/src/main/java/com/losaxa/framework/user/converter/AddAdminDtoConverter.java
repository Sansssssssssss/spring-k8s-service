package com.losaxa.framework.user.converter;

import com.losaxa.core.common.converter.ObjectConverter;
import com.losaxa.framework.user.domian.User;
import com.losaxa.framework.user.dto.AddAdminDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddAdminDtoConverter extends ObjectConverter<AddAdminDto, User> {
}
