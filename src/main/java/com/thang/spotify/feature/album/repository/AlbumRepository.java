package com.thang.spotify.feature.album.repository;

import com.thang.spotify.feature.album.entity.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    Page<Album> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);

//    Page<Album> getAllDefaultsAlbums(Pageable pageable);

}
