package com.back.domain.post.controller;

import com.back.domain.post.entity.Post;
import com.back.domain.post.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    // @ModelAttribute("siteName") 붙은 메서드를 컨트롤러 안에 두면 타임리프에서 해당 메서드의 리턴값 사용 가능
    @ModelAttribute("siteName")
    public String siteName() {
        return "커뮤니티 사이트 A";
    }

    @AllArgsConstructor
    @Getter
    public static class WriteForm {
        @NotBlank(message = "1-제목을 입력해주세요.")
        @Size(min = 2, max = 20, message = "2-제목은 2자 이상, 20자 이하로 입력 가능합니다.")
        private String title;

        @NotBlank(message = "3-내용을 입력해주세요.")
        @Size(min = 2, max = 100, message = "4-내용은 2자 이상, 20자 이하로 입력 가능합니다.")
        private String content;
    }

    @GetMapping("/posts/write")
    public String write(@ModelAttribute("form") WriteForm form) {
        return "post/write";
    }

    @PostMapping("/posts/doWrite")
    public String createPost(
            // @Valid @ModelAttribute("writeForm") WriteForm form의 축약형
            // @ModelAttribute: 스프링 MVC에서 요청 파라미터를 자바 객체로 바인딩
            // @Valid: form 객체의 필드에 @NotBlank, @Size 등 붙어 있으면 검사
            @ModelAttribute("form") @Valid WriteForm form,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            return "post/write";
        }
        Post newPost = postService.write(form.getTitle(), form.getContent());
        model.addAttribute("post", newPost);
        return "post/writeDone";
    }
}
