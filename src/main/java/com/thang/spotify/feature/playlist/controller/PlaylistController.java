package com.thang.spotify.feature.playlist.controller;

import com.thang.spotify.common.dto.ResponseData;
import com.thang.spotify.feature.playlist.dto.response.PlaylistResponse;
import com.thang.spotify.infra.security.SecurityUserDetails;
import com.thang.spotify.feature.playlist.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/playlists")
@Slf4j
@Validated
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistService playlistService;

    @PostMapping
    public ResponseEntity<ResponseData<PlaylistResponse>> createPlaylist(@AuthenticationPrincipal SecurityUserDetails user) {

        if (user == null) {
            ResponseData<PlaylistResponse> responseData = ResponseData.<PlaylistResponse>builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .message("Unauthorized")
                    .data(null)
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseData);
        }

        PlaylistResponse playlist = playlistService.createPlaylistForUser(user.getId());
        ResponseData<PlaylistResponse> responseData = ResponseData.<PlaylistResponse>builder()
                .status(HttpStatus.CREATED.value())
                .message("Playlist created successfully")
                .data(playlist)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseData);
    }

    @GetMapping
    public ResponseEntity<ResponseData<List<PlaylistResponse>>> getPlaylists(@AuthenticationPrincipal SecurityUserDetails user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        List<PlaylistResponse> playlist = playlistService.getAllPlaylistsByUser(user.getId());
        ResponseData<List<PlaylistResponse>> responseData = ResponseData.<List<PlaylistResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Playlist retrieved successfully")
                .data(playlist)
                .build();
        return ResponseEntity.ok(responseData);
    }

    @DeleteMapping("/{playlistId}")
    public ResponseEntity<ResponseData<Void>> deletePlaylist(@PathVariable Long playlistId,
                                                             @AuthenticationPrincipal SecurityUserDetails user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        playlistService.deletePlaylist(playlistId, user.getId());
        ResponseData<Void> responseData = ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Playlist deleted successfully")
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(responseData);
    }

}
