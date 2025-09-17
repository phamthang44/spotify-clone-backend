package com.thang.spotify.feature.catalog.entity;

import com.thang.spotify.feature.artist.entity.Artist;
import com.thang.spotify.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Table(name = "country")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Country extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String code;

    @OneToMany(mappedBy = "country", fetch = FetchType.LAZY)
    private List<Artist> artists = new ArrayList<>();

}
