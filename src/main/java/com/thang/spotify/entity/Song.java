package com.thang.spotify.entity;

import com.thang.spotify.common.enums.SongStatus;
import com.thang.spotify.common.util.SongStatusConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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
@Table(name = "song")
public class Song extends BaseEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id", nullable = false)
    private Album album;

    @Column(name = "duration", nullable = false)
    private int duration; // Duration in seconds

    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SongGenre> songGenres = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    @Column(name = "release_date", nullable = false)
    private LocalDateTime releaseDate; // Release date of the song

    @Column(name = "cover_url")
    private String coverImageUrl; // URL of the song's cover image

    @Column(name = "audio_url")
    private String audioUrl; // URL of the song's audio file

    @Convert(converter = SongStatusConverter.class)
    @Column(name = "status", columnDefinition = "e_song_status", nullable = false)
    private SongStatus status; // Status of the song (e.g., PUBLISHED, UNPUBLISHED)

    @Column(name = "play_count", nullable = false)
    private int playCount; // Number of times the song has been played

    @Column(name = "like_count", nullable = false)
    private int likeCount; // Number of likes the song has received

    @ManyToMany(mappedBy = "likedSongs")
    private Set<User> likedByUsers = new HashSet<>();

    @OneToMany(mappedBy = "song")
    private List<PlaylistSong> playlistSongs = new ArrayList<>();

    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlaybackHistory> playbackHistories = new ArrayList<>();



}
