// pages/me/me.js
const auth = require('../../utils/auth');
const app = getApp();
Page({
  // 页面的初始数据
  data: {
    isLoggedIn: false, // 登录状态
    userName: '', // 用户姓名
    isAdmin: false, // 是否为管理员
  },

  // 页面加载时检查登录状态
  onShow() {
    this.checkLoginStatus();
  },

  // 检查登录状态并更新数据
  checkLoginStatus() {
    const token = wx.getStorageSync('token');
    const name = wx.getStorageSync('name');
    const isAdmin = wx.getStorageSync('isAdmin');

    if (token && name) {
      this.setData({
        isLoggedIn: true,
        userName: name,
        isAdmin: isAdmin
      });
    } else {
      this.setData({
        isLoggedIn: false,
        userName: '',
        isAdmin: false
      });
      this.showModalExample();
    }
  },

  // 跳转逻辑：根据登录状态和管理员身份决定跳转页面
  toLoginPage() {
    if (this.data.isLoggedIn) {
      // 如果已登录，根据是否是管理员决定跳转页面
      if (this.data.isAdmin) {
        // 如果是管理员，跳转到管理员页面
        wx.navigateTo({
          url: '/pages/adminPanel/adminPanel',
        });
      } else {
        // 如果是普通用户，跳转到 Preferences 页面
        wx.navigateTo({
          url: '/pages/Preferences/Preferences',
        });
      }
    } else {
      // 如果未登录，跳转到登录页
      wx.navigateTo({
        url: '/pages/login/login',
      });
    }
  },
  toAccountManagementPage() {
    wx.navigateTo({
      url: '/pages/accountManagement/accountManagement', // 新页面路径
    });
  },
  // 跳转到反馈页面
  toFeedbackPage() {
    if (!this.data.isLoggedIn) {
      wx.showModal({
        title: '提示',
        content: '请先登录后再提交反馈！',
        showCancel: false,
        confirmText: '确定',
        success: (res) => {
          if (res.confirm) {
            wx.navigateTo({
              url: '/pages/login/login'
            });
          }
        }
      });
      return;
    }
    // 已登录，跳转到反馈页面
    wx.navigateTo({
      url: '/pages/feedback/feedback',
    });
  },
  showModalExample() {
    wx.showModal({
      title: '提示', // 标题
      content: '请先登录以继续操作！', // 内容
      showCancel: false, // 隐藏取消按钮
      confirmText: '确定', // 确认按钮文字
      success: (res) => { // 回调函数
        if (res.confirm) {
          console.log('用户点击了确定');
          // 用户点击确定后跳转到登录页面
          wx.navigateTo({
            url: '/pages/login/login'
          });
        }
      }
    });
  },
});