package service;

import entity.Follow;
import entity.User;
import exception.Errorcode;
import exception.FollowException;
import exception.UserNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import repository.FollowRepository;
import repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final Follow follow;
    private final FollowRepository followRepository;

    public User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
    }

    public User findByName(String name) {
        return userRepository.findByName(name)
                .orElseThrow(() -> new UserNotFoundException("닉네임으로 사용자를 찾을 수 없습니다."));
    }
}
