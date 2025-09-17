package com.thang.spotify.feature.song.dto.response;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SongResponse implements Serializable {

    private Long id;
    private String title;
    private String artistName;
    private String albumName;
    private List<String> genres;
    private LocalDateTime releaseDate;
    private String coverImageUrl;
    private int duration; // in seconds
    private String audioUrl;
    private String status; // e.g., PUBLISHED, UNPUBLISHED
    private int playCount; // Number of times the song has been played
    private int likeCount; // Number of likes the song has received

}
