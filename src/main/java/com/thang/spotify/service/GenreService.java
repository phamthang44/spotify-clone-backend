package com.thang.spotify.service;

import com.thang.spotify.dto.response.genre.GenreResponse;
import com.thang.spotify.entity.Genre;

import java.util.List;

public interface GenreService {

    List<GenreResponse> getAllGenreResponsesDefault();
    Genre getGenreById(Long id);
    List<GenreResponse> getAllGenreResponses();

    List<Genre> searchGenres(String query);
}
