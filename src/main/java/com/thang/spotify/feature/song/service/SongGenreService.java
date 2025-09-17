package com.thang.spotify.feature.song.service;

import com.thang.spotify.common.dto.PageResponse;
import com.thang.spotify.feature.song.dto.response.SongResponse;

public interface SongGenreService {

    PageResponse<SongResponse> getSongsByGenre(Long genreId);
}
