package com.thang.spotify.entity;

import com.thang.spotify.common.enums.Privacy;
import com.thang.spotify.common.util.PrivacyEnumConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "playlist")
public class Playlist extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User user;

    @Column(name = "cover_url")
    private String coverImageUrl;

    @Column(name = "description")
    private String description;

    @Convert(converter = PrivacyEnumConverter.class)
    @Column(columnDefinition = "e_privacy", nullable = false)
    private Privacy privacy;

    @OneToMany(mappedBy = "playlist")
    @OrderBy("trackNumber ASC")
    private List<PlaylistSong> playlistSongs = new ArrayList<>();

}
