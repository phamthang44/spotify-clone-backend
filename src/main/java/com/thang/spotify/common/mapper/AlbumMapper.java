package com.thang.spotify.common.mapper;

import com.thang.spotify.dto.response.album.AlbumResponse;
import com.thang.spotify.entity.Album;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AlbumMapper {
    AlbumResponse toAlbumResponse(Album album);
}
