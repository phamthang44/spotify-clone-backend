package com.thang.spotify.feature.auth.dto.response;

import com.thang.spotify.common.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse implements Serializable {

    private String accessToken;
    private UserStatus userStatus;

}
