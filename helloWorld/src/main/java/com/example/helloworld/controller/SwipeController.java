package com.example.helloworld.controller;

import com.example.helloworld.model.*;
import com.example.helloworld.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/swipes")
public class SwipeController {
    @Autowired
    private SwipeService swipeService;
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;

    @PostMapping
    public Swipe swipe(@RequestParam Long userId, @RequestParam Long postId, @RequestParam Swipe.SwipeDirection direction) {
        User user = userService.findById(userId).orElseThrow();
        Post post = postService.findAll().stream().filter(p -> p.getId().equals(postId)).findFirst().orElseThrow();
        return swipeService.swipe(user, post, direction);
    }

    @GetMapping("/inbox")
    public List<Post> inbox(@RequestParam Long userId) {
        User user = userService.findById(userId).orElseThrow();
        return swipeService.getRightSwipedPosts(user)
                .stream()
                .map(Swipe::getPost)
                .collect(Collectors.toList());
    }
}