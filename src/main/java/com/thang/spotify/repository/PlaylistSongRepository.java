package com.thang.spotify.repository;

import com.thang.spotify.entity.PlaylistSong;
import com.thang.spotify.entity.composite.PlayListSongId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistSongRepository extends JpaRepository<PlaylistSong, PlayListSongId> {

}
