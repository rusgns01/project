package com.example.crud.controller;

import com.example.crud.entity.PostEntity;
import com.example.crud.entity.UserEntity;
import com.example.crud.repository.PostRepository;
import com.example.crud.repository.UserRepository;
import com.example.crud.service.WriteService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class PageController {

    private final UserRepository userRepository;
    private final WriteService writeService;
    private final PostRepository postRepository;

    @GetMapping("/")
    public String homeP(){
        return "index";
    }

    @GetMapping("/main")
    public String mainP(){
        return "main";
    }

    @GetMapping("/friends")
    public String friendsPage(Model model, @AuthenticationPrincipal OAuth2User oAuth2User) {
        // 로그인한 내 id (UserEntity의 id)
        String kakaoId = oAuth2User.getAttribute("id").toString();
        UserEntity me = userRepository.findByKakaoId(kakaoId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        List<UserEntity> allUsers = userRepository.findAll();

        // 추천 친구 리스트에 isMe 플래그 추가
        List<Map<String, Object>> recommendedFriends = allUsers.stream().map(user -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", user.getId());
            map.put("name", user.getName());
            map.put("email", user.getEmail());
            map.put("isMe", user.getId().equals(me.getId()));
            return map;
        }).collect(Collectors.toList());

        //테스트 true false
        recommendedFriends.forEach(map -> System.out.println(map));

//        System.out.println("추천 친구 수: " + recommendedFriends.size());
//        recommendedFriends.forEach(map -> System.out.println(map));
//        System.out.println("내 정보: " + me);
//
//        System.out.println("DB에서 가져온 전체 유저 수: " + allUsers.size());
//        System.out.println("DB 전체 값" + allUsers);
//        allUsers.forEach(System.out::println);
//        allUsers.forEach(user -> System.out.println("id=" + user.getId() + ", name=" + user.getName() + ", email=" + user.getEmail() + ", kakaoId=" + user.getKakaoId()));

        model.addAttribute("recommendedFriends", recommendedFriends);
        return "friends";
    }
}
