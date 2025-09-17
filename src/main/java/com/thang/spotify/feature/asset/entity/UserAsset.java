package com.thang.spotify.feature.asset.entity;

import com.thang.spotify.common.enums.UserAssetType;
import com.thang.spotify.common.entity.BaseEntity;
import com.thang.spotify.feature.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_asset")
public class UserAsset extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "asset_type")
    @Enumerated(EnumType.STRING)
    private UserAssetType type;

    @Column(name = "url", nullable = false)
    private String url;

}
