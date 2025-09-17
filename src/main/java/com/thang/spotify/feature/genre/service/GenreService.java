package com.thang.spotify.feature.genre.service;

import com.thang.spotify.feature.genre.dto.response.GenreResponse;
import com.thang.spotify.feature.genre.entity.Genre;

import java.util.List;

public interface GenreService {

    List<GenreResponse> getAllGenreResponsesDefault();
    Genre getGenreById(Long id);
    List<GenreResponse> getAllGenreResponses();

    List<Genre> searchGenres(String query);
}
