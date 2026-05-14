const app = getApp();

Page({
  data: {
    userInfo: {},
    roleText: '',
    noticeList: []
  },

  onLoad() {
    this.loadUserInfo();
  },

  onShow() {
    this.loadUserInfo();
    this.loadNoticeList();
  },

  // 加载用户信息
  loadUserInfo() {
    const userInfo = app.globalData.userInfo || wx.getStorageSync('userInfo');
    const role = app.globalData.role || userInfo?.role;
    
    let roleText = '';
    switch(role) {
      case 'PARENT':
        roleText = '家长';
        break;
      case 'TEACHER':
        roleText = '教师';
        break;
      case 'ADMIN':
        roleText = '管理员';
        break;
    }

    this.setData({
      userInfo: userInfo || {},
      roleText
    });
  },

  // 加载通知列表
  async loadNoticeList() {
    try {
      const res = await app.request({
        url: '/notice/list'
      });

      if (res.code === 200) {
        // 格式化时间
        const notices = res.data.map(item => {
          return {
            ...item,
            createTime: this.formatTime(item.createTime)
          };
        });

        this.setData({
          noticeList: notices.slice(0, 5) // 只显示前5条
        });
      }
    } catch (error) {
      // 加载通知失败
    }
  },

  // 格式化时间
  formatTime(timeStr) {
    if (!timeStr) return '';
    const date = new Date(timeStr);
    const now = new Date();
    const diff = now - date;
    
    // 小于1小时显示"X分钟前"
    if (diff < 3600000) {
      const minutes = Math.floor(diff / 60000);
      return minutes < 1 ? '刚刚' : `${minutes}分钟前`;
    }
    
    // 小于24小时显示"X小时前"
    if (diff < 86400000) {
      const hours = Math.floor(diff / 3600000);
      return `${hours}小时前`;
    }
    
    // 否则显示日期
    const month = date.getMonth() + 1;
    const day = date.getDate();
    return `${month}月${day}日`;
  },

  // 查看通知详情
  viewNoticeDetail(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/notice/notice?id=${id}`
    });
  },

  // 查看全部通知
  viewAllNotices() {
    wx.navigateTo({
      url: '/pages/notice/notice'
    });
  },

  // 跳转到作业页面
  goToHomework() {
    wx.switchTab({
      url: '/pages/homework/homework'
    });
  },

  // 跳转到成绩页面
  goToScore() {
    wx.switchTab({
      url: '/pages/score/score'
    });
  },

  // 跳转到请假页面
  goToLeave() {
    wx.switchTab({
      url: '/pages/leave/leave'
    });
  },

  // 下拉刷新
  async onPullDownRefresh() {
    await this.loadNoticeList();
    wx.stopPullDownRefresh();
  }
});