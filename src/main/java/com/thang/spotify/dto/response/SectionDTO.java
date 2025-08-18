package com.thang.spotify.dto.response;

import com.thang.spotify.dto.response.album.AlbumResponse;
import com.thang.spotify.dto.response.song.SongResponse;
import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectionDTO<T> {
    private String title;
    private List<T> items;
}
