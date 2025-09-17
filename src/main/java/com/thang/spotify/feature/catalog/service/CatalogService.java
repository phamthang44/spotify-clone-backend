package com.thang.spotify.feature.catalog.service;

import com.thang.spotify.feature.catalog.dto.CatalogDTO;
import com.thang.spotify.common.dto.PageResponse;
import com.thang.spotify.feature.album.dto.response.AlbumResponse;
import com.thang.spotify.feature.artist.dto.response.ArtistResponse;
import com.thang.spotify.feature.genre.dto.response.GenreResponse;
import com.thang.spotify.feature.song.dto.response.SongResponse;
import org.springframework.data.domain.Pageable;

public interface CatalogService {

//    PageResponse<ArtistResponse> getArtists(Integer limit, Integer offset);
//    PageResponse<AlbumResponse> getAlbums(Integer limit, Integer offset);
//    PageResponse<AlbumResponse> getAlbumsByArtistId(String artistId, Integer limit, Integer offset);
//    PageResponse<AlbumResponse> getAlbumsByGenreId(String genreId, Integer limit, Integer offset);
//    PageResponse<AlbumResponse> getAlbumsBySongId(String songId, Integer limit, Integer offset);
//    PageResponse<AlbumResponse> getAlbumsByArtistName(String artistName, Integer limit, Integer offset);
//    PageResponse<AlbumResponse> getAlbumsByTitle(String title, Integer limit, Integer offset);
//    PageResponse<AlbumResponse> getAlbumsByReleaseDate(String releaseDate, Integer limit, Integer offset);
//    PageResponse<AlbumResponse> getAlbumsByReleaseDateRange(String startDate, String endDate, Integer limit, Integer offset);
//    PageResponse<AlbumResponse> getAlbumsByDescription(String description, Integer limit, Integer offset);
//    PageResponse<SongResponse> getSongs(Integer limit, Integer offset);
//    PageResponse<SongResponse> getSongsByArtistId(String artistId, Integer limit, Integer offset);
//    PageResponse<SongResponse> getSongsByAlbumId(String albumId, Integer limit, Integer offset);
//    PageResponse<SongResponse> getSongsByGenreId(String genreId, Integer limit, Integer offset);
//    PageResponse<SongResponse> getSongsByArtistName(String artistName, Integer limit, Integer offset);
    PageResponse<SongResponse> getSongs(int pageNo, int pageSize);
    PageResponse<AlbumResponse> getAlbums(int pageNo, int pageSize);
    PageResponse<ArtistResponse> getArtists(int pageNo, int pageSize);
    PageResponse<GenreResponse> getGenres(int pageNo, int pageSize);
    PageResponse<SongResponse> searchSongs(Pageable pageable, String[] keywords);
    PageResponse<AlbumResponse> searchAlbums(Pageable pageable, String[] keywords);
    PageResponse<ArtistResponse> searchArtists(Pageable pageable, String[] keywords);
    PageResponse<GenreResponse> searchGenres(Pageable pageable, String[] keywords);

    CatalogDTO getDefaultData();

}
