package com.thang.spotify.feature.album.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchAlbumResponse {
    private String type;
    private AlbumResponse album;
}
