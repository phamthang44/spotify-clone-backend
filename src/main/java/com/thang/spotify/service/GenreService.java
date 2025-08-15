package com.thang.spotify.service;

import com.thang.spotify.dto.response.genre.GenreResponse;

import java.util.List;

public interface GenreService {

    List<GenreResponse> getAllGenreResponsesDefault();

}
