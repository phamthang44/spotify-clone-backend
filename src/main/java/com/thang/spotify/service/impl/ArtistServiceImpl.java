package com.thang.spotify.service.impl;

import com.thang.spotify.common.mapper.ArtistMapper;
import com.thang.spotify.dto.response.artist.ArtistResponse;
import com.thang.spotify.entity.Artist;
import com.thang.spotify.repository.ArtistRepository;
import com.thang.spotify.service.ArtistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
                    .map(artist -> {
                        log.info("Artist: {}", artist.getName());
                        ArtistResponse artistResponse = artistMapper.toArtistResponse(artist);
                        log.info("Artist response: {}", artistResponse.getName());
                        artistResponse.setCountry(artist.getCountry().getName());
                        return artistResponse;
                    })
                    .toList();
        }

        return List.of();
    }

    @Override
    public ArtistResponse getArtistById(Long id) {
        return null;
    }

    @Override
    public Artist getArtistEntityById(Long id) {
        return null;
    }
}
