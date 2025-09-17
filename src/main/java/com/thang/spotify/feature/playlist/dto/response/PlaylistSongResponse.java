package com.thang.spotify.feature.playlist.dto.response;

import com.thang.spotify.feature.song.dto.response.SongResponse;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistSongResponse implements Serializable {

    private SongResponse song;
    private int trackNumber;
    private LocalDateTime addedAt;

}
