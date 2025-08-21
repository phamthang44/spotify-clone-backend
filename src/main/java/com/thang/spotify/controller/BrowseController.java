package com.thang.spotify.controller;

import com.thang.spotify.dto.response.GenrePageDTO;
import com.thang.spotify.dto.response.ResponseData;
import com.thang.spotify.dto.response.ResultsResponse;
import com.thang.spotify.dto.response.SectionDTO;
import com.thang.spotify.dto.response.genre.GenreResponse;
import com.thang.spotify.service.AlbumService;
import com.thang.spotify.service.BrowseService;
import com.thang.spotify.service.GenreService;
import com.thang.spotify.service.SongService;
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
@RequestMapping("/api/v1/browse")
@Slf4j
@Validated
@RequiredArgsConstructor
public class BrowseController {

    private final BrowseService browseService;

    @GetMapping("/genres/{genreId}")
    public ResponseEntity<ResponseData<GenrePageDTO>> getGenrePage(@PathVariable Long genreId) {
        log.info("Fetching genre page for genre ID: {}", genreId);
        GenrePageDTO sectionPage = browseService.getGenrePageById(genreId);
        return ResponseEntity.ok(new ResponseData<>(
                HttpStatus.OK.value(), "Genre page fetched successfully", sectionPage
        ));
    }



}
