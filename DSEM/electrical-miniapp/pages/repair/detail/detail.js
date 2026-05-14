const request = require('../../../utils/request.js');

Page({
  data: {
    repairDetail: {},
    statusText: {
      0: '待处理',
      1: '处理中',
      2: '已完成'
    }
  },

  onLoad(options) {
    if (options.id) {
      this.loadRepairDetail(options.id);
    }
  },

  loadRepairDetail(id) {
    request.get('/api/repair/detail/' + id).then(res => {
      this.setData({
        repairDetail: res.data
      });
    });
  }
});
