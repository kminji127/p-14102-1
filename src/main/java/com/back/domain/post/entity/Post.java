package com.back.domain.post.entity;

import com.back.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Post extends BaseEntity {
    private String title;
    private String content;

    public void modify(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
