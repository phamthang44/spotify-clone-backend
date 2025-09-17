package com.thang.spotify.feature.artist.service;

import com.thang.spotify.feature.artist.dto.response.ArtistResponse;
import com.thang.spotify.feature.artist.dto.response.SearchArtistResponse;
import com.thang.spotify.feature.artist.entity.Artist;

import java.util.List;

public interface ArtistService {
    List<Artist> getAllArtists();
    List<ArtistResponse> getAllArtistResponsesDefault();
    ArtistResponse getArtistResponseById(Long id);
    Artist getArtistEntityById(Long id);
    List<SearchArtistResponse> searchArtists(String query);
}
