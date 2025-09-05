package com.thang.spotify.service.impl;

import com.thang.spotify.common.mapper.PlaylistMapper;
import com.thang.spotify.common.util.Util;
import com.thang.spotify.dto.response.playlist.PlaylistResponse;
import com.thang.spotify.entity.Playlist;
import com.thang.spotify.entity.User;
import com.thang.spotify.exception.InvalidDataException;
import com.thang.spotify.repository.PlaylistRepository;
import com.thang.spotify.repository.UserRepository;
import com.thang.spotify.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class PlaylistServiceImpl implements PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final UserRepository userRepository;
    private final PlaylistMapper playlistMapper;


    @Override
    public PlaylistResponse getCurrentUserPlaylist(Long userId) {

        Util.validateNumber(userId);

        if (userId <= 0) {
            log.error("Invalid user ID: {}", userId);
            throw new InvalidDataException("User ID must be greater than 0");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));

        Playlist playlist = playlistRepository.findByUser(user);
        return null;
    }

    private PlaylistResponse mapToPlaylistResponse(Playlist playlist) {

        PlaylistResponse playlistResponse = playlistMapper.toPlaylistResponse(playlist);


        return playlistResponse;
    }


}
