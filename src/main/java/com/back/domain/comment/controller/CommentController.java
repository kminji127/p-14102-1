package com.back.domain.comment.controller;

import com.back.domain.post.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final PostService postService;

    @AllArgsConstructor
    @Getter
    public static class WriteForm {
        @NotBlank
        @Size(min = 2, max = 100)
        private String content;
    }

    @PostMapping("/posts/{postId}/comments")
    @Transactional
    public String addComment(@PathVariable int postId,
                             @Valid WriteForm writeForm) {
        postService.addComment(postId, writeForm.getContent());
        return "redirect:/posts/" + postId;
    }

    @GetMapping("/posts/{postId}/comments/{commentId}")
    @Transactional
    public String delete(@PathVariable int postId,
                         @PathVariable int commentId) {
        postService.deleteComment(postId, commentId);
        return "redirect:/posts/" + postId;
    }
}
