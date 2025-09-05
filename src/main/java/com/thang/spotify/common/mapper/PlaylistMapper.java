package com.thang.spotify.common.mapper;

import com.thang.spotify.dto.response.playlist.PlaylistResponse;
import com.thang.spotify.entity.Playlist;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlaylistMapper {

    PlaylistResponse toPlaylistResponse(Playlist playlist);

}
