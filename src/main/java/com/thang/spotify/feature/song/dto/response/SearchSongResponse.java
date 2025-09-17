package com.thang.spotify.feature.song.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchSongResponse {

    private String type;
    private SongResponse song;

}
