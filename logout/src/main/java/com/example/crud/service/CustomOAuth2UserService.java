package com.example.crud.service;

import com.example.crud.entity.UserEntity;
import com.example.crud.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Autowired
    private final RestTemplate restTemplate;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // google, kakao, naver

        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();

        UserEntity user;

        switch (registrationId) {
//            case "google":
//                user = processGoogleUser(attributes);
//                break;
            case "kakao":
                user = processKakaoUser(attributes);
                break;
//            case "naver":
//                user = processNaverUser(attributes);
//                break;
            default:
                throw new OAuth2AuthenticationException("지원하지 않는 소셜 로그인입니다.");
        }

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                "id" // 또는 소셜 별 id 키
        );
    }

    private UserEntity processKakaoUser(Map<String, Object> attributes) {
        // 카카오 처리
        String kakaoId = String.valueOf(attributes.get("id"));

        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        String email = (String) kakaoAccount.get("email");

        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        String nickname = (String) profile.get("nickname");
        String profileImage = (String) profile.get("profile_image_url");

        // 사용자 조회
        Optional<UserEntity> userOptional = userRepository.findByKakaoId(kakaoId);

        UserEntity user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            // 기존 유저 정보 업데이트
            user.setEmail(email);
            user.setName(nickname);
            user.setProfileImage(profileImage);
        } else {
            // 신규 유저 생성
            user = new UserEntity();
            user.setKakaoId(kakaoId);
            user.setEmail(email);
            user.setName(nickname);
            user.setProfileImage(profileImage);
        }

        return userRepository.save(user);
    }

    public void unlinkKakaoAccount(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<?> request = new HttpEntity<>(headers);

        String unlinkUrl = "https://kapi.kakao.com/v1/user/unlink";

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(unlinkUrl, request, String.class);
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new RuntimeException("카카오 연결 끊기 실패: " + response.getBody());
            }
        } catch (Exception e) {
            throw new RuntimeException("카카오 unlink 요청 중 오류: " + e.getMessage(), e);
        }
    }
}
