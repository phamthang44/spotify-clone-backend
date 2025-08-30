package com.thang.spotify.repository;

import com.thang.spotify.common.enums.UserAssetType;
import com.thang.spotify.entity.UserAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAssetRepository extends JpaRepository<UserAsset, Long> {
    UserAsset findByUserIdAndType(Long user_id, UserAssetType type);
}
