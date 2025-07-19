package com.example.crud.controller;

import com.example.crud.entity.PostEntity;
import com.example.crud.entity.UserEntity;
import com.example.crud.repository.PostRepository;
import com.example.crud.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@AllArgsConstructor
public class WriteController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @GetMapping("/write")
    public String writeP(Model model, @AuthenticationPrincipal OAuth2User oAuth2User) {
        if (oAuth2User == null) {
            return "redirect:/login";
        }

        String kakaoId = oAuth2User.getAttribute("id").toString();
        model.addAttribute("authorId", kakaoId);

        Map<String, Object> properties = (Map<String, Object>) oAuth2User.getAttribute("properties");
        String nickname = (String) properties.get("nickname");
        model.addAttribute("author", nickname);
        return "write";
    }

    @PostMapping("/write")
    public String savePost(@RequestParam("title") String title,
                           @RequestParam("content") String content,
                           @AuthenticationPrincipal OAuth2User oAuth2User) {
        String kakaoId = oAuth2User.getAttribute("id").toString();
        UserEntity user = userRepository.findByKakaoId(kakaoId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
        
        Map<String, Object> properties = (Map<String, Object>) oAuth2User.getAttribute("properties");
        String nickname = (String) properties.get("nickname");
        
        PostEntity post = new PostEntity();
        post.setAuthor(user);
        post.setAuthorName(nickname);
        post.setTitle(title);
        post.setContent(content);

        postRepository.save(post);
        return "redirect:/posts";
    }
}
