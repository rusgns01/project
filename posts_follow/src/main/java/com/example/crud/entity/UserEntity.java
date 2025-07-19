package com.example.crud.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "userentity")
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kakao_Id", unique = true)
    private String kakaoId;

    private String email;
    
    @Column(name = "name")
    private String name;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "is_private")
    private Boolean isPrivate = false;

    @OneToMany(mappedBy = "fromUser", fetch = FetchType.LAZY)
    private List<FollowEntity> followings;

    @OneToMany(mappedBy = "toUser", fetch = FetchType.LAZY)
    private List<FollowEntity> followers;

    public UserEntity(String kakaoId, String email, String name, String profileImage){
        this.kakaoId = kakaoId;
        this.email = email;
        this.name = name;
        this.profileImage = profileImage;
    }
}
