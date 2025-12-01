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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    private String getWriteFormHtml(String title, String content) {
        return """
                <form action="doWrite" method="POST">
                  <input type="text" name="title" placeholder="제목" value="%s">
                  <br>
                  <textarea name="content" placeholder="내용">%s</textarea>
                  <br>
                  <input type="submit" value="작성">
                </form>
                <script>
                  // 현재까지 나온 모든 폼 중 마지막 1개 찾기
                  const forms = document.querySelectorAll('form');
                  const lastForm = forms[forms.length - 1];
                  // 포커스할 필드는 ul 안의 li 에서 첫 번째로 나온 ErrorFieldName으로 설정
                  // previousElementSibling으로 form 이전 데이터인 ul 탐색
                  // data-error-field-name => dataset으로 매핑됨
                  const fieldToFocus = lastForm.previousElementSibling?.querySelector('li')?.dataset?.errorFieldName || '';
                  if (fieldToFocus.length > 0) {
                    // 해당 폼에서 지정된 필드에 포커스
                    lastForm[fieldToFocus].focus();
                  }
                </script>
                """.formatted(title, content);
    }

    private String getErrorMessageHtml(String errorMessage) {
        return """
                <ul style="color:red;">%s</ul>
                """.formatted(errorMessage);
    }

    @GetMapping("/posts/write")
    @ResponseBody
    public String write() {
        return getWriteFormHtml("", "");
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
            String errorMessage = bindingResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> (fieldError.getField() + "-" + fieldError.getDefaultMessage()).split("-", 3))
                    // 주석: 에러 번호, 속성(data-error-field-name): 필드명, 내용: 에러 문구
                    .map(field -> "<!--%s--><li data-error-field-name=\"%s\">%s</li>".formatted(field[1], field[0], field[2]))
                    .sorted()
                    .collect(Collectors.joining("\n"));
            return getErrorMessageHtml(errorMessage) + getWriteFormHtml(form.getTitle(), form.getContent());
        }
        Post newPost = postService.write(form.getTitle(), form.getContent());
        return "%d번 글 생성 완료".formatted(newPost.getId());
    }
}
