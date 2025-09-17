package com.thang.spotify.feature.album.dto.response;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlbumResponse implements Serializable {

    private Long id;
    private String title;
    private String artistName;
    private String coverImageUrl;
    private LocalDateTime releaseDate;
    private int totalTracks;
    private String description;

}
