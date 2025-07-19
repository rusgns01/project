package com.example.crud.controller;

import com.example.crud.dto.PostDTO;
import com.example.crud.entity.PostEntity;
import com.example.crud.entity.UserEntity;
import com.example.crud.repository.PostRepository;
import com.example.crud.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class PostController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @GetMapping("/posts")
    public String posts(Model model) {
        List<PostEntity> postList = postRepository.findAll();
        List<Map<String, Object>> dtoList = postList.stream().map(post -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", post.getId());
            map.put("content", post.getContent());
            map.put("authorName", post.getAuthor().getName());

            return map;
        }).collect(Collectors.toList());
        model.addAttribute("posts", dtoList);

        return "posts";
    }

    // 상세 조회
//    @GetMapping("/posts/{postId}")
//    public ResponseEntity<PostDTO> getPost(@PathVariable("postId") Long postId,
//                                           @AuthenticationPrincipal OAuth2User oAuth2User) {
//        PostEntity post = postRepository.findById(postId)
//                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
//
//        // 현재 로그인한 사용자가 작성자인지 확인
//        boolean isAuthor = false;
//        if (oAuth2User != null) {
//            String currentUserKakaoId = oAuth2User.getAttribute("id").toString();
//            UserEntity currentUser = userRepository.findByKakaoId(currentUserKakaoId)
//                    .orElse(null);
//            if (currentUser != null) {
//                isAuthor = post.getAuthor().getId().equals(currentUser.getId());
//            }
//        }
//
//        PostDTO postDTO = new PostDTO(post, isAuthor); // isAuthor 정보도 포함
//        return ResponseEntity.ok(postDTO);
//    }

    @GetMapping("/posts/{postId}")
    public String viewPost(@PathVariable("postId") Long postId,
                           @AuthenticationPrincipal OAuth2User oAuth2User,
                           Model model) {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // 현재 로그인한 사용자가 작성자인지 확인
        boolean isAuthor = false;
        if (oAuth2User != null) {
            String currentUserKakaoId = oAuth2User.getAttribute("id").toString();
            UserEntity currentUser = userRepository.findByKakaoId(currentUserKakaoId)
                    .orElse(null);
            if (currentUser != null) {
                isAuthor = post.getAuthor().getId().equals(currentUser.getId());
            }
        }
        System.out.println("post title: " + post.getTitle());

        model.addAttribute("post", post);
        model.addAttribute("authorName", post.getAuthor().getName());
        model.addAttribute("isAuthorName", isAuthor);

        return "post_detail"; // post_detail.mustache 템플릿으로 이동
    }

    // 수정
    @PutMapping("/posts/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable("postId") Long postId,
                                        @RequestBody PostDTO postDTO,
                                        @AuthenticationPrincipal OAuth2User oAuth2User) {
        try {
            // 1. 게시글 존재 여부 확인
            PostEntity existingPost = postRepository.findById(postId)
                    .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

            // 2. 권한 체크 (작성자만 수정 가능)
            boolean isAuthor = false;
            if (oAuth2User != null) {
                String currentUserKakaoId = oAuth2User.getAttribute("id").toString();
                UserEntity currentUser = userRepository.findByKakaoId(currentUserKakaoId)
                        .orElse(null);
                if (currentUser != null) {
                    isAuthor = existingPost.getAuthor().getId().equals(currentUser.getId());
                }
            }

            if (!isAuthor) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("수정 권한이 없습니다.");
            }

            // 3. 게시글 내용 업데이트
            existingPost.setContent(postDTO.getContent());

            // 4. DB에 저장
            PostEntity updatedPost = postRepository.save(existingPost);

            // 5. 업데이트된 게시글을 DTO로 변환해서 반환 (isAuthor 포함)
            PostDTO responseDTO = new PostDTO(updatedPost, isAuthor);
            return ResponseEntity.ok(responseDTO);

        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500
        }
    }

    // 삭제
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable("postId") Long postId){
        try {
            // 게시글 존재 여부 확인
            PostEntity post = postRepository.findById(postId)
                    .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

            // 게시글 삭제
            postRepository.delete(post);

            // 3. 성공 응답 (삭제된 게시글 정보를 반환하거나, 단순 성공 메시지)
            return ResponseEntity.ok().body("게시글이 성공적으로 삭제되었습니다.");
            // return ResponseEntity.noContent().build(); // 204 No Content

        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500
        }
    }
}
