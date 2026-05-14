const app = getApp();

Page({
  data: {
    notice: null,
    role: '',
    readList: [],
    showReadList: false
  },

  onLoad(options) {
    const role = app.globalData.role || wx.getStorageSync('userInfo')?.role;
    this.setData({ role });

    if (options.id) {
      this.loadNoticeDetail(options.id);
    }
  },

  // 加载通知详情
  async loadNoticeDetail(id) {
    try {
      const res = await app.request({
        url: `/notice/${id}`
      });

      if (res.code === 200 && res.data) {
        const notice = res.data;
        // 格式化时间
        notice.createTime = this.formatTime(notice.createTime);
        notice.publishTime = this.formatTime(notice.publishTime);
        // 设置目标类型名称
        notice.targetTypeName = this.getTargetTypeName(notice.targetType);

        this.setData({ notice });
      } else {
        wx.showToast({ title: '通知不存在', icon: 'none' });
        setTimeout(() => {
          wx.navigateBack();
        }, 1500);
      }
    } catch (error) {
      console.error('加载通知详情失败', error);
      wx.showToast({ title: '加载失败', icon: 'none' });
    }
  },

  // 获取目标类型名称
  getTargetTypeName(type) {
    const typeMap = {
      'ALL': '全部用户',
      'CLASS': '指定班级',
      'PARENT': '指定家长'
    };
    return typeMap[type] || type;
  },

  // 格式化时间
  formatTime(timeStr) {
    if (!timeStr) return '';
    const date = new Date(timeStr);
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    const hour = date.getHours().toString().padStart(2, '0');
    const minute = date.getMinutes().toString().padStart(2, '0');
    return `${year}-${month}-${day} ${hour}:${minute}`;
  },

  // 加载已读人员列表
  async loadReadList() {
    const { notice } = this.data;
    if (!notice) return;

    try {
      const res = await app.request({
        url: `/notice/${notice.id}/read-list`
      });

      if (res.code === 200) {
        // 格式化时间
        const readList = (res.data || []).map(item => ({
          ...item,
          readTime: this.formatTime(item.read_time || item.readTime)
        }));
        this.setData({ readList });
      }
    } catch (error) {
      console.error('加载已读列表失败', error);
    }
  },

  // 显示/隐藏已读人员列表
  toggleReadList() {
    const { showReadList } = this.data;
    if (!showReadList) {
      // 展开时加载数据
      this.loadReadList();
    }
    this.setData({ showReadList: !showReadList });
  },

  // 获取角色名称
  getRoleName(role) {
    const roleMap = {
      'ADMIN': '管理员',
      'TEACHER': '教师',
      'PARENT': '家长'
    };
    return roleMap[role] || role;
  },

  // 删除通知
  async deleteNotice() {
    const { notice, role } = this.data;

    // 只有发布者或管理员可以删除
    if (role !== 'ADMIN' && notice.publisherId !== app.globalData.userId) {
      wx.showToast({ title: '无权删除', icon: 'none' });
      return;
    }

    wx.showModal({
      title: '确认删除',
      content: '确定要删除这条通知吗？',
      success: async (res) => {
        if (res.confirm) {
          try {
            const result = await app.request({
              url: `/notice/${notice.id}`,
              method: 'DELETE'
            });

            if (result.code === 200) {
              wx.showToast({ title: '删除成功', icon: 'success' });
              setTimeout(() => {
                wx.navigateBack();
              }, 1500);
            } else {
              wx.showToast({ title: result.message || '删除失败', icon: 'none' });
            }
          } catch (error) {
            wx.showToast({ title: error.message || '删除失败', icon: 'none' });
          }
        }
      }
    });
  }
});
