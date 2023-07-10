package com.example.fcm_oauth.domain.repository;

import com.example.fcm_oauth.domain.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByOauthId(String oauthId);
}
