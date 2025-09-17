package com.thang.spotify.feature.browse.service;

import com.thang.spotify.feature.genre.dto.response.GenrePageDTO;

public interface    BrowseService {

    GenrePageDTO getGenrePageById(Long genreId);
}
