package com.thang.spotify.controller;

import com.thang.spotify.dto.response.ResponseData;
import com.thang.spotify.dto.response.album.AlbumResponse;
import com.thang.spotify.service.AlbumService;
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

@RestController
@RequestMapping("/api/v1/albums")
@Slf4j
@Validated
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumService albumService;

    @GetMapping("/{albumId}")
    @Operation(
            summary = "Get Album by ID",
            description = "Fetches the album details by its ID. Returns a response containing the album information."
    )
    public ResponseEntity<ResponseData<AlbumResponse>> getAlbumResponseById(@Min(1) @PathVariable Long albumId) {
        log.info("Album controller : Fetching album response for album ID: {}", albumId);
        AlbumResponse albumResponse = albumService.getAlbumResponseById(albumId);
        return ResponseEntity.ok(new ResponseData<>(
                HttpStatus.OK.value(), "Album response fetched successfully", albumResponse
        ));
    }

}
