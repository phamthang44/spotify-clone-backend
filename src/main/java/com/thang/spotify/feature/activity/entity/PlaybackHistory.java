package com.thang.spotify.feature.activity.entity;

import com.thang.spotify.common.enums.Device;
import com.thang.spotify.feature.song.entity.Song;
import com.thang.spotify.feature.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "playback_history")
@Entity
public class PlaybackHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id", nullable = false)
    private Song song;

    @Column(name = "played_at", nullable = false, updatable = false)
    private LocalDateTime playedAt;

    @Column(name = "device", nullable = false)
    private Device device;

    @PrePersist
    public void prePersist() {
        playedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        playedAt = LocalDateTime.now();
    }
}
