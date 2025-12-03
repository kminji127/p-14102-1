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
import java.util.Optional;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;

@Entity
@NoArgsConstructor
@Getter
public class Post extends BaseEntity {
    private String title;
    private String content;

    // mappedBy: comment에서 저장된 변수명, fetch: 기본값 LAZY
    // orphanRemoval: 고아 객체 제거 (List에 제거하면 리모콘(주소)만 제거되고 객체는 남아 있으므로)
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = {PERSIST, REMOVE}, orphanRemoval = true)
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

    public Optional<Comment> findCommentById(int commentId) {
        return comments
                .stream()
                .filter(comment -> comment.getId() == commentId)
                .findFirst();
    }

    public boolean deleteComment(int commentId) {
        Comment comment = findCommentById(commentId)
                .orElse(null);
        if (comment == null) return false;
        // 삭제되면 true, 삭제되지 않거나 아예 없는 댓글이면 false
        return comments.remove(comment);
    }
}
