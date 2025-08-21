package com.thang.spotify.service;

import com.thang.spotify.dto.response.artist.ArtistResponse;
import com.thang.spotify.dto.response.artist.SearchArtistResponse;
import com.thang.spotify.entity.Artist;

import java.util.List;

public interface ArtistService {
    List<Artist> getAllArtists();
    List<ArtistResponse> getAllArtistResponsesDefault();
    ArtistResponse getArtistResponseById(Long id);
    Artist getArtistEntityById(Long id);
    List<SearchArtistResponse> searchArtists(String query);
}
