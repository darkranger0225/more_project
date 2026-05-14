const app = getApp();

Page({
  data: {
    username: '',
    password: ''
  },

  onLoad() {
    // 检查是否已登录
    const token = wx.getStorageSync('token');
    if (token) {
      wx.switchTab({
        url: '/pages/index/index'
      });
    }
  },

  onUsernameInput(e) {
    this.setData({
      username: e.detail.value
    });
  },

  onPasswordInput(e) {
    this.setData({
      password: e.detail.value
    });
  },

  async onLogin() {
    const { username, password } = this.data;

    if (!username.trim()) {
      wx.showToast({
        title: '请输入用户名',
        icon: 'none'
      });
      return;
    }

    if (!password.trim()) {
      wx.showToast({
        title: '请输入密码',
        icon: 'none'
      });
      return;
    }

    wx.showLoading({
      title: '登录中...'
    });

    try {
      const res = await app.request({
        url: '/auth/login',
        method: 'POST',
        data: {
          username: username.trim(),
          password: password.trim()
        }
      });

      wx.hideLoading();

      if (res.code === 200) {
        const { token, userId, username: userName, realName, role, avatar } = res.data;
        
        const userInfo = {
          userId,
          username: userName,
          realName,
          role,
          avatar
        };

        // 保存登录信息
        wx.setStorageSync('token', token);
        wx.setStorageSync('userInfo', userInfo);

        // 更新全局数据
        app.globalData.token = token;
        app.globalData.userInfo = userInfo;
        app.globalData.role = role;

        wx.showToast({
          title: '登录成功',
          icon: 'success'
        });

        setTimeout(() => {
          wx.switchTab({
            url: '/pages/index/index'
          });
        }, 1500);
      }
    } catch (error) {
      wx.hideLoading();
      wx.showToast({
        title: error.message || '登录失败',
        icon: 'none'
      });
    }
  },

  goToRegister() {
    wx.navigateTo({
      url: '/pages/register/register'
    });
  },

  goToForgotPassword() {
    wx.navigateTo({
      url: '/pages/forgot-password/forgot-password'
    });
  }
});