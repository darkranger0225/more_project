const app = getApp();

Page({
  data: {
    score: {}
  },

  onLoad(options) {
    if (options.id) {
      this.loadScoreDetail(options.id);
    }
  },

  async loadScoreDetail(id) {
    try {
      const res = await app.request({
        url: `/score/${id}`
      });

      if (res.code === 200) {
        this.setData({
          score: res.data
        });
      }
    } catch (error) {
      wx.showToast({
        title: '加载成绩详情失败',
        icon: 'none'
      });
    }
  }
});
