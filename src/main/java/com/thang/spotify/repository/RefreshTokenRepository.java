package com.thang.spotify.repository;

import com.thang.spotify.entity.RefreshToken;
import com.thang.spotify.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);

    List<RefreshToken> findAllByUser(User user);

    // lấy token hiện tại của user (nếu chỉ giữ duy nhất 1)
    Optional<RefreshToken> findByUser(User user);

    // tìm token chưa bị revoke và còn hạn
    Optional<RefreshToken> findByTokenAndRevokedFalseAndExpiryDateAfter(String token, LocalDateTime now);
    List<RefreshToken> findAllByRevokedFalseAndExpiryDateAfter(LocalDateTime now);


    @Modifying
    @Query("UPDATE RefreshToken r SET r.revoked = true WHERE r.user = :user AND r.revoked = false")
    void revokeAllByUser(@Param("user") User user);
}
