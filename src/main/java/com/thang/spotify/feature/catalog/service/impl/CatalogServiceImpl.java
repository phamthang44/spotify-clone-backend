package com.thang.spotify.feature.catalog.service.impl;

import com.thang.spotify.common.util.Util;
import com.thang.spotify.feature.album.service.AlbumService;
import com.thang.spotify.feature.artist.service.ArtistService;
import com.thang.spotify.feature.catalog.dto.CatalogDTO;
import com.thang.spotify.common.dto.PageResponse;
import com.thang.spotify.feature.album.dto.response.AlbumResponse;
import com.thang.spotify.feature.artist.dto.response.ArtistResponse;
import com.thang.spotify.feature.genre.dto.response.GenreResponse;
import com.thang.spotify.feature.catalog.service.CatalogService;
import com.thang.spotify.feature.genre.service.GenreService;
import com.thang.spotify.feature.song.dto.response.SongResponse;
import com.thang.spotify.feature.song.entity.Song;
import com.thang.spotify.common.exception.InvalidDataException;
import com.thang.spotify.feature.song.service.SongService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService {

    private final SongService songService;
    private final AlbumService albumService;
    private final ArtistService artistService;
    private final GenreService genreService;

    @Override
    public PageResponse<SongResponse> getSongs(int pageNo, int pageSize) {
        int page = Util.getPageNo(pageNo);

        if (pageNo < 0 || pageSize <= 0) {
            log.error("Invalid page number or page size: pageNo={}, pageSize={}", pageNo, pageSize);
            throw new InvalidDataException("Page number and size must be non-negative and positive respectively");
        }

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Song> songPage = songService.getSongsDefault(pageable);

        return PageResponse.<SongResponse>builder()
                .pageNo(songPage.getNumber())
                .pageSize(songPage.getSize())
                .totalPages(songPage.getTotalPages())
                .totalElements((int) songPage.getTotalElements())
                .items(songService.getConvertedSongResponses(songPage.getContent()))
                .build();
    }

    @Override
    public PageResponse<AlbumResponse> getAlbums(int pageNo, int pageSize) {
        return null;
    }

    @Override
    public PageResponse<ArtistResponse> getArtists(int pageNo, int pageSize) {
        return null;
    }

    @Override
    public PageResponse<GenreResponse> getGenres(int pageNo, int pageSize) {
        return null;
    }

    @Override
    public PageResponse<SongResponse> searchSongs(Pageable pageable, String[] keywords) {
        return null;
    }

    @Override
    public PageResponse<AlbumResponse> searchAlbums(Pageable pageable, String[] keywords) {
        return null;
    }

    @Override
    public PageResponse<ArtistResponse> searchArtists(Pageable pageable, String[] keywords) {
        return null;
    }

    @Override
    public PageResponse<GenreResponse> searchGenres(Pageable pageable, String[] keywords) {
        return null;
    }

    @Override
    public CatalogDTO getDefaultData() {

        List<AlbumResponse> albums = albumService.getDefaultAlbums();
        List<ArtistResponse> artists = artistService.getAllArtistResponsesDefault();
        List<GenreResponse> genres = genreService.getAllGenreResponsesDefault();
        return CatalogDTO.builder()
                .albums(albums)
                .artists(artists)
                .genres(genres)
                .build();
    }
}
