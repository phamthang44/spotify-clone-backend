package com.thang.spotify.service.impl;

import com.thang.spotify.common.mapper.GenreMapper;
import com.thang.spotify.dto.response.genre.GenreResponse;
import com.thang.spotify.entity.Genre;
import com.thang.spotify.exception.ResourceNotFoundException;
import com.thang.spotify.repository.GenreRepository;
import com.thang.spotify.service.GenreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @Override
    public List<GenreResponse> getAllGenreResponses() {

        log.info("Genre Service: Fetching all genres");
        Pageable pageable = PageRequest.of(0, 50);

        Page<Genre> genres = genreRepository.findAll(pageable);
        if (genres.getContent().isEmpty()) {
            log.warn("Genre Service: No genres found in the database.");
            return new ArrayList<>();
        }
        return genres.getContent().stream().map(genreMapper::toGenreResponse).toList();
    }

    @Override
    public List<GenreResponse> getAllGenreResponsesDefault() {
        log.info("Genre Service: Loading all genres (0 to 50)");
        Page<Genre> genres = genreRepository.findAll(PageRequest.of(0, 50));

        if (genres.hasContent()) {
            return genres.getContent().stream()
                    .map(genreMapper::toGenreResponse)
                    .toList();
        }

        return List.of();
    }

    private void warnIfGenreNotFound(Long id) {
        if (!genreRepository.existsById(id)) {
            log.warn("Genre Service: Genre with ID {} not found", id);
            throw new ResourceNotFoundException("Genre not found with ID: " + id);
        }
    }

    @Override
    public Genre getGenreById(Long id) {
        log.info("Genre Service: Fetching genre by ID: {}", id);
        return genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with ID: " + id));
    }
}
