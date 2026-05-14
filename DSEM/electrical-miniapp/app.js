App({
  globalData: {
    userInfo: null,
    token: null,
    baseUrl: 'http://localhost:8080'
  },

  onLaunch() {
    // 从本地存储获取token
    const token = wx.getStorageSync('token');
    if (token) {
      this.globalData.token = token;
    }
  },

  setToken(token) {
    this.globalData.token = token;
    wx.setStorageSync('token', token);
  },

  clearToken() {
    this.globalData.token = null;
    wx.removeStorageSync('token');
    wx.removeStorageSync('userInfo');
  }
});
