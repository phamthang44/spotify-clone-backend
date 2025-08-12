package com.thang.spotify.dto.response.auth;

import com.thang.spotify.dto.response.ResponseData;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class LoginResponse  extends ResponseData<Void> implements Serializable {

    private int status;
    private String message;
    private String accessToken;

    public LoginResponse(int status, String message, String accessToken) {
        super(status, message);
        this.status = status;
        this.message = message;
        this.accessToken = accessToken;
    }

}
