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
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.stream.Collectors;

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

    @PostMapping("/posts/doWrite")
    @ResponseBody
    public String createPost(
            // @Valid @ModelAttribute("writeForm") WriteForm form의 축약형
            // @ModelAttribute: 스프링 MVC에서 요청 파라미터를 자바 객체로 바인딩
            // @Valid: form 객체의 필드에 @NotBlank, @Size 등 붙어 있으면 검사
            @Valid WriteForm form,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorFieldName = "title";
            String errorMessage = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .sorted()
                    .map(message -> message.split("-", 2)[1])
                    .collect(Collectors.joining("<br />"));
            return getErrorMessageHtml(errorMessage) + getWriteFormHtml(form.getTitle(), form.getContent(), errorFieldName);
        }
        Post newPost = postService.write(form.getTitle(), form.getContent());
        return "%d번 글 생성 완료".formatted(newPost.getId());
    }
}
