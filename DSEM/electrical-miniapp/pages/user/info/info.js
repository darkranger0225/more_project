const request = require('../../../utils/request.js');

Page({
  data: {
    userInfo: {}
  },

  onShow() {
    this.loadUserInfo();
  },

  loadUserInfo() {
    request.get('/api/user/info').then(res => {
      this.setData({
        userInfo: res.data
      });
    });
  },

  onNameInput(e) {
    this.setData({
      'userInfo.name': e.detail.value
    });
  },

  onPhoneInput(e) {
    this.setData({
      'userInfo.phone': e.detail.value
    });
  },

  onAgeInput(e) {
    this.setData({
      'userInfo.age': e.detail.value
    });
  },

  bindGenderChange(e) {
    this.setData({
      'userInfo.gender': parseInt(e.detail.value)
    });
  },

  // 手机号格式校验
  validatePhone(phone) {
    // 手机号正则：以1开头，第二位是3-9，后面9位数字
    const phoneRegex = /^1[3-9]\d{9}$/;
    return phoneRegex.test(phone);
  },

  saveUserInfo() {
    const { userInfo } = this.data;

    // 校验姓名
    if (!userInfo.name || userInfo.name.trim() === '') {
      wx.showToast({
        title: '请输入姓名',
        icon: 'none'
      });
      return;
    }

    // 校验手机号
    if (userInfo.phone) {
      if (!this.validatePhone(userInfo.phone)) {
        wx.showToast({
          title: '手机号格式不正确',
          icon: 'none'
        });
        return;
      }
    }

    // 校验年龄
    if (userInfo.age) {
      const age = parseInt(userInfo.age);
      if (isNaN(age) || age < 1 || age > 150) {
        wx.showToast({
          title: '请输入正确的年龄',
          icon: 'none'
        });
        return;
      }
      userInfo.age = age;
    }

    request.put('/api/user/update', userInfo).then(() => {
      wx.showToast({
        title: '保存成功',
        icon: 'success'
      });
    });
  }
});
