package com.thang.spotify.common.util;

import com.thang.spotify.common.enums.Privacy;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.postgresql.util.PGobject;

@Converter(autoApply = true)
public class PrivacyEnumConverter implements AttributeConverter<Privacy, Object> {
    @Override
    public Object convertToDatabaseColumn(Privacy privacy) {
        if (privacy == null) return null;
        try {
            PGobject pgObject = new PGobject();
            pgObject.setType("e_privacy"); // tên type enum trong DB
            pgObject.setValue(privacy.name());
            return pgObject;
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting Privacy to PGobject", e);
        }
    }

    @Override
    public Privacy convertToEntityAttribute(Object dbData) {
        if (dbData == null) return null;
        return Privacy.valueOf(dbData.toString());
    }
}
