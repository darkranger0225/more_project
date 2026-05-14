const app = getApp();

Page({
  data: {
    admins: [],
    newAdmin: {
      username: '',
      phone: '',
      password: '123456'
    },
    showAddForm: false,
    showStats: false,
    showAdminList: false,
    showUserInfoSearch: false,
    userIdToSearch: '',
    userInfo: null,
    preferences: null,
    allowIndexing: true,
    statsTitle: '',
    statsData: [],
    loading: false,
    errorMsg: '',
    successMsg: ''
  },

  onLoad: function (options) {
    this.fetchAdmins();
  },
  
  onPullDownRefresh: function() {
    this.fetchAdmins();
    wx.stopPullDownRefresh();
  },

  // 获取所有管理员列表
  fetchAdmins: function () {
    this.setData({ loading: true });
    wx.request({
      url: app.globalData.baseUrl + '/admin/list',
      method: 'GET',
      header: {
        'Authorization': `Bearer ${wx.getStorageSync('token')}`
      },
      success: (res) => {
        if (res.statusCode === 200) {
          this.setData({
            admins: res.data,
            errorMsg: ''
          });
        } else {
          this.setData({
            errorMsg: '获取管理员列表失败: ' + (res.data.message || '未知错误')
          });
        }
      },
      fail: (err) => {
        this.setData({
          errorMsg: '网络请求失败: ' + err.errMsg
        });
      },
      complete: () => {
        this.setData({ loading: false });
      }
    });
  },

  // 显示/隐藏添加管理员表单
  toggleAddForm: function () {
    this.setData({
      showAddForm: !this.data.showAddForm,
      showStats: false,
      showAdminList: false,
      showUserInfoSearch: false,
      userInfo: null,
      preferences: null,
      newAdmin: {
        username: '',
        phone: '',
        password: '123456'
      },
      errorMsg: '',
      successMsg: ''
    });
  },
  
  // 显示/隐藏管理员列表
  toggleAdminList: function () {
    this.setData({
      showAdminList: !this.data.showAdminList,
      showAddForm: false,
      showStats: false,
      showUserInfoSearch: false,
      userInfo: null,
      preferences: null,
      errorMsg: '',
      successMsg: ''
    });
    
    if (this.data.showAdminList) {
      this.fetchAdmins();
    }
  },
  
  // 显示用户信息查询表单
  showUserInfoSearch: function () {
    this.setData({
      showUserInfoSearch: true,
      showAddForm: false,
      showStats: false,
      showAdminList: false,
      userInfo: null,
      preferences: null,
      userIdToSearch: '',
      errorMsg: '',
      successMsg: ''
    });
  },
  
  // 隐藏用户信息查询表单
  hideUserInfoSearch: function () {
    this.setData({
      showUserInfoSearch: false,
      userInfo: null,
      preferences: null,
      errorMsg: '',
      successMsg: ''
    });
  },
  
  // 用户ID输入框变化
  userIdInputChange: function (e) {
    this.setData({
      userIdToSearch: e.detail.value
    });
  },
  
  // 查询用户信息
  searchUserInfo: function () {
    if (!this.data.userIdToSearch) {
      this.setData({ errorMsg: '请输入用户ID' });
      return;
    }
    
    this.setData({ loading: true, errorMsg: '', successMsg: '' });
    
    wx.request({
      url: app.globalData.baseUrl + `/admin/user-info/${this.data.userIdToSearch}`,
      method: 'GET',
      header: {
        'Authorization': `Bearer ${wx.getStorageSync('token')}`
      },
      success: (res) => {
        if (res.statusCode === 200 && res.data.success) {
          this.setData({
            userInfo: res.data.userInfo,
            preferences: res.data.preferences,
            allowIndexing: res.data.allowIndexing,
            successMsg: '查询用户信息成功'
          });
        } else {
          this.setData({
            errorMsg: res.data.message || '未知错误',
            userInfo: null,
            preferences: null
          });
        }
      },
      fail: (err) => {
        this.setData({
          errorMsg: '网络请求失败: ' + err.errMsg,
          userInfo: null,
          preferences: null
        });
      },
      complete: () => {
        this.setData({ loading: false });
      }
    });
  },
  
  // 重置用户密码
  resetUserPassword: function () {
    if (!this.data.userIdToSearch) {
      this.setData({ errorMsg: '请输入用户ID' });
      return;
    }
    
    wx.showModal({
      title: '确认重置',
      content: `确定要重置用户ID为 ${this.data.userIdToSearch} 的密码为123456吗？`,
      success: (res) => {
        if (res.confirm) {
          this.setData({ loading: true, errorMsg: '', successMsg: '' });
          
          wx.request({
            url: app.globalData.baseUrl + `/admin/reset-user-password/${this.data.userIdToSearch}`,
            method: 'POST',
            header: {
              'Authorization': `Bearer ${wx.getStorageSync('token')}`
            },
            success: (res) => {
              if (res.statusCode === 200 && res.data.success) {
                this.setData({
                  successMsg: res.data.message || '用户密码已重置为123456'
                });
              } else {
                this.setData({
                  errorMsg: res.data.message || '未知错误'
                });
              }
            },
            fail: (err) => {
              this.setData({
                errorMsg: '网络请求失败: ' + err.errMsg
              });
            },
            complete: () => {
              this.setData({ loading: false });
            }
          });
        }
      }
    });
  },

  // 删除用户
  deleteUser: function () {
    if (!this.data.userIdToSearch) {
      this.setData({ errorMsg: '请输入用户ID' });
      return;
    }
    
    wx.showModal({
      title: '确认删除',
      content: `确定要删除用户ID为 ${this.data.userIdToSearch} 的账户吗？此操作不可恢复！`,
      success: (res) => {
        if (res.confirm) {
          this.setData({ loading: true, errorMsg: '', successMsg: '' });
          
          wx.request({
            url: app.globalData.baseUrl + `/admin/user/${this.data.userIdToSearch}`,
            method: 'DELETE',
            header: {
              'Authorization': `Bearer ${wx.getStorageSync('token')}`
            },
            success: (res) => {
              if (res.statusCode === 200 && res.data.success) {
                this.setData({
                  successMsg: res.data.message || '用户删除成功',
                  userInfo: null,
                  preferences: null
                });
              } else {
                this.setData({
                  errorMsg: res.data.message || '未知错误'
                });
              }
            },
            fail: (err) => {
              this.setData({
                errorMsg: '网络请求失败: ' + err.errMsg
              });
            },
            complete: () => {
              this.setData({ loading: false });
            }
          });
        }
      }
    });
  },

  // 输入框变化处理
  inputChange: function (e) {
    const { field } = e.currentTarget.dataset;
    const { newAdmin } = this.data;
    newAdmin[field] = e.detail.value;
    this.setData({ newAdmin });
  },

  // 添加管理员
  addAdmin: function () {
    const { username, phone, password } = this.data.newAdmin;
    
    // 简单验证
    if (!username || !phone) {
      this.setData({ errorMsg: '用户名和手机号不能为空' });
      return;
    }

    this.setData({ loading: true, errorMsg: '', successMsg: '' });

    wx.request({
      url: app.globalData.baseUrl + '/admin/add',
      method: 'POST',
      header: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${wx.getStorageSync('token')}`
      },
      data: this.data.newAdmin,
      success: (res) => {
        if (res.statusCode === 200 && res.data.success) {
          this.setData({
            successMsg: '管理员添加成功',
            showAddForm: false,
            newAdmin: {
              username: '',
              phone: '',
              password: '123456'
            }
          });
          this.fetchAdmins(); // 刷新列表
        } else {
          this.setData({
            errorMsg: res.data.message || '未知错误'
          });
        }
      },
      fail: (err) => {
        this.setData({
          errorMsg: '网络请求失败: ' + err.errMsg
        });
      },
      complete: () => {
        this.setData({ loading: false });
      }
    });
  },

  // 删除管理员
  deleteAdmin: function (e) {
    const { id, username } = e.currentTarget.dataset;
    
    wx.showModal({
      title: '确认删除',
      content: `确定要删除管理员 "${username}" 吗？`,
      success: (res) => {
        if (res.confirm) {
          this.setData({ loading: true });
          
          wx.request({
            url: app.globalData.baseUrl + `/admin/${id}`,
            method: 'DELETE',
            header: {
              'Authorization': `Bearer ${wx.getStorageSync('token')}`
            },
            success: (res) => {
              if (res.statusCode === 200 && res.data.success) {
                this.setData({
                  successMsg: '管理员删除成功'
                });
                this.fetchAdmins(); // 刷新列表
              } else {
                this.setData({
                  errorMsg: res.data.message || '未知错误'
                });
              }
            },
            fail: (err) => {
              this.setData({
                errorMsg: '网络请求失败: ' + err.errMsg
              });
            },
            complete: () => {
              this.setData({ loading: false });
            }
          });
        }
      }
    });
  },

  // 重置密码工具
  showResetOptions: function() {
    wx.showActionSheet({
      itemList: ['重置所有用户密码', '重置所有管理员密码', '根据ID重置用户密码'],
      success: (res) => {
        if (res.tapIndex === 0) {
          this.resetPasswords('user');
        } else if (res.tapIndex === 1) {
          this.resetPasswords('admin');
        } else if (res.tapIndex === 2) {
          this.showUserInfoSearch();
        }
      }
    });
  },

  // 重置密码
  resetPasswords: function(type) {
    wx.showModal({
      title: '确认重置',
      content: `确定要重置所有${type === 'user' ? '用户' : '管理员'}密码为123456吗？`,
      success: (res) => {
        if (res.confirm) {
          this.setData({ loading: true, errorMsg: '', successMsg: '' });
          
          // 调用后端API重置密码
          const url = app.globalData.baseUrl + `/admin/reset-${type}-passwords`;
          
          wx.request({
            url: url,
            method: 'POST',
            header: {
              'Authorization': `Bearer ${wx.getStorageSync('token')}`
            },
            success: (res) => {
              if (res.statusCode === 200 && res.data.success) {
                this.setData({
                  successMsg: res.data.message || `所有${type === 'user' ? '用户' : '管理员'}密码已重置为123456`
                });
              } else {
                this.setData({
                  errorMsg: res.data.message || '未知错误'
                });
              }
            },
            fail: (err) => {
              this.setData({
                errorMsg: '网络请求失败: ' + err.errMsg
              });
            },
            complete: () => {
              this.setData({ loading: false });
            }
          });
        }
      }
    });
  },
  
  // 显示匹配统计
  showMatchingStats: function() {
    this.setData({ 
      loading: true, 
      showAddForm: false,
      showStats: true,
      showAdminList: false,
      showUserInfoSearch: false,
      userInfo: null,
      preferences: null,
      statsTitle: '匹配数据统计',
      errorMsg: '', 
      successMsg: '' 
    });
    
    // 调用后端API获取匹配统计数据
    wx.request({
      url: app.globalData.baseUrl + '/admin/stats/matching',
      method: 'GET',
      header: {
        'Authorization': `Bearer ${wx.getStorageSync('token')}`
      },
      success: (res) => {
        if (res.statusCode === 200) {
          this.setData({
            statsData: res.data
          });
        } else {
          this.setData({
            errorMsg: res.data.message || '未知错误'
          });
        }
      },
      fail: (err) => {
        this.setData({
          errorMsg: '网络请求失败: ' + err.errMsg
        });
      },
      complete: () => {
        this.setData({ loading: false });
      }
    });
  },
  
  // 显示用户统计
  showUserStats: function() {
    this.setData({ 
      loading: true, 
      showAddForm: false,
      showStats: true,
      showAdminList: false,
      showUserInfoSearch: false,
      userInfo: null,
      preferences: null,
      statsTitle: '用户行为统计',
      errorMsg: '', 
      successMsg: '' 
    });
    
    // 调用后端API获取用户行为统计数据
    wx.request({
      url: app.globalData.baseUrl + '/admin/stats/user-actions',
      method: 'GET',
      header: {
        'Authorization': `Bearer ${wx.getStorageSync('token')}`
      },
      success: (res) => {
        if (res.statusCode === 200) {
          this.setData({
            statsData: res.data
          });
        } else {
          this.setData({
            errorMsg: res.data.message || '未知错误'
          });
        }
      },
      fail: (err) => {
        this.setData({
          errorMsg: '网络请求失败: ' + err.errMsg
        });
      },
      complete: () => {
        this.setData({ loading: false });
      }
    });
  },

  // 跳转到反馈管理页面
  toFeedbackManagement: function() {
    wx.navigateTo({
      url: '/pages/adminFeedback/adminFeedback',
    });
  }
}); 