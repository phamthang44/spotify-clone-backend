package com.thang.spotify.common.mapper;

import com.thang.spotify.common.enums.Gender;
import com.thang.spotify.dto.request.auth.RegisterRequest;
import com.thang.spotify.entity.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "dateOfBirth", ignore = true)
    User toUser(RegisterRequest dto);



}
