Page({
  data: {
    historyList: []
  },

  onShow() {
    this.loadHistoryList();
  },

  loadHistoryList() {
    const app = getApp();
    const request = require('../../../utils/request.js');
    request.get('/api/payment/bill/list').then(res => {
      this.setData({
        historyList: res.data || []
      });
    });
  }
});
