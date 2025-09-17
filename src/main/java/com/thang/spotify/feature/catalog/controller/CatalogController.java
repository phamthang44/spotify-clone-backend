package com.thang.spotify.feature.catalog.controller;

import com.thang.spotify.feature.catalog.dto.CatalogDTO;
import com.thang.spotify.common.dto.ResponseData;
import com.thang.spotify.feature.catalog.service.CatalogService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/catalog")
@Slf4j
@Validated
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogService catalogService;

    @GetMapping("")
    @Operation(method= "GET", summary="Get default data", description="This API allows you to take the default data of the catalog, including songs, albums, artists, and genres.")
    public ResponseEntity<ResponseData<CatalogDTO>> getDefaultCatalog() {
        log.info("Fetching default catalog");

        ResponseData<CatalogDTO> responseData = new ResponseData<>(HttpStatus.OK.value(), "Default catalog fetched successfully", catalogService.getDefaultData());

        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }



}
