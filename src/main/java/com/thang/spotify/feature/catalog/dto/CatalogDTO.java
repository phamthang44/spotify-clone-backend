package com.thang.spotify.feature.catalog.dto;

import com.thang.spotify.feature.album.dto.response.AlbumResponse;
import com.thang.spotify.feature.artist.dto.response.ArtistResponse;
import com.thang.spotify.feature.genre.dto.response.GenreResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatalogDTO {

    private List<AlbumResponse> albums;
    private List<ArtistResponse> artists;
    private List<GenreResponse> genres;

}
