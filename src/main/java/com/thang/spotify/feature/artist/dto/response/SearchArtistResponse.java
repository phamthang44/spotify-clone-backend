package com.thang.spotify.feature.artist.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchArtistResponse {
    private String type;
    private ArtistResponse artist;
}
