package com.thang.spotify.service.impl;

import com.thang.spotify.common.mapper.AlbumMapper;
import com.thang.spotify.dto.response.album.AlbumResponse;
import com.thang.spotify.entity.Album;
import com.thang.spotify.repository.AlbumRepository;
import com.thang.spotify.service.AlbumService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlbumServiceImpl implements AlbumService {

    private final AlbumRepository albumRepository;
    private final AlbumMapper albumMapper;

    @Override
    public List<Album> getAllAlbums() {
        return List.of();
    }

    @Override
    public List<AlbumResponse> getDefaultAlbums() {
        log.info("Album service : loading default albums");
        Page<Album> albumPage = albumRepository.findAll(PageRequest.of(0, 10));
        if (albumPage.hasContent()) {
            return albumPage.getContent().stream()
                    .map(album -> {
                        log.info("Album: {}", album.getTitle());
                        AlbumResponse albumResponse = albumMapper.toAlbumResponse(album);
                        log.info("Album response: {}", albumResponse.getTitle());
                        albumResponse.setArtistName(album.getArtist().getName());
                        return albumResponse;
                    })
                    .toList();
        }
        return List.of();
    }

    @Override
    public AlbumResponse getAlbumById(Long id) {
        return null;
    }
}
