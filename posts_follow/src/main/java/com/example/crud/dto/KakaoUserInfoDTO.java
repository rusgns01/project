package com.example.crud.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class KakaoUserInfoDTO {
    private String id;
    private KakaoAccount kakao_account;

    @Getter
    @Setter
    public static class KakaoAccount {
        private Profile profile;
        private String email;

        @Getter
        @Setter
        public static class Profile {
            private String nickname;
            private String profile_image_url;
            private String thumbnail_image_url;
        }
    }

    public static KakaoUserInfoDTO from(Map<String, Object> attributes) {
        KakaoUserInfoDTO dto = new KakaoUserInfoDTO();

        dto.setId(String.valueOf(attributes.get("id")));

        Map<String, Object> accountMap = (Map<String, Object>) attributes.get("kakao_account");
        KakaoAccount account = new KakaoAccount();

        if (accountMap != null) {
            account.setEmail((String) accountMap.get("email"));

            Map<String, Object> profileMap = (Map<String, Object>) accountMap.get("profile");
            KakaoUserInfoDTO.KakaoAccount.Profile profile = new KakaoUserInfoDTO.KakaoAccount.Profile();

            if (profileMap != null) {
                profile.setNickname((String) profileMap.get("nickname"));
                profile.setProfile_image_url((String) profileMap.get("profile_image_url"));
            }

            account.setProfile(profile);
        }

        dto.setKakao_account(account);
        return dto;
    }
}
