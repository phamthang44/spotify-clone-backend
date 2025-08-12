package com.thang.spotify.entity.composite;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;


@Embeddable
public class SongGenreId implements Serializable {
    private Long songId;
    private Long genreId;

    // equals() và hashCode() bắt buộc
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SongGenreId)) return false;
        SongGenreId that = (SongGenreId) o;
        return Objects.equals(songId, that.songId) &&
                Objects.equals(genreId, that.genreId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(songId, genreId);
    }
}
