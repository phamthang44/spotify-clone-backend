package com.thang.spotify.feature.search.service;

import com.thang.spotify.feature.search.dto.ResultsResponse;

public interface SearchService {

    ResultsResponse<?> search(String query);

}
