package com.thang.spotify.feature.album.mapper;

import com.thang.spotify.feature.album.dto.response.AlbumResponse;
import com.thang.spotify.feature.album.entity.Album;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AlbumMapper {
    AlbumResponse toAlbumResponse(Album album);
}
