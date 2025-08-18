package com.thang.spotify.service.impl;

import com.thang.spotify.common.mapper.SongMapper;
import com.thang.spotify.common.util.Util;
import com.thang.spotify.dto.response.PageResponse;
import com.thang.spotify.dto.response.song.SongResponse;
import com.thang.spotify.entity.Song;
import com.thang.spotify.entity.SongGenre;
import com.thang.spotify.exception.InvalidDataException;
import com.thang.spotify.exception.ResourceNotFoundException;
import com.thang.spotify.repository.SongRepository;
import com.thang.spotify.service.GenreService;
import com.thang.spotify.service.SongGenreService;
import com.thang.spotify.service.SongService;
import com.thang.spotify.service.validator.SongValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    public PageResponse<SongResponse> getSongsByGenre(SongGenre songGenre) {
        if (songGenre == null) {
            throw new InvalidDataException("Song genre cannot be null");
        }

        Pageable pageable = Util.getPageable(0, 50); // Default page size of 50

        Long genreId = songGenre.getGenre().getId();

        if (genreId == null || genreId <= 0) {
            throw new InvalidDataException("Genre ID cannot be null or less than or equal to zero");
        }


        throw new ResourceNotFoundException("No songs found for genre: " + songGenre.getGenre().getName());
    }

    @Override
    public List<SongResponse> getSongsByGenreId(Long genreId) {
        Pageable pageable = Util.getPageable(0, 50); // Default page size of 50

        boolean isValid = isValidGenreId(genreId);

        if (!isValid) {
            throw new InvalidDataException("Invalid genre ID: " + genreId);
        }
        return toSongPageResponse(genreId, pageable);
    }

    @Override
    public Song getSongById(Long id) {
        return null;
    }

    @Override
    public SongResponse getSongResponseById(Long id) {
        songValidator.validateSongId(id);
        log.info("Song service : Fetching song with ID: {}", id);
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

    private boolean isValidGenreId(Long genreId) {
        return genreId != null && genreId > 0;
    }

    private List<SongResponse> toSongPageResponse(Long genreId, Pageable pageable) {
        Page<Song> songPage = songRepository.findByGenreId(genreId, pageable);
        if (songPage.hasContent()) {
            List<Song> songs = songPage.getContent();
            return songs.stream()
                    .map(this::getSongResponse)
                    .toList();
        }
        throw new ResourceNotFoundException("No songs found for genre: " + genreService.getGenreById(genreId).getName());
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

        return List.of();
    }
}
