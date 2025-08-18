package com.thang.spotify.dto.response.auth;

import com.thang.spotify.dto.response.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse implements Serializable {

    private String accessToken;
    private String refreshToken;

}
