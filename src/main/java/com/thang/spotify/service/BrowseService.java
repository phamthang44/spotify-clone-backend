package com.thang.spotify.service;

import com.thang.spotify.dto.response.GenrePageDTO;
import com.thang.spotify.dto.response.ResultsResponse;

public interface BrowseService {

    GenrePageDTO getGenrePageById(Long genreId);
}
