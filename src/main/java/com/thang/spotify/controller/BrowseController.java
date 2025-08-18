package com.thang.spotify.controller;

import com.thang.spotify.dto.response.GenrePageDTO;
import com.thang.spotify.dto.response.ResponseData;
import com.thang.spotify.dto.response.genre.GenreResponse;
import com.thang.spotify.service.AlbumService;
import com.thang.spotify.service.BrowseService;
import com.thang.spotify.service.GenreService;
import com.thang.spotify.service.SongService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public GenrePageDTO getGenrePage(@PathVariable Long genreId) {
//        GenreResponse genre = genreService.getGenreById(genreId);

//        List<SongDTO> topSongs = songService.getTopSongsByGenre(genreId);
//        List<AlbumDTO> featuredAlbums = albumService.getFeaturedAlbumsByGenre(genreId);
//        List<SongDTO> recommended = songService.getRecommendedSongsByGenre(genreId);

        return GenrePageDTO.builder()
                .genreId(genreId)
                .genreName("test")
                .sections(List.of())
                .build();
    }

}
