package com.thang.spotify.service.impl;

import com.thang.spotify.common.enums.Privacy;
import com.thang.spotify.common.mapper.PlaylistMapper;
import com.thang.spotify.common.util.Util;
import com.thang.spotify.dto.response.playlist.PlaylistResponse;
import com.thang.spotify.entity.Playlist;
import com.thang.spotify.entity.User;
import com.thang.spotify.exception.InvalidDataException;
import com.thang.spotify.repository.PlaylistRepository;
import com.thang.spotify.repository.UserRepository;
import com.thang.spotify.service.PlaylistService;
import com.thang.spotify.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class PlaylistServiceImpl implements PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final PlaylistMapper playlistMapper;
    private final UserService userService;


    @Override
    public PlaylistResponse getCurrentUserPlaylist(Long userId) {

        Util.validateNumber(userId);

        if (userId <= 0) {
            log.error("Invalid user ID: {}", userId);
            throw new InvalidDataException("User ID must be greater than 0");
        }

        User user = userService.getUserById(userId);

        Playlist playlist = playlistRepository.findByUser(user);
        return null;
    }

    private PlaylistResponse mapToPlaylistResponse(Playlist playlist) {
        PlaylistResponse playlistResponse = playlistMapper.toPlaylistResponse(playlist);
        playlistResponse.setOwnerName(playlist.getUser().getDisplayName());
        return playlistResponse;
    }

    @Override
    @Transactional
    public PlaylistResponse createPlaylistForUser(Long userId) {
        Util.validateNumber(userId);
        User user = userService.getUserById(userId);

        Playlist newPlaylist = Playlist.builder()
                .user(user)
                .playlistSongs(null)
                .coverImageUrl(null)
                .description(null)
                .title("New Playlist")
                .privacy(Privacy.PUBLIC)
                .build();
        PlaylistResponse playlistResponse = mapToPlaylistResponse(playlistRepository.save(newPlaylist));
        playlistResponse.setOwnerName(user.getDisplayName());
        return playlistResponse;
    }

    @Override
    public List<PlaylistResponse> getAllPlaylistsByUser(Long userId) {
        Util.validateNumber(userId);
        User user = userService.getUserById(userId);

        List<Playlist> playlists = playlistRepository.findAllByUserId(userId);

        if (playlists != null && !playlists.isEmpty()) {
            return playlists.stream()
                    .map(this::mapToPlaylistResponse)
                    .toList();
        }

        return List.of();
    }
}
