package com.thang.spotify.controller;

import com.thang.spotify.dto.response.ResponseData;
import com.thang.spotify.dto.response.user.UserResponse;
import com.thang.spotify.infra.security.SecurityUserDetails;
import com.thang.spotify.service.UserAssetService;
import com.thang.spotify.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Validated
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ResponseData<UserResponse>> getCurrentUser(@AuthenticationPrincipal SecurityUserDetails userDetails) {

        log.info("User controller: Fetching current user with ID: {}", userDetails.getId());

        UserResponse userResponse = userService.getUserResponse(userDetails.getId());
        return ResponseEntity.ok(new ResponseData<>(
                200, "User fetched successfully", userResponse
        ));
    }



}
