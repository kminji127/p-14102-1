package com.back.domain.post.service;

import com.back.domain.comment.entity.Comment;
import com.back.domain.post.entity.Post;
import com.back.domain.post.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public long count() {
        return postRepository.count();
    }

    @Transactional
    public Post write(String title, String content) {
        return postRepository.save(new Post(title, content));
    }

    public Optional<Post> findById(int id) {
        return postRepository.findById(id);
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Post modify(int id, String title, String content) {
        Post post = postRepository.findById(id).get();
        post.modify(title, content);
        return post;
    }

    public void delete(int id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));
        postRepository.delete(post);
    }

    public void addComment(int id, String commentContent) {
        Post post = postRepository.findById(id).get();
        post.addComment(commentContent);
    }

    public boolean deleteComment(int postId, int commentId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));
        return post.deleteComment(commentId);
    }

    public void modifyComment(int postId, int commentId, String content) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));
        Comment comment = post.findCommentById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));
        comment.modify(content);
    }
}
