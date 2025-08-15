package com.thang.spotify.repository;

import com.thang.spotify.common.enums.SongStatus;
import com.thang.spotify.entity.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    @Query("SELECT s FROM Song s WHERE s.status = :status")
    Page<Song> findAllByStatus(@Param("status") SongStatus status, Pageable pageable);

    @Query(value = "SELECT * FROM song s WHERE s.status = 'PUBLISHED'", nativeQuery = true)
    Page<Song> findAllByStatusPublished(Pageable pageable);

    @Query("SELECT s FROM Song s")
    List<Song> getSongs(Pageable pageable);

}
