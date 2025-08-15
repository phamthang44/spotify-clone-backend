package com.thang.spotify.repository;

import com.thang.spotify.entity.FollowArtist;
import com.thang.spotify.entity.composite.FollowArtistId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowArtistRepository extends JpaRepository<FollowArtist, FollowArtistId> {
    List<FollowArtist> findByUserId(Long userId);
    List<FollowArtist> findByArtistId(Long artistId);
}
