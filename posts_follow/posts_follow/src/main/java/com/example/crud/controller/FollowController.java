package com.example.crud.controller;

import com.example.crud.dto.FollowDTO;
import com.example.crud.entity.UserEntity;
import com.example.crud.service.FollowService;
import com.example.crud.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;
    private final UserService userService;



    // 유저 정보 프로필
    @GetMapping("/friends/profile/{userName}")
    public String followPage(@PathVariable("userName") String userName, Model model) {
        UserEntity name = userService.findByName(userName);
        model.addAttribute("friend", name);

        return "friends_profile";
    }

    // 팔로우 성공, 취소
    @PostMapping("/friends/profile/follow/{userName}")
    public String viewProfile(@AuthenticationPrincipal OAuth2User oAuth2User,
                              @PathVariable("userName") String toUserName,
                              @RequestParam("action") String action,
                              @RequestParam("toUserId") String toUserId) {
//        UserEntity fromUser = userService.findByName(userDetails.getUsername());
        UserEntity myName = oAuth2User.getAttribute("name");
        UserEntity toUser = userService.findByName(toUserName);

        if ("follow".equals(action)) {
            followService.follow(myName, toUser);
        } else if ("unfollow".equals(action)) {
            followService.cancelFollow(myName, toUser);
        }

        return "redirect:/friends/profile/" + toUserName;
    }

    // 팔로잉 조회
    @GetMapping("/users/{userName}/following")
    public ResponseEntity<List<FollowDTO>> getFollowingList(@PathVariable("userName") String userName) {
        // findUser - 이거 이메일로 찾는거라 오류뜸
        UserEntity user = userService.findByName(userName);

        return ResponseEntity.ok().body(followService.followingList(user));
    }

    @GetMapping("/users/{userName}/follower")
    public ResponseEntity<List<FollowDTO>> getFollowerList(@PathVariable("userName") String userName) {
        UserEntity to_user = userService.findByName(userName);

        return ResponseEntity.ok().body(followService.followerList(to_user));
    }

//    @DeleteMapping("/users/follow/{friendName}")
//    public ResponseEntity<String> deleteFollow(@PathVariable String friendName) {
//        User fromUser = userService.findUser("user1");
//        User toUser = userService.findUser(friendName);
//
//        String result = followService.cancelFollow(fromUser, toUser);
//
//        return ResponseEntity.ok(result);
//    }
}
