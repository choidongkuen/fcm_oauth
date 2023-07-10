package com.example.fcm_oauth.domain.repository;

import com.example.fcm_oauth.domain.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<Users, Long> {
}
