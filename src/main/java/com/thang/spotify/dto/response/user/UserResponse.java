package com.thang.spotify.dto.response.user;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse implements Serializable {

    private Long id;
    private String displayName;
    private String email;
    private LocalDate dateOfBirth;
    private String phone;
    private String avatarUrl;

}
