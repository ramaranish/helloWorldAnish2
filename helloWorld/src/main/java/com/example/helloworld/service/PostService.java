
package com.example.helloworld.service;

import com.example.helloworld.model.Post;
import com.example.helloworld.model.User;
import com.example.helloworld.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import com.example.helloworld.service.LeftSwipeService;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private LeftSwipeService leftSwipeService;

    @Autowired
    private SwipeService swipeService;

    public Post create(Post post) {
        return postRepository.save(post);
    }


    public List<Post> findAll() {
        return postRepository.findAll();
    }

    // Get posts by author (own posts)
    public List<Post> findByAuthor(User author) {
        return postRepository.findAll().stream()
            .filter(post -> post.getAuthor() != null && post.getAuthor().getId().equals(author.getId()))
            .toList();
    }

    // Get all posts for a user, excluding left-swiped posts
    public List<Post> findAllForUser(User user) {
        List<Long> leftSwipedIds = leftSwipeService.getLeftSwipedPostIds(user);
        if (leftSwipedIds.isEmpty()) {
            return postRepository.findAll();
        }
        return postRepository.findAll().stream()
            .filter(post -> !leftSwipedIds.contains(post.getId()))
            .toList();
    }

    // Get posts not swiped (left or right) and not authored by user
    public List<Post> findUnswipedPostsForUser(User user) {
        List<Long> leftSwipedIds = leftSwipeService.getLeftSwipedPostIds(user);
        List<Long> rightSwipedIds = swipeService.getRightSwipedPosts(user).stream().map(s -> s.getPost().getId()).toList();
        return postRepository.findAll().stream()
            .filter(post -> post.getAuthor() != null && !post.getAuthor().getId().equals(user.getId()))
            .filter(post -> !leftSwipedIds.contains(post.getId()) && !rightSwipedIds.contains(post.getId()))
            .toList();
    }
}