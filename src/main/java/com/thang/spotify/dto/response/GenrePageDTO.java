package com.thang.spotify.dto.response;

import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenrePageDTO {
    private Long genreId;
    private String genreName;
    private List<SectionDTO> sections;
}
