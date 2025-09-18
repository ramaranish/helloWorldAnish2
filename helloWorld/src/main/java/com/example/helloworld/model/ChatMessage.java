package com.example.helloworld.model;

import jakarta.persistence.*;

@Entity
@Table(name = "chat_messages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String sender;

    @Column(nullable = false)
    private String recipient;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private java.time.LocalDateTime timestamp;


    // Default constructor
    public ChatMessage() {
        this.timestamp = java.time.LocalDateTime.now();
    }

    // Constructor with parameters
    public ChatMessage(String sender, String recipient, String content, java.time.LocalDateTime timestamp) {
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
        this.timestamp = timestamp != null ? timestamp : java.time.LocalDateTime.now();
    }
    public java.time.LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(java.time.LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }


    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public String toString() {
        return "ChatMessage{" +
                "id=" + id +
                ", sender='" + sender + '\'' +
                ", recipient='" + recipient + '\'' +
                ", content='" + content + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
