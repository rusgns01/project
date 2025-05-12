package repository;

import entity.Follow;
import entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    List<Follow> findByFromUser(User from_user);
    List<Follow> findByToUser(User to_user);

    // 내가 팔로우한 사람 모두와 관계 삭제
    void  deleteFollowByFromUser(User from_user);

    @Query("select f from Follow f where f.fromUser = :from and f.toUser = :to")
    Optional<Follow> findFollow(@Param("from") User from_user, @Param("to") User to_user);
    Optional<Follow> findByFromUserAndToUser(User fromUser, User toUser);
}