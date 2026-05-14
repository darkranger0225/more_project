// pages/login/login.js
const app = getApp();
Page({
  data: {
    phone: '',
    password: ''
  },

  // 绑定输入框事件
  bindPhoneInput(e) {
    this.setData({ phone: e.detail.value });
  },

  bindPasswordInput(e) {
    this.setData({ password: e.detail.value });
  },

  // 显示提示消息
  showToast(message, icon = 'none') {
    wx.showToast({
      title: message,
      icon: icon
    });
  },

  // 登录方法
  onLogin() {
    const { phone, password } = this.data;

    // 前端校验
    if (!phone || !password) {
      this.showToast('手机号和密码不能为空');
      return;
    }

    const phoneRegex = /^1[3-9]\d{9}$/; // 手机号正则表达式
    if (!phoneRegex.test(phone)) {
      this.showToast('请输入正确的手机号');
      return;
    }

    if (password.length < 6) {
      this.showToast('密码长度不能小于6位');
      return;
    }

    // 显示加载提示
    wx.showLoading({ title: '登录中...' });

    // 保存当前页面上下文
    const that = this;

    // 发送登录请求
    wx.request({
      url: app.globalData.baseUrl + '/user/login',
      method: 'POST',
      header: { 'content-type': 'application/json' },
      data: { phone, password },
      success(res) {
        console.log('Response:', res);
        wx.hideLoading();

        if (res.statusCode === 200) {
          const response = res.data;

          if (response.message === "登录成功") {
            // 打印登录响应数据
            console.log('登录响应数据：', response);
            
            that.showToast('登录成功', 'success');

            // 保存 token 和 isAdmin 到本地存储
            wx.setStorageSync('token', response.token);
            wx.setStorageSync('isAdmin', response.isAdmin);
            wx.setStorageSync('userId', response.userId);
            wx.setStorageSync('name', response.name);
            wx.setStorageSync('phone', response.phone);
            
            // 如果是管理员，保存管理员ID
            if (response.isAdmin && response.adminId) {
              wx.setStorageSync('adminId', response.adminId);
            }
            // 使用reLaunch关闭所有页面，打开首页
            wx.reLaunch({
              url: '/pages/home/home'
            });
          } else {
            that.showToast(response.message || '登录失败');
          }
        } else if (res.statusCode === 401) {
          const response = res.data;
          that.showToast(response.message || '账号或密码错误');
        } else {
          that.showToast('服务器错误，请稍后再试');
        }
      },
      fail(err) {
        console.error('Request Failed:', err);
        wx.hideLoading();
        that.showToast('请求失败');
      }
    });
  },

  // 页面加载时检查登录状态
  // onLoad() {
  //   const token = wx.getStorageSync('token');
  //   if (token) {
  //     // 如果有token，直接跳转到首页
  //     wx.switchTab({
  //       url: '/pages/home/home'
  //     });
  //   }
  // },

  // 跳转到注册页面
  toRegisterPage() {
    wx.navigateTo({
      url: '/pages/register/register'
    });
  },
  
  // 跳转到忘记密码页面
  toForgotPasswordPage() {
    wx.navigateTo({
      url: '/pages/forgot-password/forgot-password'
    });
  }
});