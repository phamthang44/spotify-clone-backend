package com.thang.spotify.controller;


import com.thang.spotify.dto.response.ResponseData;
import com.thang.spotify.dto.response.ResultsResponse;
import com.thang.spotify.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/v1/search")
@Slf4j
@Validated
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/query={keyword}")
    @Operation(
            summary = "Search by Keyword",
            description = "Searches for songs, albums, or artists based on the provided keyword. Returns a paginated list of results.")
    public ResponseEntity<ResponseData<ResultsResponse<?>>> searchByKeyword(@PathVariable String keyword) {
        log.info("Searching for keyword: {}", keyword);
        ResultsResponse<?> response = searchService.search(keyword);
        return ResponseEntity.ok(new ResponseData<>(
                HttpStatus.OK.value(), "Search results fetched successfully", response
        ));
    }

}
