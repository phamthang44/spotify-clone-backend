package com.thang.spotify.service;

import com.thang.spotify.dto.response.GenrePageDTO;

public interface BrowseService {

    GenrePageDTO getGenrePageById(Long genreId);

}
