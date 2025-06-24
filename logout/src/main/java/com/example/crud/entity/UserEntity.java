package com.example.crud.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String kakaoId;

    private String email;
    private String name;

    @Column(name = "profile_image")
    private String profileImage;

    public UserEntity(String kakaoId, String email, String name, String profileImage){
        this.kakaoId = kakaoId;
        this.email = email;
        this.name = name;
        this.profileImage = profileImage;
    }
}
