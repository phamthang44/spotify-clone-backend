package com.thang.spotify.common.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Privacy {
    @JsonProperty("public")
    PUBLIC,
    @JsonProperty("private")
    PRIVATE;
}
