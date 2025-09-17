package com.thang.spotify.feature.album.entity;

import com.thang.spotify.common.entity.BaseEntity;
import com.thang.spotify.feature.artist.entity.Artist;
import com.thang.spotify.feature.song.entity.Song;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "album")
public class Album extends BaseEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @Column(name = "cover_url")
    private String coverImageUrl;

    @Column(name = "release_date", nullable = false)
    private LocalDateTime releaseDate;

    @Column(name = "total_tracks", nullable = false)
    private int totalTracks;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Song> songs = new ArrayList<>();

    @Column(name = "description", nullable = false)
    private String description;

}
