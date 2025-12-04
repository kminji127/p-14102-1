package com.back.domain.comment.entity;

import com.back.domain.post.entity.Post;
import com.back.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Comment extends BaseEntity {
    @ManyToOne
    private Post post;

    private String content;

    public void modify(String content) {
        this.content = content;
    }
}
