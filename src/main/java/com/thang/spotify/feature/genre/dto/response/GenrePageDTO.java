package com.thang.spotify.feature.genre.dto.response;

import com.thang.spotify.common.dto.SectionDTO;
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
    private List<SectionDTO<?>> sections;
}
