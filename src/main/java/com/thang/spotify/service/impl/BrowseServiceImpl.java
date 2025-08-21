package com.thang.spotify.service.impl;

import com.thang.spotify.common.util.Util;
import com.thang.spotify.dto.response.GenrePageDTO;
import com.thang.spotify.dto.response.ResultsResponse;
import com.thang.spotify.dto.response.SectionDTO;
import com.thang.spotify.dto.response.song.SongResponse;
import com.thang.spotify.exception.ResourceNotFoundException;
import com.thang.spotify.service.BrowseService;
import com.thang.spotify.service.GenreService;
import com.thang.spotify.service.SongService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BrowseServiceImpl implements BrowseService {

    private final SongService songService;
    private final GenreService genreService;

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
        List<SectionDTO<?>> sections = List.of(
                SectionDTO.<SongResponse>builder()
                        .title("Top Songs")
                        .items(songService.getSongsByGenreIdAndOrderByLikeCount(genreId))
                        .build(),
                SectionDTO.<SongResponse>builder()
                        .title("New Releases")
                        .items(songService.getLatestSongsByGenreOrderByReleaseDate(genreId))
                        .build()
        );


        // Create and return the GenrePageDTO
        return GenrePageDTO.builder()
                .genreId(genreId)
                .genreName(genre.getName())
                .sections(sections)
                .build();
    }

}
