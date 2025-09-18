// src/Components/Chat/api.js
// Utility functions for chat API endpoints

const API_BASE = "http://localhost:9091/api/chat/messages";

/**
 * Send a new chat message
 * @param {{sender: string, recipient: string, content: string}} message
 */
export async function sendMessage(message) {
  console.log("Sending message:", message);
  const res = await fetch(API_BASE, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      sender: message.sender,
      recipient: message.recipient,
      content: message.content,
      timestamp: message.timestamp
    }),
  });
  if (!res.ok) throw new Error("Failed to send message");
  return res.json();
}

export async function getAllMessages() {
  const res = await fetch(API_BASE);
  if (!res.ok) throw new Error("Failed to fetch messages");
  return res.json();
}

export async function getMessageById(id) {
  const res = await fetch(`${API_BASE}/${id}`);
  if (!res.ok) throw new Error("Failed to fetch message by ID");
  return res.json();
}

export async function getMessagesBySender(sender) {
  const res = await fetch(`${API_BASE}/sender/${sender}`);
  if (!res.ok) throw new Error("Failed to fetch messages by sender");
  return res.json();
}

export async function getMessagesByRecipient(recipient) {
  const res = await fetch(`${API_BASE}/recipient/${recipient}`);
  if (!res.ok) throw new Error("Failed to fetch messages by recipient");
  return res.json();
}

export async function getRecentMessages(limit) {
  const res = await fetch(`${API_BASE}/recent/${limit}`);
  if (!res.ok) throw new Error("Failed to fetch recent messages");
  return res.json();
}

export async function searchMessages(keyword) {
  const res = await fetch(`${API_BASE}/search?keyword=${encodeURIComponent(keyword)}`);
  if (!res.ok) throw new Error("Failed to search messages");
  return res.json();
}

export async function deleteMessage(id) {
  const res = await fetch(`${API_BASE}/${id}`, { method: "DELETE" });
  if (!res.ok) throw new Error("Failed to delete message");
  return res.json();
}

// getMessagesAfter removed
