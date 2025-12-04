package com.back.domain.post.controller;

import com.back.domain.post.entity.Post;
import com.back.domain.post.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    // @ModelAttribute("siteName") 붙은 메서드를 컨트롤러 안에 두면 타임리프에서 해당 메서드의 리턴값 사용 가능
    @ModelAttribute("siteName")
    public String siteName() {
        return "커뮤니티 사이트 A";
    }

    record WriteForm(
            @NotBlank(message = "1-title-제목을 입력해주세요.")
            @Size(min = 2, max = 20, message = "2-title-제목은 2자 이상, 20자 이하로 입력 가능합니다.")
            String title, // 기본적으로 final, 그래서 setter가 없고 수정하려면 객체 자체를 다시 만들어야 한다.

            @NotBlank(message = "3-content-내용을 입력해주세요.")
            @Size(min = 2, max = 100, message = "4-content-내용은 2자 이상, 20자 이하로 입력 가능합니다.")
            String content
    ) {
    }

    record ModifyForm(
            @NotBlank(message = "1-title-제목을 입력해주세요.")
            @Size(min = 2, max = 20, message = "2-title-제목은 2자 이상, 20자 이하로 입력 가능합니다.")
            String title,

            @NotBlank(message = "3-content-내용을 입력해주세요.")
            @Size(min = 2, max = 100, message = "4-content-내용은 2자 이상, 20자 이하로 입력 가능합니다.")
            String content
    ) {
    }

    @GetMapping("/posts/write")
    public String write(@ModelAttribute("form") WriteForm form) {
        return "post/write";
    }

    @PostMapping("/posts/write")
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
        Post newPost = postService.write(form.title, form.content);
        // 302로 응답
        return "redirect:/posts/" + newPost.getId();
    }

    @GetMapping("/posts/{id}")
    @Transactional(readOnly = true)
    public String showDetail(@PathVariable int id, Model model) {
        Post post = postService.findById(id).get();
        model.addAttribute("post", post);
        return "post/detail";
    }

    @GetMapping("/posts")
    @Transactional(readOnly = true)
//    @ResponseBody // 스프링이 객체를 HTTP 응답 본문으로 직렬화
    public String showList(Model model) {
        // 스프링부트는 기본적으로 Jackson 라이브러리를 포함하고 있어서 객체를 JSON으로 자동 변환
        // JSON으로 변환 시 Content-Type: application/json 헤더가 자동으로 설정됨
        // public이나, getter 메소드가 있는 private 필드만 포함
        List<Post> posts = postService.findAll();
        model.addAttribute("posts", posts);
        return "post/list";
    }

    @GetMapping("/posts/")
    public String redirectToList() {
        return "redirect:/posts";
    }

    @GetMapping("/posts/{id}/modify")
    @Transactional(readOnly = true)
    public String showModify(@PathVariable int id,
                             @ModelAttribute("form") ModifyForm form,
                             Model model) {
        Post post = postService.findById(id).get();
        model.addAttribute("post", post);
        model.addAttribute("form", new ModifyForm(post.getTitle(), post.getContent()));
        return "post/modify";
    }

    @PutMapping("/posts/{id}/modify")
    @Transactional
    public String modify(@PathVariable int id,
                         @Valid @ModelAttribute("form") WriteForm form,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "post/modify";
        }
        Post post = postService.modify(id, form.title, form.content);
        // 302로 응답
        return "redirect:/posts/" + post.getId();
    }
}
