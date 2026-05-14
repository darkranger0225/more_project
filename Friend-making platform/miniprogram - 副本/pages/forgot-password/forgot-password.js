const app = getApp();

Page({
  data: {
    name: '',
    phone: '',
    birthday: '',
    newPassword: '',
    confirmPassword: '',
    phoneError: '',
    passwordError: ''
  },

  // 绑定输入框事件
  bindInput(e) {
    const field = e.currentTarget.dataset.field;
    const value = e.detail.value;
    this.setData({
      [field]: value
    });

    // 清除错误提示
    if (field === 'phone') {
      this.setData({ phoneError: '' });
    } else if (field === 'newPassword' || field === 'confirmPassword') {
      this.setData({ passwordError: '' });
    }
  },

  // 绑定生日选择器
  onBirthdayChange(e) {
    this.setData({
      birthday: e.detail.value
    });
  },

  // 验证表单
  validateForm() {
    const { name, phone, birthday, newPassword, confirmPassword } = this.data;
    let isValid = true;

    // 验证手机号
    const phoneRegex = /^1[3-9]\d{9}$/;
    if (!phoneRegex.test(phone)) {
      this.setData({
        phoneError: '请输入正确的手机号'
      });
      isValid = false;
    }

    // 验证密码
    if (newPassword.length < 6) {
      this.setData({
        passwordError: '密码长度不能小于6位'
      });
      isValid = false;
      return isValid;
    }

    // 验证确认密码
    if (newPassword !== confirmPassword) {
      this.setData({
        passwordError: '两次输入的密码不一致'
      });
      isValid = false;
    }

    // 验证必填字段
    if (!name || !phone || !newPassword || !confirmPassword) {
      wx.showToast({
        title: '请填写所有必填项',
        icon: 'none'
      });
      isValid = false;
    }

    return isValid;
  },

  // 重置密码
  resetPassword() {
    if (!this.validateForm()) {
      return;
    }

    const { name, phone, birthday, newPassword } = this.data;

    // 显示加载提示
    wx.showLoading({ title: '提交中...' });

    // 发送重置密码请求
    wx.request({
      url: app.globalData.baseUrl + '/user/reset-password',
      method: 'POST',
      header: { 'content-type': 'application/json' },
      data: { 
        name, 
        phone, 
        birthday, 
        newPassword 
      },
      success: (res) => {
        wx.hideLoading();
        if (res.statusCode === 200) {
          wx.showToast({
            title: '密码重置成功',
            icon: 'success',
            duration: 2000,
            success: () => {
              // 2秒后返回登录页
              setTimeout(() => {
                wx.navigateBack({
                  delta: 1
                });
              }, 2000);
            }
          });
        } else {
          wx.showToast({
            title: res.data.message || '密码重置失败',
            icon: 'none'
          });
        }
      },
      fail: (err) => {
        wx.hideLoading();
        wx.showToast({
          title: '请求失败，请检查网络',
          icon: 'none'
        });
        console.error('重置密码请求失败:', err);
      }
    });
  },
  
  // 返回登录页
  navigateBack() {
    wx.navigateBack({
      delta: 1
    });
  }
}); 