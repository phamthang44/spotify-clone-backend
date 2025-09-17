package com.thang.spotify.feature.playlist.repository;

import com.thang.spotify.feature.playlist.entity.PlaylistSong;
import com.thang.spotify.feature.playlist.entity.composite.PlayListSongId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistSongRepository extends JpaRepository<PlaylistSong, PlayListSongId> {

}
