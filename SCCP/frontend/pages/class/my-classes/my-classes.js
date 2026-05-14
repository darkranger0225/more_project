const app = getApp();

Page({
  data: {
    classList: []
  },

  onShow() {
    this.loadClassList();
  },

  async loadClassList() {
    try {
      const res = await app.request({
        url: '/class/my-classes'
      });

      if (res.code === 200) {
        // 获取每个班级的学生数量
        const classList = await Promise.all(
          res.data.map(async (cls) => {
            try {
              const studentRes = await app.request({
                url: `/student/class/${cls.id}`
              });
              return {
                ...cls,
                studentCount: studentRes.code === 200 ? studentRes.data.length : 0
              };
            } catch (error) {
              return {
                ...cls,
                studentCount: 0
              };
            }
          })
        );

        this.setData({ classList });
      }
    } catch (error) {
      wx.showToast({
        title: '加载班级列表失败',
        icon: 'none'
      });
    }
  },

  viewClassDetail(e) {
    const classId = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/class/class-detail/class-detail?id=${classId}`
    });
  },

  onPullDownRefresh() {
    this.loadClassList().then(() => {
      wx.stopPullDownRefresh();
    });
  }
});
