package com.thang.spotify.service.impl;

import com.thang.spotify.common.mapper.AlbumMapper;
import com.thang.spotify.dto.response.album.AlbumResponse;
import com.thang.spotify.dto.response.song.SongResponse;
import com.thang.spotify.entity.Album;
import com.thang.spotify.entity.Genre;
import com.thang.spotify.entity.Song;
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
    public AlbumResponse getAlbumById(Long id) {
        return null;
    }

    @Override
    public List<Album> getAlbumsByGenre(Long genreId) {
        log.info("Album service: loading albums by genre ID {}", genreId);
        validator.validateGenreId(genreId);

        Genre genre = genreService.getGenreById(genreId);
        if (genre != null) {
//            List<Album> albums = albumRepository.findByGenre(genre);
//            if (!albums.isEmpty()) {
//                return albums;
//            } else {
//                log.warn("No albums found for genre ID: {}", genreId);
//            }
        } else {
            log.error("Genre not found with ID: {}", genreId);
        }
        return List.of();
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
}
