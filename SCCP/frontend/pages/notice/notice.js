const app = getApp();

Page({
  data: {
    notices: [],
    role: '',
    activeTab: 'receive', // receive: 收到的通知, send: 发送的通知
    page: 1,
    pageSize: 10,
    hasMore: true
  },

  onLoad() {
    const role = app.globalData.role || wx.getStorageSync('userInfo')?.role;
    this.setData({ role });
  },

  onShow() {
    this.setData({ page: 1, notices: [] });
    this.loadNotices();
  },

  // 切换标签
  switchTab(e) {
    const tab = e.currentTarget.dataset.tab;
    this.setData({
      activeTab: tab,
      page: 1,
      notices: [],
      hasMore: true
    });
    this.loadNotices();
  },

  // 加载通知列表
  async loadNotices() {
    const { page, pageSize, activeTab, role } = this.data;

    try {
      let url = '/notice/list';
      if ((role === 'ADMIN' || role === 'TEACHER') && activeTab === 'send') {
        url = '/notice/published';
      }

      const res = await app.request({
        url: url,
        data: { page, pageSize }
      });

      if (res.code === 200) {
        const newNotices = res.data || [];
        // 格式化数据
        const formattedNotices = newNotices.map(item => ({
          ...item,
          targetTypeName: this.getTargetTypeName(item.targetType),
          createTime: this.formatTime(item.createTime)
        }));

        this.setData({
          notices: page === 1 ? formattedNotices : [...this.data.notices, ...formattedNotices],
          hasMore: newNotices.length === pageSize
        });
      }
    } catch (error) {
      console.error('加载通知失败', error);
    }
  },

  // 获取目标类型名称
  getTargetTypeName(type) {
    const typeMap = {
      'ALL': '全部',
      'CLASS': '指定班级',
      'PARENT': '指定家长'
    };
    return typeMap[type] || type;
  },

  // 格式化时间
  formatTime(timeStr) {
    if (!timeStr) return '';
    const date = new Date(timeStr);
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    const hour = date.getHours().toString().padStart(2, '0');
    const minute = date.getMinutes().toString().padStart(2, '0');
    return `${month}-${day} ${hour}:${minute}`;
  },

  // 查看通知详情
  viewNoticeDetail(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/notice/detail?id=${id}`
    });
  },

  // 跳转到发布通知页面
  goToPublish() {
    console.log('goToPublish called, role:', this.data.role);
    wx.navigateTo({
      url: '/pages/notice/publish',
      success: () => {
        console.log('navigateTo success');
      },
      fail: (err) => {
        console.error('navigateTo fail:', err);
      }
    });
  },

  // 下拉刷新
  onPullDownRefresh() {
    this.setData({ page: 1, notices: [] });
    this.loadNotices().then(() => {
      wx.stopPullDownRefresh();
    });
  },

  // 上拉加载更多
  onReachBottom() {
    if (this.data.hasMore) {
      this.setData({ page: this.data.page + 1 });
      this.loadNotices();
    }
  }
});
