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

    private String getWriteFormHtml(String title, String content, String fieldToFocus) {
        return """
                <form action="doWrite" method="POST">
                  <input type="text" name="title" placeholder="제목" value="%s">
                  <br>
                  <textarea name="content" placeholder="내용">%s</textarea>
                  <br>
                  <input type="submit" value="작성">
                </form>
                <script>
                  const fieldToFocus = '%s';
                  if (fieldToFocus.length > 0) {
                    // 현재까지 나온 모든 폼 중 마지막 1개 찾기
                    const forms = document.querySelectorAll('form');
                    const lastForm = forms[forms.length - 1];
                    // 해당 폼에서 지정된 필드에 포커스
                    lastForm[fieldToFocus].focus();
                  }
                </script>
                """.formatted(title, content, fieldToFocus);
    }

    private String getErrorMessageHtml(String errorMessage) {
        return """
                <div style="color:red;">%s</div>
                """.formatted(errorMessage);
    }

    @GetMapping("/posts/write")
    @ResponseBody
    public String write() {
        return getWriteFormHtml("", "", "title");
    }

    @PostMapping("/posts/doWrite")
    @ResponseBody
    public String createPost(@RequestParam(defaultValue = "") String title, @RequestParam(defaultValue = "") String content) {
        if (title.isBlank()) {
            return getErrorMessageHtml("제목을 입력해주세요") + getWriteFormHtml(title, content, "title");
        }
        if (content.isBlank()) {
            return getErrorMessageHtml("내용을 입력해주세요") + getWriteFormHtml(title, content, "content");

        }
        Post newPost = postService.write(title, content);
        return "%d번 글 생성 완료".formatted(newPost.getId());
    }
}
