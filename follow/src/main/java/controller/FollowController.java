package controller;

import dto.FollowDTO;
import entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import service.FollowService;
import service.UserService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class FollowController {
    private final UserService userService;
    private final FollowService followService;


    // 친구 맺기
    @PostMapping("/users/follow/{freindName}")
    public ResponseEntity follow(Authentication authentication, @PathVariable("friendName") String friendName) {
        User from_user = userService.findUser(authentication.getName());
        User to_user = userService.findUser(friendName);

        followService.follow(from_user, to_user);

        return ResponseEntity.ok().build();
    }

    // 팔로잉 조회
    @GetMapping("/users/{userName}/following")
    public ResponseEntity<List<FollowDTO>> getFollowingList(@PathVariable("userName") String userName, Authentication authentication) {
        User from_user = userService.findUser(authentication.getName());
        User requestUser = userService.findUser(authentication.getName());

        return ResponseEntity.ok().body(followService.followingList(from_user, requestUser));
    }

    @GetMapping("/users/{userName}/follower")
    public ResponseEntity<List<FollowDTO>> getFollowerList(@PathVariable("userName") String userName, Authentication authentication) {
        User to_user = userService.findUser(authentication.getName());
        User requestUser = userService.findUser(authentication.getName());

        return ResponseEntity.ok().body(followService.followerList(to_user, requestUser));
    }

    @DeleteMapping("/users/follow/{friendName}")
    public ResponseEntity<String> deleteFollow(Authentication authentication, @PathVariable String friendName) {
        User fromUser = userService.findUser(authentication.getName());
        User toUser = userService.findUser(friendName);

        String result = followService.cancelFollow(fromUser, toUser);

        return ResponseEntity.ok(result);
    }
}