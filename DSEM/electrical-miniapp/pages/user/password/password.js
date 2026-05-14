const request = require('../../../utils/request.js');

Page({
  data: {
    oldPassword: '',
    newPassword: '',
    confirmPassword: ''
  },

  onOldPasswordInput(e) {
    this.setData({
      oldPassword: e.detail.value
    });
  },

  onNewPasswordInput(e) {
    this.setData({
      newPassword: e.detail.value
    });
  },

  onConfirmPasswordInput(e) {
    this.setData({
      confirmPassword: e.detail.value
    });
  },

  updatePassword() {
    const { oldPassword, newPassword, confirmPassword } = this.data;
    
    if (!oldPassword) {
      wx.showToast({
        title: '请输入旧密码',
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
    
    if (newPassword !== confirmPassword) {
      wx.showToast({
        title: '两次输入的密码不一致',
        icon: 'none'
      });
      return;
    }
    
    request.put('/api/user/password', {
      oldPassword,
      newPassword
    }).then(() => {
      wx.showToast({
        title: '修改成功',
        icon: 'success'
      });
      setTimeout(() => {
        wx.navigateBack();
      }, 1500);
    });
  }
});
