package com.thang.spotify.feature.search.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResultsResponse<T> implements Serializable {

    private int total;
    private List<T> results;

}
