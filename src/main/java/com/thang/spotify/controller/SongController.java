package com.thang.spotify.controller;

import com.thang.spotify.dto.response.ResponseData;
import com.thang.spotify.dto.response.song.SongResponse;
import com.thang.spotify.service.SongService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/songs")
@Slf4j
@Validated
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;

    @Operation(method = "GET", summary = "Get 50 songs by genre", description = "This API allows you to get 50 songs by genre")
    @GetMapping("/genre/{genreId}")
    public ResponseEntity<ResponseData<List<SongResponse>>> getSongsByGenre(@Min(1) @PathVariable("genreId") Long genreId) {
        log.info("Song Controller: Fetching songs by genre ID: {}", genreId);

        List<SongResponse> songResponses = songService.getSongsByGenreId(genreId);

        return ResponseEntity.ok(ResponseData.<List<SongResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Songs fetched successfully")
                .data(songResponses)
                .build());
    }

    @GetMapping("/{id}")
    @Operation(method = "GET", summary = "Get song by ID", description = "This API allows you to get a song by its ID")
    public ResponseEntity<ResponseData<SongResponse>> getSongById(@Min(1) @PathVariable("id") Long id) {
        log.info("Song Controller: Fetching song by ID: {}", id);

        SongResponse songResponse = songService.getSongResponseById(id);

        return ResponseEntity.ok(ResponseData.<SongResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Song fetched successfully")
                .data(songResponse)
                .build());
    }

}
