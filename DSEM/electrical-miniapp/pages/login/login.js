const request = require('../../utils/request.js');
const app = getApp();

Page({
  data: {
    account: '',
    password: ''
  },

  onLoad() {
    // 检查是否已登录
    const token = app.globalData.token;
    if (token) {
      wx.switchTab({
        url: '/pages/index/index'
      });
    }
  },

  handleLogin() {
    const { account, password } = this.data;

    if (!account) {
      wx.showToast({
        title: '请输入账号',
        icon: 'none'
      });
      return;
    }

    if (!password) {
      wx.showToast({
        title: '请输入密码',
        icon: 'none'
      });
      return;
    }

    request.post('/api/user/login', {
      account,
      password
    }).then(res => {
      // 保存token
      app.setToken(res.data.token);
      // 保存用户角色
      app.globalData.userRole = res.data.role;

      wx.showToast({
        title: '登录成功',
        icon: 'success'
      });

      // 执行登录扣费（仅学生角色扣费，管理员不扣费）
      setTimeout(() => {
        if (res.data.role === 0) {
          // 学生角色，执行扣费
          this.deductForLogin();
        } else {
          // 管理员角色，不扣费，直接进入首页
          wx.switchTab({
            url: '/pages/index/index'
          });
        }
      }, 1000);
    }).catch(err => {
      console.error('登录失败:', err);
    });
  },

  // 登录扣费
  deductForLogin() {
    request.post('/api/user/login/deduct').then(res => {
      const amount = res.data && res.data.amount ? res.data.amount : 0;
      const electricityUsage = res.data && res.data.electricityUsage ? res.data.electricityUsage : 0;
      
      if (res.data && res.data.success) {
        // 扣费成功
        wx.showModal({
          title: '自动扣费',
          content: `本次登录已扣除${amount}元用电费（${electricityUsage}度电）`,
          showCancel: false,
          success: () => {
            wx.switchTab({
              url: '/pages/index/index'
            });
          }
        });
      } else {
        // 余额不足，但不清除token，让用户可以进入系统充值
        wx.showModal({
          title: '余额不足',
          content: `账户余额不足${amount}元（${electricityUsage}度电费用），请尽快充值`,
          showCancel: false,
          success: () => {
            // 直接进入首页，用户可以在"我的"页面充值
            wx.switchTab({
              url: '/pages/index/index'
            });
          }
        });
      }
    }).catch(() => {
      // 扣费接口出错，仍然允许登录
      wx.switchTab({
        url: '/pages/index/index'
      });
    });
  },

  goToRegister() {
    wx.navigateTo({
      url: '/pages/register/register'
    });
  },

  goToForgotPassword() {
    wx.navigateTo({
      url: '/pages/login/forgot-password/forgot-password'
    });
  }
});
