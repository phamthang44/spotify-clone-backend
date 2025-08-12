package com.thang.spotify.entity.composite;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FollowArtistId implements Serializable {
    private Long userId;
    private Long artistId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FollowArtistId)) return false;
        FollowArtistId that = (FollowArtistId) o;
        return userId.equals(that.userId) && artistId.equals(that.artistId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, artistId);
    }
}
