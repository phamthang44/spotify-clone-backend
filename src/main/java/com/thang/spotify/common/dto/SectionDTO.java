package com.thang.spotify.common.dto;

import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectionDTO<T> {
    private String title;
    private List<T> items;
}
