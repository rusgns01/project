package service;

import dto.FollowDTO;
import entity.Follow;
import exception.Errorcode;
import exception.FollowException;
import entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import repository.FollowRepository;
import repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    // 자기자신 x
    public String follow(User from_user, User to_user) {
        if(from_user == to_user) {
            throw new FollowException(Errorcode.SELF_FOLLOW, Errorcode.SELF_FOLLOW.getMessage());
        }

        // 중복 x
        if(followRepository.findFollow(from_user, to_user).isPresent())
            throw new FollowException(Errorcode.ALREADY_FOLLOW, Errorcode.ALREADY_FOLLOW.getMessage());

        Follow follow = Follow.builder()
                .toUser(to_user)
                .fromUser(from_user)
                .build();

        followRepository.save(follow);

        return "Success";
    }

    public List<FollowDTO> followingList(User selectedUser, User requestUser) {
        List<Follow> list = followRepository.findByFromUser(selectedUser);
        List<FollowDTO> followList = new ArrayList<>();
        for (Follow f : list) {
            followList.add(
                    f.getToUser().toFollow(findStatus(f.getToUser(), requestUser))
            );
        }
        return followList;
    }

    public List<FollowDTO> followerList(User selectedUser, User requestUser) {
        List<Follow> list = followRepository.findByToUser(selectedUser);
        List<FollowDTO> followerList = new ArrayList<>();
        for (Follow f : list) {
            followerList.add(
                    f.getFromUser().toFollow(findStatus(f.getFromUser(), requestUser))
            );
        }
        return followerList;
    }

    @Transactional
    public String cancelFollow(User from_user, User to_user){
        Follow follow = followRepository.findByFromUserAndToUser(from_user, to_user)
                .orElseThrow(() -> new FollowException(Errorcode.USER_NOT_FOUND, Errorcode.USER_NOT_FOUND.getMessage()));
        followRepository.delete(follow);
        return to_user.getName() + "님과의 팔로우를 취소하였습니다.";
    }

    //A와 B의 follow관계 찾기
    protected String findStatus(User selectedUser, User requestUser) {
        if (selectedUser.getName().equals(requestUser.getName()))
            return "self";
        if (followRepository.findFollow(requestUser, selectedUser).isEmpty())
            return "none";

        return "following";
    }
}
