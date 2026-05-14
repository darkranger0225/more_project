const request = require('../../utils/request.js');

Page({
  data: {
    balance: '0.00',
    usageList: []
  },

  onShow() {
    this.loadData();
  },

  loadData() {
    this.loadBalance();
    this.loadUsageList();
  },

  loadBalance() {
    request.get('/api/electricity/balance').then(res => {
      this.setData({
        balance: res.data ? parseFloat(res.data).toFixed(2) : '0.00'
      });
    });
  },

  loadUsageList() {
    request.get('/api/electricity/list').then(res => {
      this.setData({
        usageList: res.data || []
      });
    });
  }
});
