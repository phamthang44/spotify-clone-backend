package com.thang.spotify.controller;

import com.thang.spotify.dto.response.ResponseData;
import com.thang.spotify.dto.response.artist.ArtistResponse;
import com.thang.spotify.service.ArtistService;
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
@RequestMapping("/api/v1/artists")
@Slf4j
@Validated
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;

    @Operation()
    @GetMapping("/{artistId}")
    public ResponseEntity<ResponseData<ArtistResponse>> getArtistById(@Min(1) @PathVariable Long artistId) {
        log.info("Artist controller : Fetching artist with ID: {}", artistId);
        ArtistResponse artistResponse = artistService.getArtistResponseById(artistId);
        return ResponseEntity.ok(new ResponseData<>(
                HttpStatus.OK.value(), "Artist fetched successfully", artistResponse
        ));
    }
}
