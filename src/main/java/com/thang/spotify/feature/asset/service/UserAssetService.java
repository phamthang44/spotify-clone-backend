package com.thang.spotify.feature.asset.service;

import com.thang.spotify.common.enums.UserAssetType;

public interface UserAssetService {
    void addNewAvatar(Long userId, String avatarUrl);
    void updateAvatar(Long userId, String avatarUrl);
    String getAvatarUrl(Long userId, UserAssetType type);
}
