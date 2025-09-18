// ...existing code...
package com.example.helloworld.controller;

import com.example.helloworld.model.ChatMessage;
import com.example.helloworld.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/chat")

public class ChatController {
    // GET /api/chat/messages/recipient/{recipient} - Get messages by recipient
    @GetMapping("/messages/recipient/{recipient}")
    public ResponseEntity<List<ChatMessage>> getMessagesByRecipient(@PathVariable String recipient) {
        List<ChatMessage> messages = chatService.getMessagesByRecipient(recipient);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @Autowired
    private ChatService chatService;

    // POST /api/chat/messages - Send a new message
    @PostMapping("/messages")
    public ResponseEntity<ChatMessage> sendMessage(@RequestBody ChatMessage message) {
        try {
            ChatMessage savedMessage = chatService.saveMessage(message);
            return new ResponseEntity<>(savedMessage, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET /api/chat/messages - Get all messages
    @GetMapping("/messages")
    public ResponseEntity<List<ChatMessage>> getAllMessages() {
        List<ChatMessage> messages = chatService.getAllMessages();
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    // GET /api/chat/messages/{id} - Get message by ID
    @GetMapping("/messages/{id}")
    public ResponseEntity<ChatMessage> getMessageById(@PathVariable Long id) {
        Optional<ChatMessage> message = chatService.getMessageById(id);
        if (message.isPresent()) {
            return new ResponseEntity<>(message.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // GET /api/chat/messages/sender/{sender} - Get messages by sender
    @GetMapping("/messages/sender/{sender}")
    public ResponseEntity<List<ChatMessage>> getMessagesBySender(@PathVariable String sender) {
        List<ChatMessage> messages = chatService.getMessagesBySender(sender);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    // GET /api/chat/messages/recent/{limit} - Get recent messages
    @GetMapping("/messages/recent/{limit}")
    public ResponseEntity<List<ChatMessage>> getRecentMessages(@PathVariable int limit) {
        List<ChatMessage> messages = chatService.getRecentMessages(limit);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    // GET /api/chat/messages/search - Search messages by content
    @GetMapping("/messages/search")
    public ResponseEntity<List<ChatMessage>> searchMessages(@RequestParam String keyword) {
        List<ChatMessage> messages = chatService.searchMessages(keyword);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    // DELETE /api/chat/messages/{id} - Delete message by ID
    @DeleteMapping("/messages/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        boolean deleted = chatService.deleteMessage(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // GET /api/chat/messages/after/{timestamp} - Get messages after timestamp
    @GetMapping("/messages/after/{timestamp}")
    public ResponseEntity<List<ChatMessage>> getMessagesAfter(@PathVariable String timestamp) {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(timestamp);
            List<ChatMessage> messages = chatService.getMessagesAfter(dateTime);
            return new ResponseEntity<>(messages, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
