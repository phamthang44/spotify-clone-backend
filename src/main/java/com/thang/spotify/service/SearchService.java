package com.thang.spotify.service;

import com.thang.spotify.dto.response.ResultsResponse;

public interface SearchService {

    ResultsResponse<?> search(String query);

}
