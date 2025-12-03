package com.back.domain.post.entity;

import com.back.domain.comment.entity.Comment;
import com.back.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;

@Entity
@NoArgsConstructor
@Getter
public class Post extends BaseEntity {
    private String title;
    private String content;

    // mappedBy: comment에서 저장된 변수명, fetch: 기본값 LAZY
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = {PERSIST, REMOVE})
    private List<Comment> comments = new ArrayList<>();

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void modify(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void addComment(String content) {
        Comment comment = new Comment(this, content);
        comments.add(comment);
    }
}
