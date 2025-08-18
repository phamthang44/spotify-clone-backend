package com.thang.spotify.service.validator;

import com.thang.spotify.common.util.Util;
import com.thang.spotify.exception.InvalidDataException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AlbumValidator {

    public void validateGenreId(Long genreId) {
        Util.validateNumber(genreId);
        if (genreId <= 0) {
            throw new InvalidDataException("Genre ID must be a positive number.");
        }
    }

}
