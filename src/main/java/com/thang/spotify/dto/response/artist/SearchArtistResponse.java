package com.thang.spotify.dto.response.artist;

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
