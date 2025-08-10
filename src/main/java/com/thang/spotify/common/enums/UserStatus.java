package com.thang.spotify.common.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum UserStatus {
    @JsonProperty("active")
    ACTIVE,
    @JsonProperty("inactive")
    INACTIVE,
    @JsonProperty("banned")
    BANNED,
    @JsonProperty("none")
    NONE
}
