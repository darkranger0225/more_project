const app = getApp();

Page({
  data: {
    role: '',
    leaveId: null,
    leave: {}
  },

  onLoad(options) {
    const role = app.globalData.role || wx.getStorageSync('userInfo')?.role;
    this.setData({ 
      role,
      leaveId: options.id 
    });
    
    if (options.id) {
      this.loadLeaveDetail(options.id);
    }
  },

  async loadLeaveDetail(id) {
    try {
      const res = await app.request({
        url: `/leave/${id}`
      });

      if (res.code === 200) {
        this.setData({
          leave: res.data
        });
      }
    } catch (error) {
      wx.showToast({
        title: '加载请假详情失败',
        icon: 'none'
      });
    }
  },

  // 撤销请假申请
  cancelLeave() {
    wx.showModal({
      title: '确认撤销',
      content: '确定要撤销这条请假申请吗？',
      success: (res) => {
        if (res.confirm) {
          this.doCancelLeave();
        }
      }
    });
  },

  async doCancelLeave() {
    const { leaveId } = this.data;
    
    wx.showLoading({ title: '撤销中...' });
    
    try {
      const res = await app.request({
        url: `/leave/${leaveId}`,
        method: 'DELETE'
      });

      wx.hideLoading();

      if (res.code === 200) {
        wx.showToast({
          title: '撤销成功',
          icon: 'success'
        });
        setTimeout(() => {
          wx.navigateBack();
        }, 1500);
      } else {
        wx.showToast({
          title: res.message || '撤销失败',
          icon: 'none'
        });
      }
    } catch (error) {
      wx.hideLoading();
      wx.showToast({
        title: error.message || '撤销失败',
        icon: 'none'
      });
    }
  }
});
