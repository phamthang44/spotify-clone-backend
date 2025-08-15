package com.thang.spotify.dto;

import com.thang.spotify.dto.response.PageResponse;
import com.thang.spotify.dto.response.album.AlbumResponse;
import com.thang.spotify.dto.response.artist.ArtistResponse;
import com.thang.spotify.dto.response.genre.GenreResponse;
import com.thang.spotify.dto.response.song.SongResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatalogDTO {

    private List<AlbumResponse> albums;
    private List<ArtistResponse> artists;
    private List<GenreResponse> genres;

}
