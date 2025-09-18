import React, { createContext, useContext, useState } from "react";

const AuthContext = createContext();

export const useAuth = () => useContext(AuthContext);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null); // { id, name, email }
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);


  // Real login using backend API
  const login = async (email, password) => {
    setLoading(true);
    setError(null);
    try {
      const res = await fetch("http://localhost:9091/api/users/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username: email, password }),
      });
      const data = await res.json();
      if (!res.ok || data.error) {
        setError(data.error || "Login failed");
        setLoading(false);
        return;
      }
      // Store token in localStorage
      localStorage.setItem("token", data.token);
      setUser({
        id: data.id,
        username: data.username || data.name || data.email || "User",
        name: data.name || data.username || data.email || "User",
        email: data.email,
        ...data // in case backend returns more fields
      });
    } catch (err) {
      setError("Network error");
    }
    setLoading(false);
  };


  // Real signup using backend API
  const signup = async (name, email, password) => {
    setLoading(true);
    setError(null);
    try {
      const res = await fetch("http://localhost:9091/api/users/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username: email, email, password }),
      });
      const data = await res.json();
      if (!res.ok || data.error) {
        setError(data.error || "Signup failed");
        setLoading(false);
        return;
      }
      // Auto-login after signup
      await login(email, password);
    } catch (err) {
      setError("Network error");
    }
    setLoading(false);
  };

  const logout = () => {
    setUser(null);
    localStorage.removeItem("token");
  };

  // On mount, check for token and set user if present (optional, for persistence)
  React.useEffect(() => {
    // Optionally, fetch user info if token exists
    // For now, do nothing to avoid overwriting real user
  }, []);

  return (
    <AuthContext.Provider value={{ user, login, signup, logout, loading, error }}>
      {children}
    </AuthContext.Provider>
  );
};
