package com.thang.spotify.feature.playlist.dto.response;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistResponse implements Serializable {

    private Long id;
    private String title;
    private String coverImageUrl;
    private String description;
    private String ownerName;
    private List<PlaylistSongResponse> songs;

}
