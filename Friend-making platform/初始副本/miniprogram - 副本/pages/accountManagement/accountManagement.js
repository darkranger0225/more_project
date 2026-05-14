// pages/accountManagement/accountManagement.js
const app = getApp();
Page({
  data: {
    allowIndexing: true, // 默认允许索引
  },

  onLoad: function () {
    console.log('账号管理页面加载');
    this.fetchIndexingPermission();
  },

  // 获取用户当前的索引权限状态
  fetchIndexingPermission: function() {
    const userId = wx.getStorageSync('userId');
    wx.request({
      url: app.globalData.baseUrl + '/user/indexing-permission',
      method: 'GET',
      header: {
        'content-type': 'application/json'
      },
      data: {
        userId: userId
      },
      success: (res) => {
        if (res.data.success) {
          this.setData({
            allowIndexing: res.data.allowIndexing
          });
        } else {
          console.error('获取索引权限失败:', res.data.message);
        }
      },
      fail: (error) => {
        console.error('请求失败:', error);
      }
    });
  },

  // 处理索引权限变更
  handleIndexingChange: function() {
    const userId = wx.getStorageSync('userId');
    
    wx.showActionSheet({
      itemList: ['允许被索引', '禁止被索引'],
      success: (res) => {
        const allowIndexing = res.tapIndex === 0;
        
        wx.request({
          url: app.globalData.baseUrl + '/user/indexing-permission',
          method: 'POST',
          header: {
            'content-type': 'application/json'
          },
          data: JSON.stringify({
            userId: userId,
            allowIndexing: allowIndexing
          }),
          success: (res) => {
            if (res.data.success) {
              this.setData({
                allowIndexing: allowIndexing
              });
              wx.showToast({
                title: allowIndexing ? '已允许被索引' : '已禁止被索引',
                icon: 'success'
              });
            } else {
              wx.showToast({
                title: res.data.message || '设置失败',
                icon: 'error'
              });
            }
          },
          fail: () => {
            wx.showToast({
              title: '网络错误',
              icon: 'error'
            });
          }
        });
      }
    });
  },

  // 处理修改用户信息的事件
  handleModifyUserInfo: function () {
    console.log('用户点击了修改用户信息');

    wx.navigateTo({
      url: '/pages/user-edit/user-edit',
    });
  },

  // 处理退出登录的事件
  handleLogOut: function () {
    console.log('用户点击了退出登录');

    wx.showModal({
      title: '确认退出登录',
      content: '你确定要退出登录吗？',
      success: function (res) {
        if (res.confirm) {
          console.log('用户点击了确认退出');
          // 清除所有本地存储数据
          wx.clearStorageSync();
          // 使用reLaunch关闭所有页面，打开登录页
          wx.reLaunch({
            url: '/pages/login/login',
          });
        } else {
          console.log('用户点击了取消退出');
        }
      },
    });
  },

  // 处理注销账户的事件
  handleDeactivateAccount: function () {
    console.log('用户点击了注销账户');

    wx.showModal({
      title: '警告',
      content: '账户注销后，所有数据将被永久删除且无法恢复。确认继续吗？',
      confirmText: '确认注销',
      confirmColor: '#ff4d4f',
      success: (res) => {
        if (res.confirm) {
          // 用户确认注销，弹出密码验证框
          wx.showModal({
            title: '密码验证',
            editable: true,
            placeholderText: '请输入密码',
            content: '',
            success: (res2) => {
              if (res2.confirm && res2.content) {
                const userId = wx.getStorageSync('userId');
                const password = res2.content;
                
                // 调用后端接口进行注销
                wx.showLoading({
                  title: '正在处理',
                });
                
                wx.request({
                  url: app.globalData.baseUrl + `/user/deactivate/${userId}?password=${encodeURIComponent(password)}`,
                  method: 'DELETE',
                  success: (res3) => {
                    wx.hideLoading();
                    if (res3.data.success) {
                      wx.showToast({
                        title: '账户已注销',
                        icon: 'success',
                        duration: 2000,
                        success: () => {
                          // 清除所有本地存储数据
                          wx.clearStorageSync();
                          // 使用reLaunch关闭所有页面，打开登录页
                          setTimeout(() => {
                            wx.reLaunch({
                              url: '/pages/login/login',
                            });
                          }, 2000);
                        }
                      });
                    } else {
                      wx.showModal({
                        title: '注销失败',
                        content: res3.data.message || '操作失败，请重试',
                        showCancel: false
                      });
                    }
                  },
                  fail: () => {
                    wx.hideLoading();
                    wx.showModal({
                      title: '网络错误',
                      content: '请检查网络连接后重试',
                      showCancel: false
                    });
                  }
                });
              }
            }
          });
        }
      }
    });
  },

  onPullDownRefresh: function () {
    console.log('页面下拉刷新');
    wx.stopPullDownRefresh();
  },
});