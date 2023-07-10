package com.example.fcm_oauth.domain.entity;

import com.example.fcm_oauth.dto.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usr_id")
    private Long id;

    @Column(name = "usr_oauth_id")
    private String oauthId;

    @Column(name = "usr_nickname")
    private String nickName;

    @Column(name = "usr_age")
    private Integer age;

    @Column(name = "usr_profile")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "usr_role")
    private Role role;
}
