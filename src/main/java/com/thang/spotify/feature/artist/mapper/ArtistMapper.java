package com.thang.spotify.feature.artist.mapper;


import com.thang.spotify.feature.artist.dto.response.ArtistResponse;
import com.thang.spotify.feature.artist.entity.Artist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface ArtistMapper {

    @Mapping(target = "country", ignore = true)
    ArtistResponse toArtistResponse(Artist artist);
}
