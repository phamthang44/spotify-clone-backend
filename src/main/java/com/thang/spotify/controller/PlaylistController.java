package com.thang.spotify.controller;

import com.thang.spotify.dto.response.ResponseData;
import com.thang.spotify.dto.response.playlist.PlaylistResponse;
import com.thang.spotify.infra.security.SecurityUserDetails;
import com.thang.spotify.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
