package com.thang.spotify.dto.response.album;

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
