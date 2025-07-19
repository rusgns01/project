package com.example.crud.service;

import com.example.crud.dto.FollowDTO;
import com.example.crud.entity.FollowEntity;
import com.example.crud.entity.UserEntity;
import com.example.crud.exception.Errorcode;
import com.example.crud.exception.FollowException;
import com.example.crud.repository.FollowRepository;
import com.example.crud.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    // 자기자신 x
    public String follow(UserEntity from_user, UserEntity to_user) {
        if(from_user == to_user) {
            System.out.println("사용자: " + from_user);
            System.out.println("친구: " + to_user);
            throw new FollowException(Errorcode.SELF_FOLLOW, Errorcode.SELF_FOLLOW.getMessage());
        }

        // 중복 x
        if(followRepository.findFollow(from_user, to_user).isPresent())
            throw new FollowException(Errorcode.ALREADY_FOLLOW, Errorcode.ALREADY_FOLLOW.getMessage());

        FollowEntity follow = FollowEntity.builder()
                .toUser(to_user)
                .fromUser(from_user)
                .build();

        followRepository.save(follow);

        return "friends_profile";
    }

    public List<FollowDTO> followingList(UserEntity fromUser) {
        return fromUser.getFollowings().stream()
                .map(follow -> new FollowDTO(
                        follow.getToUser().getName(),
                        follow.getToUser().getEmail()
                ))
                .collect(Collectors.toList());
    }

    public List<FollowDTO> followerList(UserEntity toUser) {
        return toUser.getFollowers().stream()
                .map(follow -> new FollowDTO(
                        follow.getFromUser().getName(),
                        follow.getFromUser().getEmail()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public String cancelFollow(UserEntity from_user, UserEntity to_user){
        FollowEntity follow = followRepository.findByFromUserAndToUser(from_user, to_user)
                .orElseThrow(() -> new FollowException(Errorcode.USER_NOT_FOUND, Errorcode.USER_NOT_FOUND.getMessage()));
        followRepository.delete(follow);
        return to_user.getName() + " 님과의 팔로우를 취소하였습니다.";
        // 이거 왜 경로가 뜨냐?
    }
}
