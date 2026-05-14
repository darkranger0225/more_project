const request = require('../../utils/request.js');

Page({
  data: {
    repairList: [],
    statusText: {
      0: '待处理',
      1: '处理中',
      2: '已完成'
    }
  },

  onShow() {
    this.loadRepairList();
  },

  loadRepairList() {
    request.get('/api/repair/list').then(res => {
      this.setData({
        repairList: res.data || []
      });
    });
  },

  goToAddRepair() {
    wx.navigateTo({
      url: '/pages/repair/add/add'
    });
  },

  goToDetail(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: '/pages/repair/detail/detail?id=' + id
    });
  }
});
