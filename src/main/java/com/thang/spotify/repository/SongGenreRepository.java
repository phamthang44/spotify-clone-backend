package com.thang.spotify.repository;

import com.thang.spotify.entity.SongGenre;
import com.thang.spotify.entity.composite.SongGenreId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongGenreRepository extends JpaRepository<SongGenre, SongGenreId> {

}
