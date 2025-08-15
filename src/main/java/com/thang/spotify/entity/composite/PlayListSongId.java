package com.thang.spotify.entity.composite;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PlayListSongId implements Serializable {

    private Long playlistId;
    private Long songId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayListSongId)) return false;
        PlayListSongId that = (PlayListSongId) o;
        return playlistId.equals(that.playlistId) && songId.equals(that.songId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(songId, playlistId);
    }
}
