package com.thang.spotify.feature.playlist.mapper;

import com.thang.spotify.feature.playlist.dto.response.PlaylistResponse;
import com.thang.spotify.feature.playlist.entity.Playlist;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlaylistMapper {

    PlaylistResponse toPlaylistResponse(Playlist playlist);

}
