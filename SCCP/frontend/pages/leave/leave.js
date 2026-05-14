const app = getApp();

Page({
  data: {
    role: '',
    leaveList: []
  },

  onShow() {
    const role = app.globalData.role || wx.getStorageSync('userInfo')?.role;
    this.setData({ role });
    this.loadLeaveList();
  },

  async loadLeaveList() {
    try {
      const res = await app.request({
        url: '/leave/list'
      });

      if (res.code === 200) {
        this.setData({
          leaveList: res.data || []
        });
      } else {
        wx.showToast({
          title: res.message || '加载失败',
          icon: 'none'
        });
      }
    } catch (error) {
      wx.showToast({
        title: '加载请假列表失败',
        icon: 'none'
      });
    }
  },

  applyLeave() {
    wx.navigateTo({
      url: '/pages/leave/apply'
    });
  },

  // 查看请假详情
  viewLeaveDetail(e) {
    const id = e.currentTarget.dataset.id;
    const status = e.currentTarget.dataset.status;
    // 教师点击待审批的请假条时，不跳转详情，而是显示审批弹窗
    if (this.data.role === 'TEACHER' && status === 'PENDING') {
      return;
    }
    wx.navigateTo({
      url: `/pages/leave/detail?id=${id}`
    });
  },

  // 教师审批通过
  approveLeave(e) {
    const id = e.currentTarget.dataset.id;
    this.showApproveDialog(id, 'APPROVED');
  },

  // 教师审批拒绝
  rejectLeave(e) {
    const id = e.currentTarget.dataset.id;
    this.showApproveDialog(id, 'REJECTED');
  },

  // 显示审批对话框
  showApproveDialog(id, status) {
    const isApprove = status === 'APPROVED';
    wx.showModal({
      title: isApprove ? '通过请假' : '拒绝请假',
      content: isApprove ? '批准' : '拒绝',
      editable: true,
      placeholderText: '请输入审批备注（可选）',
      success: (res) => {
        if (res.confirm) {
          this.submitApprove(id, status, res.content);
        }
      }
    });
  },

  // 提交审批
  async submitApprove(id, status, remark) {
    wx.showLoading({ title: '处理中...' });
    try {
      const res = await app.request({
        url: '/leave/approve',
        method: 'POST',
        data: {
          id,
          status,
          approveRemark: remark || ''
        }
      });

      wx.hideLoading();

      if (res.code === 200) {
        wx.showToast({
          title: status === 'APPROVED' ? '已通过' : '已拒绝',
          icon: 'success'
        });
        this.loadLeaveList();
      } else {
        wx.showToast({
          title: res.message || '操作失败',
          icon: 'none'
        });
      }
    } catch (error) {
      wx.hideLoading();
      wx.showToast({
        title: error.message || '操作失败',
        icon: 'none'
      });
    }
  },

  onPullDownRefresh() {
    this.loadLeaveList().then(() => {
      wx.stopPullDownRefresh();
    });
  }
});