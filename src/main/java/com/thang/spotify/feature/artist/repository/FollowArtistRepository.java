package com.thang.spotify.feature.artist.repository;

import com.thang.spotify.feature.user.entity.FollowArtist;
import com.thang.spotify.feature.user.entity.composite.FollowArtistId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowArtistRepository extends JpaRepository<FollowArtist, FollowArtistId> {
    List<FollowArtist> findByUserId(Long userId);
    List<FollowArtist> findByArtistId(Long artistId);
}
