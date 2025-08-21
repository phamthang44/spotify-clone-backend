package com.thang.spotify.repository;

import com.thang.spotify.common.enums.SongStatus;
import com.thang.spotify.entity.Song;
import com.thang.spotify.entity.SongGenre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    @Query("SELECT s FROM Song s WHERE s.status = :status")
    Page<Song> findAllByStatus(@Param("status") SongStatus status, Pageable pageable);

    @Query(value = "SELECT * FROM song s WHERE s.status = 'PUBLISHED'", nativeQuery = true)
    Page<Song> findAllByStatusPublished(Pageable pageable);

    @Query("SELECT s FROM Song s")
    List<Song> getSongs(Pageable pageable);


    @Query("""
        SELECT s FROM Song s
        JOIN s.songGenres sg
        WHERE sg.genre.id = :genreId
    """)
    Page<Song> findBySongGenreId(@Param("genreId") Long genreId, Pageable pageable);

    @Query("SELECT s FROM Song s WHERE s.genre.id = :genreId")
    Page<Song> findByGenreId(@Param("genreId") Long genreId, Pageable pageable);

    Page<Song> findAllByOrderByReleaseDateDesc(Pageable pageable);

    Page<Song> findAllByGenreIdOrderByLikeCountDesc(Long genreId, Pageable pageable);

    Page<Song> findAllByGenreIdOrderByReleaseDateDesc(Long genreId, Pageable pageable);

    @Query("SELECT s FROM Song s WHERE LOWER(s.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Song> findByNameContainingIgnoreCase(@Param("keyword") String keyword, Pageable pageable);
}
