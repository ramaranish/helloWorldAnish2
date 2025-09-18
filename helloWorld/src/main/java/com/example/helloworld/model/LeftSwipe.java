package com.example.helloworld.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "left_swipes")
public class LeftSwipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Post post;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public LeftSwipe() {
        this.timestamp = LocalDateTime.now();
    }

    public LeftSwipe(User user, Post post, LocalDateTime timestamp) {
        this.user = user;
        this.post = post;
        this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Post getPost() { return post; }
    public void setPost(Post post) { this.post = post; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
