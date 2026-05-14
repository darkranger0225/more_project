const app = getApp();

Page({
  data: {
    userInfo: {},
    role: '',
    roleText: ''
  },

  onShow() {
    this.loadUserInfo();
  },

  loadUserInfo() {
    const userInfo = app.globalData.userInfo || wx.getStorageSync('userInfo');
    const role = app.globalData.role || userInfo?.role;
    
    let roleText = '';
    switch(role) {
      case 'PARENT':
        roleText = '家长';
        break;
      case 'TEACHER':
        roleText = '教师';
        break;
      case 'ADMIN':
        roleText = '管理员';
        break;
    }

    this.setData({
      userInfo: userInfo || {},
      role,
      roleText
    });
  },

  goToMyStudents() {
    wx.navigateTo({
      url: '/pages/student/my-students/my-students'
    });
  },

  goToMyClasses() {
    wx.navigateTo({
      url: '/pages/class/my-classes/my-classes'
    });
  },

  // 管理员功能
  goToUserManage() {
    wx.navigateTo({
      url: '/pages/admin/user-manage/user-manage'
    });
  },

  goToClassManage() {
    wx.navigateTo({
      url: '/pages/admin/class-manage/class-manage'
    });
  },

  goToStudentManage() {
    wx.navigateTo({
      url: '/pages/admin/student-manage/student-manage'
    });
  },

  changePassword() {
    wx.navigateTo({
      url: '/pages/profile/change-password/change-password'
    });
  },

  clearCache() {
    wx.showModal({
      title: '提示',
      content: '确定要清除缓存吗？',
      success: (res) => {
        if (res.confirm) {
          wx.clearStorage();
          wx.showToast({
            title: '清除成功',
            icon: 'success'
          });
        }
      }
    });
  },

  aboutUs() {
    wx.showModal({
      title: '关于我们',
      content: '家校沟通平台 v1.0.0\n连接学校与家庭，构建高效便捷的家校共育环境。',
      showCancel: false
    });
  },

  logout() {
    wx.showModal({
      title: '提示',
      content: '确定要退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          app.clearLoginData();
          wx.redirectTo({
            url: '/pages/login/login'
          });
        }
      }
    });
  }
});