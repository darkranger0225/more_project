const request = require('../../../utils/request.js');

Page({
  data: {
    account: '',
    phone: '',
    newPassword: '',
    confirmPassword: ''
  },

  handleSubmit() {
    const { account, phone, newPassword, confirmPassword } = this.data;

    if (!account) {
      wx.showToast({
        title: '请输入账号',
        icon: 'none'
      });
      return;
    }

    if (!phone) {
      wx.showToast({
        title: '请输入手机号',
        icon: 'none'
      });
      return;
    }

    if (!newPassword) {
      wx.showToast({
        title: '请输入新密码',
        icon: 'none'
      });
      return;
    }

    if (newPassword.length < 6) {
      wx.showToast({
        title: '新密码至少6位',
        icon: 'none'
      });
      return;
    }

    if (newPassword !== confirmPassword) {
      wx.showToast({
        title: '两次输入的密码不一致',
        icon: 'none'
      });
      return;
    }

    request.post('/api/user/reset-password', {
      account,
      phone,
      newPassword
    }).then(res => {
      wx.showToast({
        title: '密码重置成功',
        icon: 'success'
      });
      
      setTimeout(() => {
        wx.navigateBack();
      }, 1000);
    }).catch(err => {
      console.error('密码重置失败:', err);
    });
  },

  goToLogin() {
    wx.navigateBack();
  }
});