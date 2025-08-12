package com.thang.spotify.common.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SongStatus {
    @JsonProperty("published")
    PUBLISHED,
    @JsonProperty("draft")
    DRAFT,
    @JsonProperty("removed")
    REMOVED
}
