package com.example.crud.repository;

import com.example.crud.entity.FollowEntity;
import com.example.crud.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<FollowEntity, Long> {

    List<FollowEntity> findByFromUser(UserEntity from_user);
    List<FollowEntity> findByToUser(UserEntity to_user);
    List<FollowEntity> findAllByFromUser(UserEntity from_user);
    List<FollowEntity> findAllByToUser(UserEntity to_user);
    void  deleteFollowByFromUser(UserEntity from_user);
    @Query("select f from FollowEntity f where f.fromUser = :from and f.toUser = :to")
    Optional<FollowEntity> findFollow(@Param("from") UserEntity from_user, @Param("to") UserEntity to_user);
    Optional<FollowEntity> findByFromUserAndToUser(UserEntity fromUser, UserEntity toUser);
}