package com.example.crud.dto;

import com.example.crud.entity.PostEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDTO {
    private Long id;
    private String title;
    private String authorName;
    private String content;
    private String createdAt;
    private boolean isAuthor;

    public PostDTO(PostEntity post, boolean isAuthor){
        this.id = post.getId();
        this.title = post.getTitle();
        this.authorName = post.getAuthor().getName();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt().toString();
        this.isAuthor = isAuthor;
    }
}
