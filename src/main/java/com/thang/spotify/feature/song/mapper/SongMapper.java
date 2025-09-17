package com.thang.spotify.feature.song.mapper;

import com.thang.spotify.feature.song.dto.response.SongResponse;
import com.thang.spotify.feature.song.entity.Song;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SongMapper {

    SongResponse toSongResponse(Song song);

}
