import request from '../utils/request';

// POST /api/user/login -> { id, token, userName }
export function login(data) {
  return request.post('/user/login', data);
}

// POST /api/user/signup
export function signup(data) {
  return request.post('/user/signup', data);
}
