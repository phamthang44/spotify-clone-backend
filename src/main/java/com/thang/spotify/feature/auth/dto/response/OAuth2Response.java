package com.thang.spotify.feature.auth.dto.response;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OAuth2Response implements Serializable {
    private String email;
    private String name;
    private String status;
    private boolean isExistingUser; // để frontend biết login hay signup
    private List<String> requiredFields;
}
