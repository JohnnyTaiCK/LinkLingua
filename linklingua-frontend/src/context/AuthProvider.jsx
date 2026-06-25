import { useState, useCallback, useMemo, useEffect } from 'react';
import { AuthContext } from './auth-context';
import {
  getStoredUser,
  setAuth,
  clearAuth,
  AUTH_LOGOUT_EVENT,
} from '../utils/auth';

export function AuthProvider({ children }) {
  const [user, setUser] = useState(getStoredUser);

  const login = useCallback((loginVO) => {
    setAuth(loginVO);
    setUser({ token: loginVO.token, id: loginVO.id, userName: loginVO.userName });
  }, []);

  const logout = useCallback(() => {
    clearAuth();
    setUser(null);
  }, []);

  // Sync logged-out state when the token expires and gets cleared by the
  // request/response interceptors, or when another tab logs in/out.
  useEffect(() => {
    const handleLogout = () => setUser(null);
    const handleStorage = (e) => {
      if (e.key === 'token' || e.key === null) {
        setUser(getStoredUser());
      }
    };
    window.addEventListener(AUTH_LOGOUT_EVENT, handleLogout);
    window.addEventListener('storage', handleStorage);
    return () => {
      window.removeEventListener(AUTH_LOGOUT_EVENT, handleLogout);
      window.removeEventListener('storage', handleStorage);
    };
  }, []);

  const value = useMemo(
    () => ({ user, isLoggedIn: !!user, login, logout }),
    [user, login, logout]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
