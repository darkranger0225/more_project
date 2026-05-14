// utils/http.js
import { request } from '../miniprogram_npm/mina-request/index';

// 获取应用实例
const app = getApp();

// 创建请求实例
const http = request.create({
  baseURL: app.globalData.baseUrl,
  timeout: 10000,
  header: {
    'Content-Type': 'application/json'
  }
});

// 请求拦截器
http.interceptors.request.use(
  config => {
    // 从本地存储获取token
    const token = wx.getStorageSync('token');
    // 如果token存在，添加到请求头
    if (token) {
      config.header['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

// 响应拦截器
http.interceptors.response.use(
  response => {
    const res = response.data;
    // 根据后端返回的状态码处理响应
    if (res.code === 200) {
      return res;
    } else {
      // 错误处理
      wx.showToast({
        title: res.message || '请求失败',
        icon: 'none'
      });
      // 如果是未授权错误，清除token并跳转到登录页
      if (res.code === 401) {
        wx.removeStorageSync('token');
        wx.removeStorageSync('userId');
        wx.removeStorageSync('isAdmin');
        wx.navigateTo({
          url: '/pages/login/login'
        });
      }
      return Promise.reject(res);
    }
  },
  error => {
    // 网络错误处理
    wx.showToast({
      title: '网络错误，请检查网络连接',
      icon: 'none'
    });
    return Promise.reject(error);
  }
);

module.exports = http;