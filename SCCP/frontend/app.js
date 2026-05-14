App({
  globalData: {
    userInfo: null,
    token: null,
    // 后端API地址
    baseUrl: 'http://localhost:8080',
    role: null
  },

  onLaunch() {
    // 检查本地存储的登录状态
    const token = wx.getStorageSync('token');
    const userInfo = wx.getStorageSync('userInfo');

    if (token && userInfo) {
      this.globalData.token = token;
      this.globalData.userInfo = userInfo;
      this.globalData.role = userInfo.role;
    }
  },

  // 全局请求方法
  request(options) {
    const app = this;
    const token = app.globalData.token;
    
    return new Promise((resolve, reject) => {
      wx.request({
        url: app.globalData.baseUrl + options.url,
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
            // Token过期，清除登录状态并跳转到登录页
            app.clearLoginData();
            wx.redirectTo({
              url: '/pages/login/login'
            });
            reject(res);
          } else {
            wx.showToast({
              title: '网络错误',
              icon: 'none'
            });
            reject(res);
          }
        },
        fail: (err) => {
          wx.showToast({
            title: '网络请求失败',
            icon: 'none'
          });
          reject(err);
        }
      });
    });
  },

  // 清除登录数据
  clearLoginData() {
    // 清除全局数据
    this.globalData.token = null;
    this.globalData.userInfo = null;
    this.globalData.role = null;

    // 清除本地存储的登录信息
    wx.removeStorageSync('token');
    wx.removeStorageSync('userInfo');

    // 清除其他业务缓存数据
    wx.removeStorageSync('students');
    wx.removeStorageSync('classes');
    wx.removeStorageSync('homeworkList');
    wx.removeStorageSync('noticeList');
    wx.removeStorageSync('leaveList');
    wx.removeStorageSync('scoreList');

    // 清除所有临时缓存
    try {
      const res = wx.getStorageInfoSync();
      if (res && res.keys) {
        res.keys.forEach(key => {
          // 保留系统相关的不清除
          if (!key.startsWith('sys_') && !key.startsWith('wx_')) {
            wx.removeStorageSync(key);
          }
        });
      }
    } catch (e) {
      console.error('清除缓存失败', e);
    }
  }
});