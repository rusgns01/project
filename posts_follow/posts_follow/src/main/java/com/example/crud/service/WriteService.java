package com.example.crud.service;

import com.example.crud.entity.UserEntity;
import com.example.crud.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WriteService {
    private final UserRepository userRepository;

    public String getRealNameByUsername(String username) {
        return userRepository.findByName(username)
                .map(UserEntity::getName)
                .orElse("이름없음");
    }
}