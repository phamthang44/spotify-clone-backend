package com.thang.spotify.service;

import com.thang.spotify.dto.request.playlist.PlaylistPutRequest;
import com.thang.spotify.dto.response.playlist.PlaylistResponse;

import java.util.List;

public interface PlaylistService {

    PlaylistResponse getCurrentUserPlaylist(Long userId);
    PlaylistResponse createPlaylistForUser(Long userId);
    List<PlaylistResponse> getAllPlaylistsByUser(Long userId);
    PlaylistResponse editPlaylist(PlaylistPutRequest playlistPutRequest);
    void deletePlaylist(Long playlistId, Long userId);

}
