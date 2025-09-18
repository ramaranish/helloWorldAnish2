import React, { useState, useEffect } from "react";
import Post from "./Post";
import { swipePost } from "./api";
import { leftSwipePost } from "./leftSwipeApi";
import { useAppContext } from "../../AppContext";
import axios from "axios";
import { sendMessage } from "../Chat/api";

const Feed = ({ onRightSwipe, onLeftSwipe }) => {
  const [posts, setPosts] = useState([]);
  const [index, setIndex] = useState(0);
  const [leftSwiped, setLeftSwiped] = useState([]);
  const { addRightSwipedPost, CURRENT_USER } = useAppContext();

  useEffect(() => {
    const fetchPosts = async () => {
      try {
        const res = await axios.get(`http://localhost:9091/api/posts/feed?userId=${CURRENT_USER.id}`);
        setPosts(res.data);
      } catch (err) {
        setPosts([]);
      }
    };
    if (CURRENT_USER && CURRENT_USER.id) {
      fetchPosts();
    }
  }, [CURRENT_USER]);

  const handleRightSwipe = async () => {
    const post = posts[index];
    addRightSwipedPost(post);
    try {
      await swipePost({
        postId: post.id,
        userId: CURRENT_USER.id,
        direction: "RIGHT",
        timestamp: new Date().toISOString(),
      });
      // Send chat message to post author
      if (post.author && post.author.username) {
        await sendMessage({
          sender: CURRENT_USER.username,
          content: "I want to collaborate with u",
          recipient: post.author.username,
          timestamp: new Date().toISOString()
        });
      }
    } catch (err) {
      console.error("Failed to save right swipe or send message:", err);
    }
    if (onRightSwipe) onRightSwipe(post, CURRENT_USER);
    // Remove the swiped post from posts array
    setPosts(prev => prev.filter((p, i) => i !== index));
    // Reset index if no posts remain
    if (visiblePosts.length === 1) {
      setIndex(0);
    } else if (index < visiblePosts.length - 1) {
      setIndex(index);
    } else {
      setIndex(0);
    }
  };

  const handleLeftSwipe = async () => {
    const post = posts[index];
    setLeftSwiped((prev) => [...prev, post.id]);
    try {
      await leftSwipePost({
        postId: post.id,
        userId: CURRENT_USER.id,
        timestamp: new Date().toISOString(),
      });
    } catch (err) {
      console.error("Failed to save left swipe:", err);
    }
    if (onLeftSwipe) onLeftSwipe(post, CURRENT_USER);
    goToNext();
  };

  const visiblePosts = posts.filter((p) => !leftSwiped.includes(p.id));
  const currentPost = visiblePosts[index] || null;

  function goToNext() {
    if (index < visiblePosts.length - 1) setIndex(index + 1);
    else if (visiblePosts.length > 0) setIndex(visiblePosts.length - 1);
  }

  function goToPrev() {
    if (index > 0) setIndex(index - 1);
  }

  return (
    <div className="flex flex-col h-full min-h-screen bg-black">
      <div className="flex-1 flex items-center justify-center p-4 w-full">
        <div className="w-full max-w-[70%]">
          {currentPost ? (
            <Post
              post={currentPost}
              onRightSwipe={handleRightSwipe}
              onLeftSwipe={() => { handleLeftSwipe(); goToPrev(); }}
            />
          ) : (
            <div className="text-center text-gray-400 text-xl py-20">No more posts to show.</div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Feed;