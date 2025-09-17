package com.thang.spotify.feature.song.service;

import com.thang.spotify.common.dto.PageResponse;
import com.thang.spotify.feature.song.dto.response.SearchSongResponse;
import com.thang.spotify.feature.song.dto.response.SongResponse;
import com.thang.spotify.feature.song.entity.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SongService {

    List<SongResponse> getAllSongs();
    PageResponse<SongResponse> getSongResponsesByPage(int pageNo, int pageSize);
//    Page<Song> getSongsByPage(int pageNo, int pageSize);
    Page<Song> getSongsByArtistId(Long artistId, int pageNo, int pageSize);
    Page<Song> getSongsByAlbumId(Long albumId, int pageNo, int pageSize);
    Page<Song> getSongsByArtistId(Long artistId, Pageable pageable);
    Song getSongById(Long id);
    SongResponse getSongResponseById(Long id);
    List<SearchSongResponse> searchSongs(String keyword);
    Page<Song> getSongsDefault(Pageable pageable);
    List<SongResponse> getConvertedSongResponses(List<Song> songs);
    List<SongResponse> getSongsByGenreId(Long genreId);
    List<Song> getSongsEntityByGenreId(Long genreId);
//    List<Song> getSongsByReleaseDate();
    List<SongResponse> getLatestSongsByGenreOrderByReleaseDate(Long genreId);
    List<SongResponse> getSongsByGenreIdAndOrderByLikeCount(Long genreId);
}
