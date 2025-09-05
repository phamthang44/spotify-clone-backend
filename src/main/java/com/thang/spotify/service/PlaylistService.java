package com.thang.spotify.service;

import com.thang.spotify.dto.response.playlist.PlaylistResponse;

public interface PlaylistService {

    PlaylistResponse getCurrentUserPlaylist(Long userId);


}
