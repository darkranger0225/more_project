const app = getApp();

Page({
  data: {
    username: '',
    password: '',
    confirmPassword: '',
    realName: '',
    phone: '',
    roleArray: ['家长', '教师'],
    roleIndex: 0,
    role: 'PARENT'
  },

  onUsernameInput(e) {
    this.setData({ username: e.detail.value });
  },

  onPasswordInput(e) {
    this.setData({ password: e.detail.value });
  },

  onConfirmPasswordInput(e) {
    this.setData({ confirmPassword: e.detail.value });
  },

  onRealNameInput(e) {
    this.setData({ realName: e.detail.value });
  },

  onPhoneInput(e) {
    this.setData({ phone: e.detail.value });
  },

  onRoleChange(e) {
    const index = e.detail.value;
    const role = index === '0' ? 'PARENT' : 'TEACHER';
    this.setData({
      roleIndex: index,
      role: role
    });
  },

  async onRegister() {
    const { username, password, confirmPassword, realName, phone, role } = this.data;

    // 表单验证
    if (!username.trim()) {
      wx.showToast({ title: '请输入用户名', icon: 'none' });
      return;
    }

    if (username.length < 3) {
      wx.showToast({ title: '用户名至少3位', icon: 'none' });
      return;
    }

    if (!password) {
      wx.showToast({ title: '请输入密码', icon: 'none' });
      return;
    }

    if (password.length < 6) {
      wx.showToast({ title: '密码至少6位', icon: 'none' });
      return;
    }

    if (password !== confirmPassword) {
      wx.showToast({ title: '两次密码不一致', icon: 'none' });
      return;
    }

    if (!realName.trim()) {
      wx.showToast({ title: '请输入真实姓名', icon: 'none' });
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

    wx.showLoading({ title: '注册中...' });

    try {
      const res = await app.request({
        url: '/auth/register',
        method: 'POST',
        data: {
          username: username.trim(),
          password: password,
          realName: realName.trim(),
          phone: phone.trim(),
          role: role
        }
      });

      wx.hideLoading();

      if (res.code === 200) {
        wx.showToast({
          title: '注册成功',
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
        title: error.message || '注册失败',
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