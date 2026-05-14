const app = getApp();

Page({
  data: {
    classId: null,
    classInfo: {},
    studentList: []
  },

  onLoad(options) {
    if (options.id) {
      this.setData({ classId: options.id });
      this.loadClassDetail(options.id);
      this.loadStudentList(options.id);
    }
  },

  async loadClassDetail(classId) {
    try {
      const res = await app.request({
        url: `/class/${classId}`
      });

      if (res.code === 200) {
        this.setData({
          classInfo: res.data
        });
      }
    } catch (error) {
      wx.showToast({
        title: '加载班级信息失败',
        icon: 'none'
      });
    }
  },

  async loadStudentList(classId) {
    try {
      const res = await app.request({
        url: `/student/class/${classId}`
      });

      if (res.code === 200) {
        this.setData({
          studentList: res.data
        });
      }
    } catch (error) {
      wx.showToast({
        title: '加载学生列表失败',
        icon: 'none'
      });
    }
  },

  onPullDownRefresh() {
    const { classId } = this.data;
    Promise.all([
      this.loadClassDetail(classId),
      this.loadStudentList(classId)
    ]).then(() => {
      wx.stopPullDownRefresh();
    });
  }
});
