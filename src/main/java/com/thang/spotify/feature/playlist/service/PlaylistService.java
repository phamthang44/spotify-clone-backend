package com.thang.spotify.feature.playlist.service;

import com.thang.spotify.feature.playlist.dto.request.PlaylistPutRequest;
import com.thang.spotify.feature.playlist.dto.response.PlaylistResponse;

import java.util.List;

public interface PlaylistService {

    PlaylistResponse getCurrentUserPlaylist(Long userId);
    PlaylistResponse createPlaylistForUser(Long userId);
    List<PlaylistResponse> getAllPlaylistsByUser(Long userId);
    PlaylistResponse editPlaylist(PlaylistPutRequest playlistPutRequest);
    void deletePlaylist(Long playlistId, Long userId);

}
