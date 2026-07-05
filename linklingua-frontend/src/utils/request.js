import axios from 'axios';
import { message } from 'antd';
import { isTokenValid } from './jwt';
import { getToken, clearAuth } from './auth';

// 1. 创建axios实例
const request = axios.create({
  // 部署时通过 VITE_API_BASE_URL 注入（如 "/api" 走 nginx 反向代理）；
  // 本地开发缺省回退到后端地址，行为保持不变。
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api', // 后端接口根地址
  timeout: 10000, // 超时时间 10秒
});

// 无需登录态即可访问的接口
const AUTH_FREE_PATHS = ['/user/login', '/user/signup'];

function redirectToLogin() {
  if (window.location.pathname !== '/login') {
    window.location.href = '/login';
  }
}

// 2. 请求拦截器：校验 token 时效，并自动添加 Token
request.interceptors.request.use(
  (config) => {
    const token = getToken();
    const url = config.url || '';
    const isAuthFree = AUTH_FREE_PATHS.some((p) => url.includes(p));

    // 有效 token：正常携带
    if (token && isTokenValid(token)) {
      config.headers.Authorization = `Bearer ${token}`;
      return config;
    }

    // 登录 / 注册接口本身不需要 token
    if (isAuthFree) {
      return config;
    }

    // 需要鉴权的写操作（发布 / 修改 / 删除 / 上传等非 GET 请求）
    const needsAuth = (config.method || 'get').toLowerCase() !== 'get';
    if (needsAuth) {
      // token 缺失或已过期：在请求发出前直接拦截，绝不发往后端
      if (token) clearAuth();
      message.error('login status expired, please re-login');
      redirectToLogin();
      return Promise.reject(new axios.Cancel('TOKEN_EXPIRED'));
    }

    // 公共 GET 请求：若残留过期 token 则顺手清理，但请求照常放行
    if (token) clearAuth();
    return config;
  },
  (error) => Promise.reject(error)
);

// 3. 响应拦截器：统一处理返回结果、错误码
request.interceptors.response.use(
  (res) => {
    const data = res.data;
    // 后端统一格式 {code, message, data}
    if (Number(data.code) !== 200) {
      message.error(data.message || 'request failed');
      return Promise.reject(data);
    }
    return data;
  },
  (error) => {
    // 被取消的请求（AbortController / StrictMode 清理）不算错误，静默忽略
    if (axios.isCancel(error)) {
      return Promise.reject(error);
    }
    // 处理 HTTP 状态码：401 未登录 / 403 无权限
    const status = error.response?.status;
    if (status === 401) {
      message.error('login status expired, please re-login');
      // 清空本地登录态，跳转到登录页
      clearAuth();
      redirectToLogin();
    } else if (status === 403) {
      message.error('no permission');
    } else {
      message.error('server error, please try again later');
    }
    return Promise.reject(error);
  }
);

export default request;