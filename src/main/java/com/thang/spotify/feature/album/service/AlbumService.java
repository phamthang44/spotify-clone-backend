package com.thang.spotify.feature.album.service;

import com.thang.spotify.feature.album.dto.response.AlbumResponse;
import com.thang.spotify.feature.album.dto.response.SearchAlbumResponse;
import com.thang.spotify.feature.album.entity.Album;

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
    AlbumResponse getAlbumResponseById(Long id);

//    List<Album> getAlbumsByGenre(Long genreId);
    List<AlbumResponse> getAlbumsByGenreResponse(Long genreId);
    List<SearchAlbumResponse> searchByName(String albumName);

}
