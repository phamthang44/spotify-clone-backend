package com.thang.spotify.service;

import com.thang.spotify.dto.response.album.AlbumResponse;
import com.thang.spotify.entity.Album;

import java.util.List;

public interface AlbumService {

    // Define methods for album-related operations, such as:
    // - Creating an album
    // - Retrieving an album by ID
    // - Updating an album
    // - Deleting an album
    // - Searching for albums by various criteria
    // - Listing all albums, etc.

    // Example method signatures:
    //AlbumResponse createAlbum(AlbumRequest albumRequest);
    // AlbumResponse getAlbumById(Long id);
    // void updateAlbum(Long id, AlbumRequest albumRequest);
    // void deleteAlbum(Long id);
    // List<AlbumResponse> searchAlbums(String query);

    List<Album> getAllAlbums();
    List<AlbumResponse> getDefaultAlbums();
    AlbumResponse getAlbumById(Long id);
}
