package com.thang.spotify.entity.composite;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PlayListSongId implements Serializable {
    private Long playListId;
    private Long songId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayListSongId)) return false;
        PlayListSongId that = (PlayListSongId) o;
        return playListId.equals(that.playListId) && songId.equals(that.songId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(songId, playListId);
    }
}
