package com.thang.spotify.service.impl;

import com.thang.spotify.common.mapper.SongMapper;
import com.thang.spotify.common.util.Util;
import com.thang.spotify.dto.response.PageResponse;
import com.thang.spotify.dto.response.song.SongResponse;
import com.thang.spotify.entity.Song;
import com.thang.spotify.repository.SongRepository;
import com.thang.spotify.service.SongService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {

    private final SongRepository songRepository;
    private final SongMapper songMapper;

    @Override
    public Page<Song> getSongsDefault(Pageable pageable) {

        Page<Song> songPage = songRepository.findAll(pageable);
        if (songPage.hasContent()) {
            return songPage;
        }

        return null;
    }

    @Override
    public List<SongResponse> getAllSongs() {

        List<Song> songs = songRepository.findAll();
        if (!songs.isEmpty()) {
            return songs.stream()
                    .map(songMapper::toSongResponse)
                    .toList();
        }

        return List.of();
    }

    @Override
    public List<SongResponse> getConvertedSongResponses(List<Song> songs) {
        return songs.stream()
                .map(songMapper::toSongResponse)
                .toList();
    }

    @Override
    public PageResponse<SongResponse> getSongResponsesByPage(int pageNo, int pageSize) {

        Util.validatePageNoPageSize(pageNo, pageSize);

        Pageable pageable = Util.getPageable(pageNo, pageSize);

        Page<Song> songPage = songRepository.findAll(pageable);

        return PageResponse.<SongResponse>builder()
                .pageNo(songPage.getNumber())
                .pageSize(songPage.getSize())
                .totalPages(songPage.getTotalPages())
                .totalElements((int) songPage.getTotalElements())
                .items(songPage.getContent().stream()
                        .map(songMapper::toSongResponse)
                        .toList())
                .build();
    }

    @Override
    public Page<Song> getSongsByArtistId(Long artistId, int pageNo, int pageSize) {
        return null;
    }

    @Override
    public Page<Song> getSongsByAlbumId(Long albumId, int pageNo, int pageSize) {
        return null;
    }

    @Override
    public Page<Song> getSongsByArtistId(Long artistId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Song> getSongsByGenreId(Long genreId, int pageNo, int pageSize) {
        return null;
    }

    @Override
    public Song getSongById(Long id) {
        return null;
    }

    @Override
    public SongResponse getSongResponseById(Long id) {
        return null;
    }

    @Override
    public Page<Song> searchSongs(Specification<Song> spec, Pageable pageable) {
        return null;
    }

    @Override
    public List<SongResponse> getSongsDefault() {

        List<Song> songs = songRepository.getSongs(PageRequest.of(0, 50));

        if (!songs.isEmpty()) {
                return songs.stream()
                        .map(songMapper::toSongResponse)
                        .toList();
        }

        return List.of();
    }
}
