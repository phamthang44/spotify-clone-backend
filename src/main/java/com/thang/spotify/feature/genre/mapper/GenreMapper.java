package com.thang.spotify.feature.genre.mapper;

import com.thang.spotify.feature.genre.dto.response.GenreResponse;
import com.thang.spotify.feature.genre.entity.Genre;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GenreMapper {
    GenreResponse toGenreResponse(Genre genre);
}
