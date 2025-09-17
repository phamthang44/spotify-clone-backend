package com.thang.spotify.feature.song.service.impl;

import com.thang.spotify.feature.song.mapper.SongMapper;
import com.thang.spotify.common.util.Util;
import com.thang.spotify.common.dto.PageResponse;
import com.thang.spotify.feature.song.dto.response.SearchSongResponse;
import com.thang.spotify.feature.song.dto.response.SongResponse;
import com.thang.spotify.feature.song.entity.Song;
import com.thang.spotify.feature.song.entity.SongGenre;
import com.thang.spotify.common.exception.InvalidDataException;
import com.thang.spotify.common.exception.ResourceNotFoundException;
import com.thang.spotify.feature.song.repository.SongRepository;
import com.thang.spotify.feature.genre.service.GenreService;
import com.thang.spotify.feature.song.service.SongService;
import com.thang.spotify.feature.song.validator.SongValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {

    private final SongRepository songRepository;
    private final SongMapper songMapper;
    private final GenreService genreService;
    private final SongValidator songValidator;

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
    public List<SongResponse> getSongsByGenreId(Long genreId) {
        Pageable pageable = Util.getPageable(0, 50); // Default page size of 50

        boolean isValid = isValidGenreId(genreId);

        if (!isValid) {
            throw new InvalidDataException("Invalid genre ID: " + genreId);
        }

        Page<Song> songPage = songRepository.findByGenreId(genreId, pageable);
        return toSongPageResponse(songPage);
    }

    @Override
    public Song getSongById(Long id) {
        songValidator.validateSongId(id);
        log.info("Song service : Fetching song entity with ID: {}", id);
        return songRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found with ID: " + id));
    }

    @Override
    public SongResponse getSongResponseById(Long id) {
        songValidator.validateSongId(id);
        log.info("Song service : Fetching song response with ID: {}", id);
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found with ID: " + id));
        return getSongResponse(song);
    }

    private SongResponse getSongResponse(Song song) {
        SongResponse songResponse = songMapper.toSongResponse(song);
        songResponse.setAlbumName(song.getAlbum().getTitle());
        songResponse.setArtistName(song.getArtist().getName());
        Set<SongGenre> songGenres = song.getSongGenres();
        boolean isValidSongGenresList = songGenres != null && !songGenres.isEmpty();
        if (isValidSongGenresList) {
            List<String> genreNames = convertGenresToStringList(songGenres);
            songResponse.setGenres(genreNames);
            if (songGenres.size() == 1 && song.getGenre() != null && song.getGenre().getName() != null) {
                songResponse.setGenres(List.of(song.getGenre().getName()));
            }
        } else {
            // only 1 genre
            songResponse.setGenres(List.of(song.getGenre().getName()));
        }

        return songResponse;
    }

    @Override
    public List<SearchSongResponse> searchSongs(String keyword) {

        Pageable pageable = Util.getPageable(0, 10);
        log.info("Song service : Searching songs with keyword: {}", keyword);
        Page<Song> songs = songRepository.findByNameContainingIgnoreCase(keyword, pageable);
        if (!songs.getContent().isEmpty()) {
            return songs.getContent().stream()
                    .map(song -> {
                        SearchSongResponse songResponse = new SearchSongResponse();
                        songResponse.setType("song");
                        songResponse.setSong(getSongResponse(song));
                        return songResponse;
                    })
                    .toList();
        }
        return List.of();
    }

    private boolean isValidGenreId(Long genreId) {
        return genreId != null && genreId > 0;
    }

    private List<SongResponse> toSongPageResponse(Page<Song> songPage) {
        if (songPage.hasContent()) {
            List<Song> songs = songPage.getContent();
            return songs.stream()
                    .map(this::getSongResponse)
                    .toList();
        }
        throw new ResourceNotFoundException("There are no songs available");
    }

    private List<String> convertGenresToStringList(Set<SongGenre> songGenres) {
        List<String> genreNames = new ArrayList<>();
        for (SongGenre songGenre : songGenres) {
            if (songGenre.getGenre() != null && songGenre.getGenre().getName() != null) {
                genreNames.add(songGenre.getGenre().getName());
            }
        }
        return genreNames;
    }

    @Override
    public List<Song> getSongsEntityByGenreId(Long genreId) {
        //todo here:
        if (isValidGenreId(genreId)) {
            Pageable pageable = Util.getPageable(0, 50); // Default page size of 50
            Page<Song> songPage = songRepository.findByGenreId(genreId, pageable);
            if (songPage.hasContent()) {
                return songPage.getContent();
            }
        } else {
            throw new InvalidDataException("Invalid genre ID: " + genreId);
        }

        return List.of();
    }

    @Override
    public List<SongResponse> getLatestSongsByGenreOrderByReleaseDate(Long genreId) {
        log.info("Song service : Fetching songs response by release date");
        Util.validateNumber(genreId);
        if (!isValidGenreId(genreId)) {
            throw new InvalidDataException("Invalid genre ID: " + genreId);
        }
        Pageable pageable = Util.getPageable(0, 50);
        Page<Song> songs = songRepository.findAllByGenreIdOrderByReleaseDateDesc(genreId, pageable);
        if (songs.isEmpty()) {
            log.warn("Song service : No songs response found by release date");
            throw new ResourceNotFoundException("No songs found by release date");
        }
        return toSongPageResponse(songs);
    }

    @Override
    public List<SongResponse> getSongsByGenreIdAndOrderByLikeCount(Long genreId) {
        log.info("Song service : Fetching songs by genre ID: {} and ordering by like count", genreId);
        Pageable pageable = Util.getPageable(0, 50);

        Page<Song> song = songRepository.findAllByGenreIdOrderByLikeCountDesc(genreId, pageable);

        if (!song.getContent().isEmpty()) {
            log.info("Song service : Found {} songs by genre ID: {}", song.getContent().size(), genreId);
            return toSongPageResponse(song);
        }
        throw new ResourceNotFoundException("No songs found by genre ID: " + genreId);
    }
}
