package com.thang.spotify.dto.response.song;

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
