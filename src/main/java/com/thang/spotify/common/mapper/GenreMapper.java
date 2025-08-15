package com.thang.spotify.common.mapper;

import com.thang.spotify.dto.response.genre.GenreResponse;
import com.thang.spotify.entity.Genre;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GenreMapper {
    GenreResponse toGenreResponse(Genre genre);
}
