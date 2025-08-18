package com.thang.spotify.service;

import com.thang.spotify.dto.response.PageResponse;
import com.thang.spotify.dto.response.song.SongResponse;
import com.thang.spotify.entity.Song;
import com.thang.spotify.entity.SongGenre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface SongService {

    List<SongResponse> getAllSongs();
    PageResponse<SongResponse> getSongResponsesByPage(int pageNo, int pageSize);
//    Page<Song> getSongsByPage(int pageNo, int pageSize);
    Page<Song> getSongsByArtistId(Long artistId, int pageNo, int pageSize);
    Page<Song> getSongsByAlbumId(Long albumId, int pageNo, int pageSize);
    Page<Song> getSongsByArtistId(Long artistId, Pageable pageable);
    PageResponse<SongResponse> getSongsByGenre(SongGenre songGenre);
    Song getSongById(Long id);
    SongResponse getSongResponseById(Long id);
    Page<Song> searchSongs(Specification<Song> spec, Pageable pageable);
    Page<Song> getSongsDefault(Pageable pageable);
    List<SongResponse> getConvertedSongResponses(List<Song> songs);
    List<SongResponse> getSongsDefault();
    List<SongResponse> getSongsByGenreId(Long genreId);
    List<Song> getSongsEntityByGenreId(Long genreId);

}
