package com.back.domain.post.controller;

import com.back.domain.post.entity.Post;
import com.back.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    private String getWriteFormHtml() {
        return """
                <form action="doWrite" method="POST">
                  <input type="text" name="title" placeholder="제목">
                  <br>
                  <textarea name="content" placeholder="내용"></textarea>
                  <br>
                  <input type="submit" value="작성">
                </form>
                """;
    }

    private String getErrorMessageHtml(String errorMessage) {
        return """
                <div style="color:red;">%s</div>
                """.formatted(errorMessage);
    }

    @GetMapping("/posts/write")
    @ResponseBody
    public String write() {
        return getWriteFormHtml();
    }

    @PostMapping("/posts/doWrite")
    @ResponseBody
    public String createPost(@RequestParam(defaultValue = "") String title, @RequestParam(defaultValue = "") String content) {
        if (title.isBlank()) {
            return getErrorMessageHtml("제목을 입력해주세요") + getWriteFormHtml();
        }
        if (content.isBlank()) {
            return getErrorMessageHtml("내용을 입력해주세요") + getWriteFormHtml();

        }
        Post newPost = postService.write(title, content);
        return "%d번 글 생성 완료".formatted(newPost.getId());
    }
}
