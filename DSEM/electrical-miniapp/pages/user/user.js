const request = require('../../utils/request.js');
const app = getApp();

Page({
  data: {
    userInfo: {},
    accountBalance: '0.00',
    roleText: {
      0: '学生',
      1: '公寓管理员',
      2: '系统管理员'
    }
  },

  onShow() {
    // 检查登录状态
    if (!app.globalData.token) {
      wx.redirectTo({
        url: '/pages/login/login'
      });
      return;
    }

    this.loadUserInfo();
    this.loadAccountInfo();
  },

  loadUserInfo() {
    const userInfo = wx.getStorageSync('userInfo');
    if (userInfo) {
      this.setData({ userInfo });
    }

    request.get('/api/user/info').then(res => {
      this.setData({
        userInfo: res.data
      });
      wx.setStorageSync('userInfo', res.data);
    }).catch(() => {
      // 加载失败不处理（request.js 已统一处理 401）
    });
  },

  loadAccountInfo() {
    request.get('/api/account/info').then(res => {
      if (res.data) {
        this.setData({
          accountBalance: parseFloat(res.data.balance).toFixed(2)
        });
      }
    }).catch(() => {
      // 接口出错不处理
    });
  },

  goToUserInfo() {
    wx.navigateTo({
      url: '/pages/user/info/info'
    });
  },

  goToPassword() {
    wx.navigateTo({
      url: '/pages/user/password/password'
    });
  },

  goToRecharge() {
    wx.navigateTo({
      url: '/pages/account/recharge/recharge'
    });
  },

  goToAdmin() {
    wx.navigateTo({
      url: '/pages/admin/admin'
    });
  },

  handleLogout() {
    wx.showModal({
      title: '提示',
      content: '确定要退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          app.clearToken();
          wx.redirectTo({
            url: '/pages/login/login'
          });
        }
      }
    });
  }
});
