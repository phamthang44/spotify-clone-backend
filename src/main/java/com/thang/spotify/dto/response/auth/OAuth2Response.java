package com.thang.spotify.dto.response.auth;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OAuth2Response {
    private String email;
    private String name;
    private String status;
    private boolean isExistingUser; // để frontend biết login hay signup
    private List<String> requiredFields;
}
