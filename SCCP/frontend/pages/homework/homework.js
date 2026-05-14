const app = getApp();

Page({
  data: {
    role: '',
    homeworkList: []
  },

  onShow() {
    const role = app.globalData.role || wx.getStorageSync('userInfo')?.role;
    this.setData({ role });
    this.loadHomeworkList();
  },

  // 加载作业列表
  async loadHomeworkList() {
    try {
      const res = await app.request({
        url: '/homework/list'
      });

      if (res.code === 200) {
        const homeworkList = res.data.map(item => {
          const deadline = new Date(item.deadline);
          const now = new Date();
          return {
            ...item,
            deadline: this.formatDateTime(item.deadline),
            isExpired: deadline < now
          };
        });

        this.setData({
          homeworkList
        });
      }
    } catch (error) {
      // 加载作业列表失败
    }
  },

  // 格式化日期时间
  formatDateTime(dateTimeStr) {
    if (!dateTimeStr) return '';
    const date = new Date(dateTimeStr);
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');
    return `${month}-${day} ${hours}:${minutes}`;
  },

  // 查看作业详情
  viewHomeworkDetail(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/homework/detail?id=${id}`
    });
  },

  // 发布作业（教师端）
  publishHomework() {
    wx.navigateTo({
      url: '/pages/homework/publish'
    });
  },

  // 下拉刷新
  async onPullDownRefresh() {
    await this.loadHomeworkList();
    wx.stopPullDownRefresh();
  }
});