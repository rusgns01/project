package controller;

import helper.Constants.SocialLoginType;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.OauthService;

@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
@Slf4j
public class OauthController {
    private final OauthService oauthService;

    @GetMapping("/authorization/{socialLoginType}")
    public ResponseEntity<Void> socialLogin(@PathVariable SocialLoginType socialLoginType, HttpServletResponse response) {
        String redirectURL = oauthService.getOauthRedirectURL(socialLoginType);
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", redirectURL)
                .build();
    }

    @GetMapping("/authorization/{socialLoginType}/callback")
    public ResponseEntity<String> socialLoginCallback(@PathVariable SocialLoginType socialLoginType,
                                                      @RequestParam String code) {
        log.info(">> 사용자로부터 SNS 로그인 요청을 받음 :: {} Social Login", socialLoginType);
        String accessToken = oauthService.requestAccessToken(socialLoginType, code);
        log.info(">> {} 액세스 토큰 발급 완료: {}", socialLoginType, accessToken);
        return ResponseEntity.ok("Access Token: " + accessToken);
    }
}
