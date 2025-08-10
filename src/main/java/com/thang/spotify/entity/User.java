package com.thang.spotify.entity;

import com.thang.spotify.common.enums.Gender;
import com.thang.spotify.common.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user")
public class User extends BaseEntity {

    @Column(unique = true, nullable = false, name = "display_name")
    private String displayName;

    @Column(nullable = false, name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "gender")
    private Gender gender;

    @Column(name = "phone")
    private String phone;

    @Column(nullable = false, name = "email", unique = true)
    private String email;

    @Column(nullable = false, name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status")
    private UserStatus status;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(nullable = false, name = "role_id")
    private Role role;

    @Column(name = "avatar_url")
    private String avatarUrl;

}
