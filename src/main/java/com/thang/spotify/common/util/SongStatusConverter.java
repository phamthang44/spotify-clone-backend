package com.thang.spotify.common.util;

import com.thang.spotify.common.enums.Privacy;
import com.thang.spotify.common.enums.SongStatus;
import jakarta.persistence.AttributeConverter;

public class SongStatusConverter implements AttributeConverter<SongStatus, String> {
    @Override
    public String convertToDatabaseColumn(SongStatus status) {
        return status != null ? status.name() : null;
    }

    @Override
    public SongStatus convertToEntityAttribute(String dbData) {
        return dbData != null ? SongStatus.valueOf(dbData) : null;
    }
}
