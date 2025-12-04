package com.back.domain.comment.controller;

import com.back.domain.comment.entity.Comment;
import com.back.domain.post.entity.Post;
import com.back.domain.post.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

    @AllArgsConstructor
    @Getter
    public static class ModifyForm {
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

    @GetMapping("/posts/{postId}/comments/{commentId}/modify")
    @Transactional(readOnly = true)
    public String showModify(@PathVariable int postId,
                             @PathVariable int commentId,
                             Model model) {
        Post post = postService.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));
        Comment comment = post.findCommentById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));
        model.addAttribute("post", post);
        model.addAttribute("comment", comment);
        return "post/comment/modify";
    }

    @PostMapping("/posts/{postId}/comments/{commentId}/modify")
    @Transactional()
    public String modify(@PathVariable int postId,
                         @PathVariable int commentId,
                         @ModelAttribute("form") ModifyForm form) {
        postService.modifyComment(postId, commentId, form.getContent());
        return "redirect:/posts/" + postId;
    }
}
