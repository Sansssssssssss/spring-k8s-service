package com.losaxa.framework.user.converter;

import com.losaxa.core.common.converter.ObjectConverter;
import com.losaxa.framework.user.domian.User;
import com.losaxa.framework.user.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDtoConverter extends ObjectConverter<UserDto, User> {
}
