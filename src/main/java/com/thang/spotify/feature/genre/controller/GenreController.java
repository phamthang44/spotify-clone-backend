package com.thang.spotify.feature.genre.controller;

import com.thang.spotify.common.dto.ResponseData;
import com.thang.spotify.feature.genre.dto.response.GenreResponse;
import com.thang.spotify.feature.genre.service.GenreService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/genres")
@Slf4j
@Validated
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping
    @Operation(
            method = "GET",
            summary = "Get all genres",
            description = "This API retrieves all genres from the database."
    )
    public ResponseEntity<ResponseData<List<GenreResponse>>> getAllGenres() {
        log.info("GenreController: Fetching all genres");
        List<GenreResponse> genreResponses = genreService.getAllGenreResponsesDefault();

        if (genreResponses.isEmpty()) {
            log.warn("GenreController: No genres found in the database.");
            return ResponseEntity.ok(new ResponseData<>(404, "No genres found"));
        }
        return ResponseEntity.ok(
                new ResponseData<>(HttpStatus.OK.value(), "Genres retrieved successfully", genreResponses)
        );
    }

}
