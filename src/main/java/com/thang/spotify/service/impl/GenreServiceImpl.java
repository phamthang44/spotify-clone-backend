package com.thang.spotify.service.impl;

import com.thang.spotify.common.mapper.GenreMapper;
import com.thang.spotify.dto.response.genre.GenreResponse;
import com.thang.spotify.entity.Genre;
import com.thang.spotify.repository.GenreRepository;
import com.thang.spotify.service.GenreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @Override
    public List<GenreResponse> getAllGenreResponsesDefault() {

        Page<Genre> genres = genreRepository.findAll(PageRequest.of(0, 50));

        if (genres.hasContent()) {
            return genres.getContent().stream()
                    .map(genreMapper::toGenreResponse)
                    .toList();
        }

        return List.of();
    }
}
