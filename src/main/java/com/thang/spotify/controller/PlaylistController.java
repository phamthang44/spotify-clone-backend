package com.thang.spotify.controller;

import com.thang.spotify.dto.response.ResponseData;
import com.thang.spotify.dto.response.playlist.PlaylistResponse;
import com.thang.spotify.entity.User;
import com.thang.spotify.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/playlists")
@Slf4j
@Validated
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistService playlistService;

    @GetMapping("/me")
    public ResponseEntity<ResponseData<PlaylistResponse>> getPlaylistByUserId(@AuthenticationPrincipal User user) {
        PlaylistResponse playlist = playlistService.getCurrentUserPlaylist(user.getId());
        ResponseData<PlaylistResponse> responseData = ResponseData.<PlaylistResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Playlist retrieved successfully")
                .data(playlist)
                .build();
        return ResponseEntity.ok(responseData);
    }



}
