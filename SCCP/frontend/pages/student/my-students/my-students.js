const app = getApp();

Page({
  data: {
    studentList: []
  },

  onShow() {
    this.loadStudentList();
  },

  async loadStudentList() {
    try {
      const res = await app.request({
        url: '/student/my-students'
      });

      if (res.code === 200) {
        this.setData({
          studentList: res.data
        });
      }
    } catch (error) {
      wx.showToast({
        title: '加载失败',
        icon: 'none'
      });
    }
  },

  addStudent() {
    wx.navigateTo({
      url: '/pages/student/add-student/add-student'
    });
  },

  editStudent(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/student/add-student/add-student?id=${id}&mode=edit`
    });
  },

  deleteStudent(e) {
    const id = e.currentTarget.dataset.id;
    
    wx.showModal({
      title: '提示',
      content: '确定要删除这个孩子信息吗？',
      success: async (res) => {
        if (res.confirm) {
          try {
            const result = await app.request({
              url: `/student/${id}`,
              method: 'DELETE'
            });

            if (result.code === 200) {
              wx.showToast({
                title: '删除成功',
                icon: 'success'
              });
              this.loadStudentList();
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
  }
});