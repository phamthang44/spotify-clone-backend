package com.thang.spotify.common.mapper;


import com.thang.spotify.dto.response.artist.ArtistResponse;
import com.thang.spotify.entity.Artist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface ArtistMapper {

    @Mapping(target = "country", ignore = true)
    ArtistResponse toArtistResponse(Artist artist);
}
