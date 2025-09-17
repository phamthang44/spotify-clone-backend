package com.thang.spotify.feature.playlist.repository;

import com.thang.spotify.feature.playlist.entity.Playlist;
import com.thang.spotify.feature.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    Playlist findByUser(User user);

    Playlist findTopByUserOrderByIdDesc(User user);
    List<Playlist> findAllByUser(User user);

    @Query("SELECT p FROM Playlist p WHERE p.user.id = :userId AND p.isDeleted = false")
    List<Playlist> findAllByUserIdAndDeletedIsFalse(Long userId);

    boolean existsByIdAndUserId(Long playlistId, Long userId);

    Optional<Playlist> findByIdAndUserId(Long playlistId, Long userId);

}
