package com.thang.spotify.feature.search.service.impl;

import com.thang.spotify.common.util.Util;
import com.thang.spotify.feature.album.service.AlbumService;
import com.thang.spotify.feature.search.dto.ResultsResponse;
import com.thang.spotify.feature.artist.service.ArtistService;
import com.thang.spotify.feature.genre.service.GenreService;
import com.thang.spotify.feature.search.service.SearchService;
import com.thang.spotify.feature.song.service.SongService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final SongService songService;
    private final AlbumService albumService;
    private final GenreService genreService;
    private final ArtistService artistService;

    @Override
    public ResultsResponse<?> search(String query) {

        log.info("Search service: searching for query '{}'", query);
        if (Util.isNullOrBlank(query)) {
            log.error("Empty input keyword provided for search");
            return ResultsResponse.builder()
                    .total(0)
                    .results(List.of())
                    .build();
        }
        List<?> mergedResults = Stream.of(
                        songService.searchSongs(query),
                        albumService.searchByName(query),
                        artistService.searchArtists(query)
                )
                .flatMap(List::stream)
                .limit(5)
                .toList();
        return ResultsResponse.builder()
                .total(mergedResults.size())
                .results((List<Object>) mergedResults)
                .build();
    }

    private int getTotalResults(List<?> results) {



        return results != null ? results.size() : 0;
    }
}
