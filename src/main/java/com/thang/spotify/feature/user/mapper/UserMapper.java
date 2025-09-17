package com.thang.spotify.feature.user.mapper;

import com.thang.spotify.feature.auth.dto.request.RegisterRequest;
import com.thang.spotify.feature.user.dto.response.UserResponse;
import com.thang.spotify.feature.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "dateOfBirth", ignore = true)
    User toUser(RegisterRequest dto);

    UserResponse toUserResponse(User user);

}
