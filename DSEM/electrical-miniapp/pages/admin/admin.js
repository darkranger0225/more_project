const request = require('../../utils/request.js');

Page({
  data: {
    userInfo: {},
    statistics: {},
    activeTab: 'electricity'
  },

  onShow() {
    this.loadUserInfo();
    this.loadStatistics();
  },

  loadUserInfo() {
    const userInfo = wx.getStorageSync('userInfo');
    if (userInfo) {
      this.setData({ userInfo });
    }
  },

  loadStatistics() {
    request.get('/api/admin/statistics').then(res => {
      this.setData({
        statistics: res.data
      });
    });
  },

  switchTab(e) {
    const tab = e.currentTarget.dataset.tab;
    this.setData({
      activeTab: tab
    });
  },

  goToStudentManage() {
    wx.navigateTo({
      url: '/pages/admin/student/student'
    });
  },

  goToElectricityManage() {
    wx.navigateTo({
      url: '/pages/admin/electricity/electricity'
    });
  },

  goToRepairManage() {
    wx.navigateTo({
      url: '/pages/admin/repair/repair'
    });
  },

  goToAnnouncementManage() {
    wx.navigateTo({
      url: '/pages/admin/announcement/announcement'
    });
  }
});
