package com.example.fcm_oauth.domain.repository;

import com.example.fcm_oauth.domain.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<Members, Long> {
}
