package com.thang.spotify.service;

import com.thang.spotify.dto.response.PageResponse;
import com.thang.spotify.dto.response.song.SongResponse;

public interface SongGenreService {

    PageResponse<SongResponse> getSongsByGenre(Long genreId);
}
