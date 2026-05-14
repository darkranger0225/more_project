const app = getApp();

Page({
  data: {
    step: 1,
    username: '',
    phone: '',
    newPassword: '',
    confirmPassword: '',
    userId: null
  },

  onUsernameInput(e) {
    this.setData({ username: e.detail.value });
  },

  onPhoneInput(e) {
    this.setData({ phone: e.detail.value });
  },

  onNewPasswordInput(e) {
    this.setData({ newPassword: e.detail.value });
  },

  onConfirmPasswordInput(e) {
    this.setData({ confirmPassword: e.detail.value });
  },

  // 验证用户
  async verifyUser() {
    const { username, phone } = this.data;

    if (!username.trim()) {
      wx.showToast({ title: '请输入用户名', icon: 'none' });
      return;
    }

    if (!phone.trim()) {
      wx.showToast({ title: '请输入手机号', icon: 'none' });
      return;
    }

    if (!/^1[3-9]\d{9}$/.test(phone)) {
      wx.showToast({ title: '手机号格式不正确', icon: 'none' });
      return;
    }

    wx.showLoading({ title: '验证中...' });

    try {
      const res = await app.request({
        url: '/auth/verify-user',
        method: 'POST',
        data: {
          username: username.trim(),
          phone: phone.trim()
        }
      });

      wx.hideLoading();

      if (res.code === 200) {
        this.setData({
          step: 2,
          userId: res.data.userId
        });
      }
    } catch (error) {
      wx.hideLoading();
      wx.showToast({
        title: error.message || '验证失败',
        icon: 'none'
      });
    }
  },

  // 重置密码
  async resetPassword() {
    const { newPassword, confirmPassword, userId } = this.data;

    if (!newPassword) {
      wx.showToast({ title: '请输入新密码', icon: 'none' });
      return;
    }

    if (newPassword.length < 6) {
      wx.showToast({ title: '密码至少6位', icon: 'none' });
      return;
    }

    if (newPassword !== confirmPassword) {
      wx.showToast({ title: '两次密码不一致', icon: 'none' });
      return;
    }

    wx.showLoading({ title: '重置中...' });

    try {
      const res = await app.request({
        url: '/auth/reset-password',
        method: 'POST',
        data: {
          userId: userId,
          newPassword: newPassword
        }
      });

      wx.hideLoading();

      if (res.code === 200) {
        wx.showToast({
          title: '密码重置成功',
          icon: 'success'
        });

        setTimeout(() => {
          wx.redirectTo({
            url: '/pages/login/login'
          });
        }, 1500);
      }
    } catch (error) {
      wx.hideLoading();
      wx.showToast({
        title: error.message || '重置失败',
        icon: 'none'
      });
    }
  },

  goToLogin() {
    wx.redirectTo({
      url: '/pages/login/login'
    });
  }
});