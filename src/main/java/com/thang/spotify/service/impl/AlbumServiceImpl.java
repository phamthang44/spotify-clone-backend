package com.thang.spotify.service.impl;

import com.thang.spotify.common.mapper.AlbumMapper;
import com.thang.spotify.common.util.Util;
import com.thang.spotify.dto.response.album.AlbumResponse;
import com.thang.spotify.dto.response.album.SearchAlbumResponse;
import com.thang.spotify.dto.response.song.SearchSongResponse;
import com.thang.spotify.dto.response.song.SongResponse;
import com.thang.spotify.entity.Album;
import com.thang.spotify.entity.Genre;
import com.thang.spotify.entity.Song;
import com.thang.spotify.exception.InvalidDataException;
import com.thang.spotify.exception.ResourceNotFoundException;
import com.thang.spotify.repository.AlbumRepository;
import com.thang.spotify.service.AlbumService;
import com.thang.spotify.service.GenreService;
import com.thang.spotify.service.SongService;
import com.thang.spotify.service.validator.AlbumValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlbumServiceImpl implements AlbumService {

    private final AlbumRepository albumRepository;
    private final AlbumMapper albumMapper;
    private final AlbumValidator validator;
    private final GenreService genreService;
    private final SongService songService;

    @Override
    public List<Album> getAllAlbums() {
        return List.of();
    }

    @Override
    public List<AlbumResponse> getDefaultAlbums() {
        log.info("Album service : loading default albums");
        Page<Album> albumPage = albumRepository.findAll(PageRequest.of(0, 10));
        if (albumPage.hasContent()) {
            return albumPage.getContent().stream()
                    .map(album -> {
                        log.info("Album: {}", album.getTitle());
                        AlbumResponse albumResponse = albumMapper.toAlbumResponse(album);
                        log.info("Album response: {}", albumResponse.getTitle());
                        albumResponse.setArtistName(album.getArtist().getName());
                        return albumResponse;
                    })
                    .toList();
        }
        return List.of();
    }

    @Override
    public AlbumResponse getAlbumResponseById(Long id) {
        Util.validateNumber(id);
        log.info("Album service: loading album by ID {}", id);
        if (id <= 0) {
            log.error("Invalid album ID provided: {}", id);
            throw new InvalidDataException("Invalid album ID provided.");
        }

        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Album not found with ID: " + id));
        log.info("Album found: {}", album.getTitle());

        return getAlbumResponse(album);
    }

    @Override
    public List<AlbumResponse> getAlbumsByGenreResponse(Long genreId) {

        Genre genre = genreService.getGenreById(genreId);
        if (genre != null) {
            List<Song> songs = songService.getSongsEntityByGenreId(genre.getId());
        } else {

        }

        return List.of();
    }

    @Override
    public List<SearchAlbumResponse> searchByName(String albumName) {
        if (Util.isNullOrBlank(albumName)) {
            log.error("AlbumService: Empty album name provided for search");
            return List.of();
        }

        Pageable pageable = PageRequest.of(0, 10);

        List<Album> albums = albumRepository.findByTitleContainingIgnoreCase(albumName, pageable).getContent();
        log.info("Album service : searching by name : {}", albumName);
        if (!albums.isEmpty()) {
            return albums.stream()
                    .map(album -> {
                        SearchAlbumResponse searchAlbumResponse = new SearchAlbumResponse();
                        searchAlbumResponse.setType("album");
                        searchAlbumResponse.setAlbum(getAlbumResponse(album));
                        return searchAlbumResponse;
                    })
                    .toList();
        }
        log.warn("AlbumService: No albums found with name: {}", albumName);
        return List.of();
    }

    private AlbumResponse getAlbumResponse(Album album) {
        return AlbumResponse.builder()
                .artistName(album.getArtist().getName())
                .title(album.getTitle())
                .id(album.getId())
                .releaseDate(album.getReleaseDate())
                .coverImageUrl(album.getCoverImageUrl())
                .totalTracks(album.getTotalTracks())
                .description(album.getDescription())
                .build();
    }

}
