package com.example.follow.service;

import com.example.follow.FollowApplication;
import entity.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import repository.UserRepository;
import service.FollowService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
public class FollowServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowService followService;

    @Test
    void test() {
        // given
        User fromUser = userRepository.save(new User("userA", "emailA@example.com"));
        User toUser = userRepository.save(new User("userB", "emailB@example.com"));

        // when - follow
        String followMsg = followService.follow(fromUser, toUser);
        System.out.println(followMsg); // ✅ 출력
        assertThat(followMsg).contains("님을 팔로우하였습니다");

        // when - cancel follow
        String cancelMsg = followService.cancelFollow(fromUser, toUser);
        System.out.println(cancelMsg); // ✅ 출력
        assertThat(cancelMsg).contains("팔로우를 취소하였습니다");
    }
}
