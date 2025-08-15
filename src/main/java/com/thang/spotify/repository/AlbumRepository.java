package com.thang.spotify.repository;

import com.thang.spotify.entity.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

//    Page<Album> getAllDefaultsAlbums(Pageable pageable);

}
