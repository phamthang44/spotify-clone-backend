package com.thang.spotify.feature.song.validator;

import com.thang.spotify.common.exception.InvalidDataException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SongValidator {

    public void validateSongId(Long songId) {
        if (songId == null || songId <= 0) {
            log.error("Invalid song ID: {}", songId);
            throw new InvalidDataException("Song ID must be a positive number.");
        }
    }

}
