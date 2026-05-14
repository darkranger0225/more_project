const app = getApp();

const baseUrl = 'http://localhost:8080';

// 标记是否正在跳转到登录页，防止重复跳转
let isRedirectingToLogin = false;

const request = (options) => {
  return new Promise((resolve, reject) => {
    const token = app.globalData.token;
    
    wx.request({
      url: baseUrl + options.url,
      method: options.method || 'GET',
      data: options.data || {},
      header: {
        'Content-Type': 'application/json',
        'Authorization': token ? 'Bearer ' + token : ''
      },
      success: (res) => {
        if (res.statusCode === 200) {
          if (res.data.code === 200) {
            resolve(res.data);
          } else {
            wx.showToast({
              title: res.data.message || '请求失败',
              icon: 'none'
            });
            reject(res.data);
          }
        } else if (res.statusCode === 401) {
          // 避免重复处理401
          if (isRedirectingToLogin) {
            reject(res);
            return;
          }
          isRedirectingToLogin = true;
          
          // token过期，清除登录状态
          app.clearToken();
          wx.showToast({
            title: '登录已过期，请重新登录',
            icon: 'none'
          });
          
          // 延迟跳转，避免竞态条件
          setTimeout(() => {
            isRedirectingToLogin = false;
            wx.redirectTo({
              url: '/pages/login/login'
            });
          }, 1500);
          
          reject(res);
        } else if (res.statusCode === 403) {
          wx.showToast({
            title: '没有权限访问',
            icon: 'none'
          });
          reject(res);
        } else if (res.statusCode === 500) {
          wx.showToast({
            title: '服务器错误，请稍后重试',
            icon: 'none'
          });
          reject(res);
        } else {
          wx.showToast({
            title: '请求失败：' + res.statusCode,
            icon: 'none'
          });
          reject(res);
        }
      },
      fail: (err) => {
        wx.showToast({
          title: '网络请求失败，请检查网络',
          icon: 'none'
        });
        reject(err);
      }
    });
  });
};

module.exports = {
  get: (url, data) => request({ url, method: 'GET', data }),
  post: (url, data, params) => request({ url, method: 'POST', data, ...params }),
  put: (url, data) => request({ url, method: 'PUT', data }),
  del: (url, data) => request({ url, method: 'DELETE', data })
};
