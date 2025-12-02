package com.back.global.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass // 엔티티의 부모 클래스에는 이걸 달아야 한다.
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @CreatedDate
//    @Getter(AccessLevel.PRIVATE) // 필드별로 getter의 엑세스 레벨을 조절 가능
    private LocalDateTime createdDate;

    @LastModifiedDate
//    @Getter(AccessLevel.PROTECTED)
    private LocalDateTime modifyDate;

}