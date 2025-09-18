// src/Components/Chat/ChatContext.js
import React, { createContext, useContext, useState, useEffect, useCallback } from "react";
import * as chatApi from "./api";
import { useAppContext } from "../../AppContext";

const ChatContext = createContext();

export const useChat = () => useContext(ChatContext);

export const ChatProvider = ({ children }) => {
  const [messages, setMessages] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [selectedUser, setSelectedUser] = useState(null);
  const { CURRENT_USER } = useAppContext();

  // Fetch messages for the current user as recipient on mount
  useEffect(() => {
    if (!CURRENT_USER || !CURRENT_USER.username) return;
    setLoading(true);
    chatApi.getMessagesByRecipient(CURRENT_USER.username)
      .then(setMessages)
      .catch(setError)
      .finally(() => setLoading(false));
  }, [CURRENT_USER]);

  // Send a new message
  const sendMessage = useCallback(async (msg) => {
    setLoading(true);
    // Optimistically add the message for instant feedback
    const tempId = Date.now();
    const optimisticMsg = { ...msg, id: tempId };
    setMessages((prev) => [...prev, optimisticMsg]);
    try {
      const newMsg = await chatApi.sendMessage(msg);
      setMessages((prev) => prev.map(m => m.id === tempId ? newMsg : m));
    } catch (err) {
      setError(err);
      // Remove optimistic message on error
      setMessages((prev) => prev.filter(m => m.id !== tempId));
    }
    setLoading(false);
  }, []);

  // Delete a message
  const deleteMessage = useCallback(async (id) => {
    setLoading(true);
    try {
      await chatApi.deleteMessage(id);
      setMessages((prev) => prev.filter((m) => m.id !== id));
    } catch (err) {
      setError(err);
    }
    setLoading(false);
  }, []);

  // Search messages
  const searchMessages = useCallback(async (keyword) => {
    setLoading(true);
    try {
      const results = await chatApi.searchMessages(keyword);
      setMessages(results);
    } catch (err) {
      setError(err);
    }
    setLoading(false);
  }, []);

  // Optionally add more API wrappers (get by sender, recent, after, etc.)

  return (
    <ChatContext.Provider value={{ messages, loading, error, sendMessage, deleteMessage, searchMessages, selectedUser, setSelectedUser }}>
      {children}
    </ChatContext.Provider>
  );
};
