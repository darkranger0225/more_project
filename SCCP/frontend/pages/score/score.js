const app = getApp();

Page({
  data: {
    role: '',
    scoreList: []
  },

  onShow() {
    const role = app.globalData.role || wx.getStorageSync('userInfo')?.role;
    this.setData({ role });
    this.loadScoreList();
  },

  // 教师端：跳转到录入成绩页面
  goToAddScore() {
    wx.navigateTo({
      url: '/pages/score/add'
    });
  },

  // 查看成绩详情
  viewScoreDetail(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/score/detail?id=${id}`
    });
  },

  async loadScoreList() {
    try {
      const res = await app.request({
        url: '/score/list'
      });

      if (res.code === 200) {
        this.setData({
          scoreList: res.data
        });
      }
    } catch (error) {
      // 加载成绩失败
    }
  },

  onPullDownRefresh() {
    this.loadScoreList().then(() => {
      wx.stopPullDownRefresh();
    });
  }
});