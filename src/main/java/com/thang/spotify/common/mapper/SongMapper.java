package com.thang.spotify.common.mapper;

import com.thang.spotify.dto.response.song.SongResponse;
import com.thang.spotify.entity.Song;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SongMapper {

    SongResponse toSongResponse(Song song);

}
