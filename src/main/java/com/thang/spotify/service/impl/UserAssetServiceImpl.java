package com.thang.spotify.service.impl;

import com.thang.spotify.common.enums.UserAssetType;
import com.thang.spotify.common.util.Util;
import com.thang.spotify.entity.User;
import com.thang.spotify.entity.UserAsset;
import com.thang.spotify.repository.UserAssetRepository;
import com.thang.spotify.repository.UserRepository;
import com.thang.spotify.service.UserAssetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserAssetServiceImpl implements UserAssetService {

    private final UserAssetRepository userAssetRepository;
    private final UserRepository userRepository;


    @Override
    public void addNewAvatar(Long userId, String avatarUrl) {
        Util.validateNumber(userId);
        Util.isNullOrEmpty(avatarUrl);
        createOrUpdateUserAvatar(userId, avatarUrl);
    }

    @Override
    public void updateAvatar(Long userId, String avatarUrl) {
        createOrUpdateUserAvatar(userId, avatarUrl);
    }

    private void createOrUpdateUserAvatar(Long userId, String avatarUrl) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.error("User with ID {} not found", userId);
            return new UsernameNotFoundException("User not found");
        });

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
