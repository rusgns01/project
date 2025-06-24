package com.example.crud.controller;

import com.example.crud.dto.KakaoUserInfoDTO;
import com.example.crud.entity.UserEntity;
import com.example.crud.repository.UserRepository;
import com.example.crud.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

// UserController.java
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    private final UserRepository userRepository;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 안됨");
        }

        String kakaoId = String.valueOf(principal.getAttribute("id"));
        Optional<UserEntity> userOpt = userRepository.findByKakaoId(kakaoId);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("유저 없음");
        }

        return ResponseEntity.ok(userOpt.get());
    }

    @DeleteMapping("/me")
    public ResponseEntity<?> deleteUser(
            @AuthenticationPrincipal OAuth2User principal,
            OAuth2AuthenticationToken authentication) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 안됨");
        }

        // 카카오 ID 추출
        Object idObj = principal.getAttribute("id");
        String kakaoId = (idObj instanceof Number)
                ? String.valueOf(((Number) idObj).longValue())
                : (idObj != null ? idObj.toString() : null);

        if (kakaoId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("id 정보 없음");
        }

        // 유저 조회
        Optional<UserEntity> userOpt = userRepository.findByKakaoId(kakaoId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("유저 없음");
        }

        // 액세스 토큰 가져오기
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(),
                authentication.getName());

        if (client == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("카카오 클라이언트 없음");
        }

        String accessToken = client.getAccessToken().getTokenValue();

        // 카카오 연결 끊기
        customOAuth2UserService.unlinkKakaoAccount(accessToken);
        userRepository.delete(userOpt.get());

        return ResponseEntity.ok("유저 탈퇴 완료");
    }

}
