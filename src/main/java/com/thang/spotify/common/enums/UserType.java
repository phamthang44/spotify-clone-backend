package com.thang.spotify.common.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum UserType {
    @JsonProperty("admin")
    ADMIN,
    @JsonProperty("user")
    USER;

    public static UserType fromString(String type) {
        for (UserType userType : UserType.values()) {
            if (userType.name().equalsIgnoreCase(type)) {
                return userType;
            }
        }
        throw new IllegalArgumentException("Unknown user type: " + type);
    }

    public String getName() {
        return this.name();
    }
}
