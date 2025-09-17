package com.thang.spotify.feature.artist.dto.response;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistResponse implements Serializable {

    private Long id;
    private String name;
    private String bio;
    private String avatarUrl;
    private String country;
    private int debutYear;

}
