package com.thang.spotify.dto.response.playlist;

import com.thang.spotify.dto.response.song.SongResponse;
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
