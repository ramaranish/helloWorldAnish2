import React, { useEffect, useState } from "react";
import { useChat } from "./ChatContext";
import { getAllUsers } from "./usersApi";
import { useAppContext } from "../../AppContext";


const InboxList = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const { messages, selectedUser, setSelectedUser } = useChat();
  const { CURRENT_USER } = useAppContext();

  useEffect(() => {
    getAllUsers()
      .then(setUsers)
      .catch(setError)
      .finally(() => setLoading(false));
  }, []);

  // Build a list of unique chat partners (other users you've messaged or received from)
  const chatPartners = Array.from(new Set(
    messages
      .map(m => m.sender === CURRENT_USER.username ? m.recipient : m.sender)
      .filter(username => username && username !== CURRENT_USER.username)
  ));
  const filteredUsers = users.filter(user => chatPartners.includes(user.username));

  // Find last message for each chat partner
  const usersWithLastMsg = filteredUsers.map((user) => {
    const lastMsg = messages.filter((m) =>
      (m.sender === CURRENT_USER.username && m.recipient === user.username) ||
      (m.recipient === CURRENT_USER.username && m.sender === user.username)
    ).pop();
    return {
      ...user,
      lastMessage: lastMsg ? lastMsg.content : "No messages yet.",
      unread: 0, // You can implement unread logic as needed
    };
  });

  return (
    <div className="w-72 bg-gray-900 border-r border-gray-800 h-full flex flex-col">
      <div className="p-4 border-b border-gray-800">
        <h2 className="text-xl font-bold text-green-400">Inbox</h2>
      </div>
      {loading && <div className="text-gray-400 p-4">Loading users...</div>}
      {error && <div className="text-red-400 p-4">{error.message || "Failed to fetch users"}</div>}
      <ul className="flex-1 overflow-y-auto">
        {usersWithLastMsg.map((user) => (
          <li
            key={user.id}
            onClick={() => setSelectedUser(user)}
            className={`px-4 py-3 cursor-pointer flex items-center justify-between border-b border-gray-800 transition bg-opacity-80 hover:bg-gray-800 ${selectedUser && selectedUser.id === user.id ? "bg-gray-800" : ""}`}
          >
            <div>
              <div className="font-semibold text-white">{user.username || user.name}</div>
              <div className="text-gray-400 text-xs truncate max-w-[140px]">{user.lastMessage}</div>
            </div>
            {user.unread > 0 && (
              <span className="ml-2 bg-green-500 text-xs text-white rounded-full px-2 py-0.5 font-bold">
                {user.unread}
              </span>
            )}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default InboxList;
