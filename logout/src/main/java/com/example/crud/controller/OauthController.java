//package com.example.crud.controller;
//
//import com.example.crud.dto.KakaoUserInfoDTO;
//import com.example.crud.entity.UserEntity;
//import com.example.crud.service.social.KakaoOauth;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//public class OauthController {
//
//    private final KakaoOauth kakaoOauth;
//
//    @GetMapping("/auth/kakao/callback")
//    public ResponseEntity<?> kakaoCallback(@RequestParam String code) {
//        // 1. 인가 코드로 Access Token 요청
//        String accessToken = kakaoOauth.requestAccessToken(code);
//
//        // 2. Access Token으로 사용자 정보 요청
//        KakaoUserInfoDTO kakaoUserInfo = kakaoOauth.requestUserInfo(accessToken);
//
//        // 3. 사용자 정보 DB 저장 또는 업데이트
//        UserEntity user = kakaoOauth.saveOrUpdateUser(kakaoUserInfo);
//
//        // 4. 필요하면 JWT 토큰 발급하거나 세션 처리 후 응답
//        return ResponseEntity.ok(user);
//    }
//}