package com.thang.spotify.repository;

import com.thang.spotify.entity.Playlist;
import com.thang.spotify.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    Playlist findByUser(User user);
}
