import { isTokenValid } from './jwt';

const TOKEN_KEY = 'token';
const USER_ID_KEY = 'userId';
const USER_NAME_KEY = 'userName';

// Dispatched on the window whenever auth is cleared (e.g. expired token), so
// the React layer (AuthProvider) can sync its state without a full reload.
export const AUTH_LOGOUT_EVENT = 'auth:logout';

export function getToken() {
  return localStorage.getItem(TOKEN_KEY);
}

export function setAuth(loginVO) {
  localStorage.setItem(TOKEN_KEY, loginVO.token);
  localStorage.setItem(USER_ID_KEY, String(loginVO.id));
  localStorage.setItem(USER_NAME_KEY, loginVO.userName ?? '');
}

export function clearAuth() {
  localStorage.removeItem(TOKEN_KEY);
  localStorage.removeItem(USER_ID_KEY);
  localStorage.removeItem(USER_NAME_KEY);
  window.dispatchEvent(new Event(AUTH_LOGOUT_EVENT));
}

// Returns the stored user only when a non-expired token is present.
// An expired/malformed token is cleaned up and treated as logged-out.
export function getStoredUser() {
  const token = getToken();
  if (!isTokenValid(token)) {
    if (token) clearAuth();
    return null;
  }
  return {
    token,
    id: Number(localStorage.getItem(USER_ID_KEY)) || null,
    userName: localStorage.getItem(USER_NAME_KEY) || '',
  };
}
