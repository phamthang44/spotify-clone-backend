package com.thang.spotify.feature.song.repository;

import com.thang.spotify.feature.song.entity.SongGenre;
import com.thang.spotify.feature.song.entity.composite.SongGenreId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongGenreRepository extends JpaRepository<SongGenre, SongGenreId> {

}
