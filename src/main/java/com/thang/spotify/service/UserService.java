package com.thang.spotify.service;

import com.thang.spotify.dto.request.auth.RegisterRequest;

public interface UserService {

    long registerUser(RegisterRequest registerRequestDTO);

}
