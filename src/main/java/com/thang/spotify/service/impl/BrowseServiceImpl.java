package com.thang.spotify.service.impl;

import com.thang.spotify.dto.response.GenrePageDTO;
import com.thang.spotify.dto.response.SectionDTO;
import com.thang.spotify.dto.response.album.AlbumResponse;
import com.thang.spotify.dto.response.song.SongResponse;
import com.thang.spotify.exception.InvalidDataException;
import com.thang.spotify.exception.ResourceNotFoundException;
import com.thang.spotify.service.AlbumService;
import com.thang.spotify.service.BrowseService;
import com.thang.spotify.service.GenreService;
import com.thang.spotify.service.SongService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BrowseServiceImpl implements BrowseService {

    private final SongService songService;
    private final GenreService genreService;
    private final AlbumService albumService;

    @Override
    public GenrePageDTO getGenrePageById(Long genreId) {
        log.info("Browse service: loading genre page by ID {}", genreId);

        // Validate the genre ID
        if (genreId == null || genreId <= 0) {
            throw new IllegalArgumentException("Invalid genre ID provided.");
        }

        // Fetch the genre by ID
        var genre = genreService.getGenreById(genreId);
        if (genre == null) {
            throw new ResourceNotFoundException("Genre not found for ID: " + genreId);
        }

        // Fetch songs and albums for the genre
        var songs = songService.getSongsByGenreId(genreId);

        List<SectionDTO> sections = List.of(
                SectionDTO.<SongResponse>builder()
                        .title("Top Songs")
                        .items(songs)
                        .build(),
                SectionDTO.<AlbumResponse>builder()
                        .title("Albums")
                        .items(albumService.getAlbumsByGenre(genreId))
                        .build()
        );


        // Create and return the GenrePageDTO
        return GenrePageDTO.builder()
                .genreId(genreId)
                .genreName(genre.getName())
                .sections();
    }
}
