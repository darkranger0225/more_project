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
        url: '/class/list'
      });

      if (res.code === 200) {
        this.setData({
          classList: res.data
        });
      }
    } catch (error) {
      wx.showToast({
        title: '加载失败',
        icon: 'none'
      });
    }
  },

  addClass() {
    wx.navigateTo({
      url: '/pages/admin/class-form/class-form'
    });
  },

  editClass(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/admin/class-form/class-form?id=${id}&mode=edit`
    });
  },

  deleteClass(e) {
    const id = e.currentTarget.dataset.id;
    
    wx.showModal({
      title: '提示',
      content: '确定要删除该班级吗？\n删除后该班级的学生将失去班级关联！',
      confirmColor: '#f44336',
      success: async (res) => {
        if (res.confirm) {
          try {
            const result = await app.request({
              url: `/class/${id}`,
              method: 'DELETE'
            });

            if (result.code === 200) {
              wx.showToast({
                title: '删除成功',
                icon: 'success'
              });
              this.loadClassList();
            }
          } catch (error) {
            wx.showToast({
              title: error.message || '删除失败',
              icon: 'none'
            });
          }
        }
      }
    });
  },

  onPullDownRefresh() {
    this.loadClassList().then(() => {
      wx.stopPullDownRefresh();
    });
  }
});