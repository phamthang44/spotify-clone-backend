package com.thang.spotify.common.util;

import com.thang.spotify.common.enums.Privacy;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


@Converter(autoApply = true)
public class PrivacyEnumConverter implements AttributeConverter<Privacy, String> {
    @Override
    public String convertToDatabaseColumn(Privacy privacy) {
        return privacy != null ? privacy.name() : null;
    }

    @Override
    public Privacy convertToEntityAttribute(String dbData) {
        return dbData != null ? Privacy.valueOf(dbData) : null;
    }
}
