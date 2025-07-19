package com.example.crud.service;

import com.example.crud.entity.UserEntity;
import com.example.crud.exception.UserNotFoundException;
import com.example.crud.repository.FollowRepository;
import com.example.crud.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    public UserEntity findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("이메일로 사용자를 찾을 수 없습니다."));
    }

    public UserEntity findByName(String name) {
        return userRepository.findByName(name)
                .orElseThrow(() -> new UserNotFoundException("닉네임으로 사용자를 찾을 수 없습니다."));
    }
}
