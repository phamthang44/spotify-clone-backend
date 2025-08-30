package com.thang.spotify.service.impl;

import com.thang.spotify.common.enums.UserAssetType;
import com.thang.spotify.common.util.Util;
import com.thang.spotify.entity.User;
import com.thang.spotify.entity.UserAsset;
import com.thang.spotify.repository.UserAssetRepository;
import com.thang.spotify.service.UserAssetService;
import com.thang.spotify.service.UserService;
import com.thang.spotify.service.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserAssetServiceImpl implements UserAssetService {

    private final UserAssetRepository userAssetRepository;
    private final UserService userService;


    @Override
    public void addNewAvatar(Long userId, String avatarUrl) {
        Util.validateNumber(userId);
        Util.isNullOrEmpty(avatarUrl);

        User user = userService.getUserById(userId);

        UserAsset userAsset = new UserAsset();
        userAsset.setUser(user);
        userAsset.setUrl(avatarUrl);
        userAsset.setType(UserAssetType.AVATAR);
        userAssetRepository.save(userAsset);
    }

    @Override
    public void updateAvatar(Long userId, String avatarUrl) {
        User user = userService.getUserById(userId);

        UserAsset userAsset = new UserAsset();
        userAsset.setUser(user);
        userAsset.setUrl(avatarUrl);
        userAsset.setType(UserAssetType.AVATAR);
        userAssetRepository.save(userAsset);
    }

    @Override
    public String getAvatarUrl(Long userId, UserAssetType type) {
        UserAsset userAsset = userAssetRepository.findByUserIdAndType(userId, type);
        if (userAsset != null) {
            return userAsset.getUrl();
        }
        return "";
    }
}
