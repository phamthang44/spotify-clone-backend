package com.thang.spotify.service.impl;

import com.thang.spotify.common.mapper.ArtistMapper;
import com.thang.spotify.common.util.Util;
import com.thang.spotify.dto.response.artist.ArtistResponse;
import com.thang.spotify.dto.response.artist.SearchArtistResponse;
import com.thang.spotify.entity.Artist;
import com.thang.spotify.exception.InvalidDataException;
import com.thang.spotify.exception.ResourceNotFoundException;
import com.thang.spotify.repository.ArtistRepository;
import com.thang.spotify.service.ArtistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;
    private final ArtistMapper artistMapper;

    @Override
    public List<Artist> getAllArtists() {
        return List.of();
    }

    @Override
    public List<ArtistResponse> getAllArtistResponsesDefault() {

        log.info("Service Artist: loading all artists");
        Page<Artist> artists = artistRepository.findAll(PageRequest.of(0, 10));
        if (!artists.getContent().isEmpty()) {
            return artists.getContent().stream()
                    .map(this::getArtistResponse)
                    .toList();
        }

        return List.of();
    }

    @Override
    public ArtistResponse getArtistResponseById(Long id) {
        Util.validateNumber(id);
        log.info("Fetching artist with ID: {}", id);
        if (id <= 0) {
            log.error("Invalid artist ID: {}", id);
            throw new InvalidDataException("Artist ID must be greater than 0");
        }
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Artist not found with ID: " + id));
        log.info("Artist found: {}", artist.getName());
        return getArtistResponse(artist);
    }

    @Override
    public Artist getArtistEntityById(Long id) {
        return null;
    }

    @Override
    public List<SearchArtistResponse> searchArtists(String query) {
        if (query == null || query.isBlank()) {
            log.error("Empty input keyword provided for artist search");
            return List.of();
        }
        log.info("Searching for artists with query: '{}'", query);

        Pageable pageable = Util.getPageable(0, 10);
        Page<Artist> artists = artistRepository.findArtistsByName(query, pageable);

        if (artists.hasContent()) {
            log.info("Artists found with query: '{}'", query);
            return artists.getContent().stream()
                    .map(artist -> {
                        log.info("Artist found: '{}'", artist.getName());
                        SearchArtistResponse response = new SearchArtistResponse();
                        response.setType("artist");
                        response.setArtist(getArtistResponse(artist));
                        return response;
                    })
                    .toList();
        }
        log.warn("No artists found for query: '{}'", query);
        return List.of();
    }

    private ArtistResponse getArtistResponse(Artist artist) {
        if (artist == null) {
            log.warn("Artist is null, cannot create ArtistResponse");
            return null;
        }
        log.info("Artist found with name: '{}'", artist.getName());
        ArtistResponse artistResponse = artistMapper.toArtistResponse(artist);
        artistResponse.setCountry(artist.getCountry().getName());
        return artistResponse;
    }
}
