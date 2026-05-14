const app = getApp();

Page({
  data: {
    oldPassword: '',
    newPassword: '',
    confirmPassword: ''
  },

  onOldPasswordInput(e) {
    this.setData({ oldPassword: e.detail.value });
  },

  onNewPasswordInput(e) {
    this.setData({ newPassword: e.detail.value });
  },

  onConfirmPasswordInput(e) {
    this.setData({ confirmPassword: e.detail.value });
  },

  async savePassword() {
    const { oldPassword, newPassword, confirmPassword } = this.data;

    if (!oldPassword) {
      wx.showToast({ title: '请输入原密码', icon: 'none' });
      return;
    }

    if (!newPassword) {
      wx.showToast({ title: '请输入新密码', icon: 'none' });
      return;
    }

    if (newPassword.length < 6) {
      wx.showToast({ title: '新密码至少6位', icon: 'none' });
      return;
    }

    if (newPassword !== confirmPassword) {
      wx.showToast({ title: '两次密码不一致', icon: 'none' });
      return;
    }

    wx.showLoading({ title: '保存中...' });

    try {
      const res = await app.request({
        url: '/user/change-password',
        method: 'POST',
        data: {
          oldPassword: oldPassword,
          newPassword: newPassword
        }
      });

      wx.hideLoading();

      if (res.code === 200) {
        wx.showToast({
          title: '修改成功',
          icon: 'success'
        });

        // 修改成功后退出登录，让用户重新登录
        setTimeout(() => {
          app.clearLoginData();
          wx.redirectTo({
            url: '/pages/login/login'
          });
        }, 1500);
      }
    } catch (error) {
      wx.hideLoading();
      wx.showToast({
        title: error.message || '修改失败',
        icon: 'none'
      });
    }
  }
});