package com.thang.spotify.entity;

import com.thang.spotify.common.enums.AuthProvider;
import com.thang.spotify.common.enums.Gender;
import com.thang.spotify.common.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tbl_user")
@AttributeOverride(name = "id", column = @Column(name = "id"))
public class User extends BaseEntity {

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, name = "gender", columnDefinition = "e_gender DEFAULT 'OTHER'")
    private Gender gender;

    @Column(name = "phone")
    private String phone;

    @Column(nullable = false, name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status", columnDefinition = "e_user_status DEFAULT 'ACTIVE'")
    private UserStatus status;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(nullable = false, name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Playlist> playlists = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "song_like",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id")
    )
    private Set<Song> likedSongs = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "auth_provider")
    private AuthProvider authProvider;

    @Column(name = "provider_id")
    private String providerId;

}
