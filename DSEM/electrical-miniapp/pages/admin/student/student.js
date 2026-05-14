const request = require('../../../utils/request.js');

Page({
  data: {
    students: [],
    searchText: ''
  },

  onShow() {
    this.loadStudents();
  },

  loadStudents() {
    request.get('/api/admin/student/list', {
      page: 1,
      size: 100
    }).then(res => {
      this.setData({
        students: res.data.records || []
      });
    });
  },

  onSearch(e) {
    const searchText = e.detail.value;
    this.setData({ searchText });
    this.searchStudents(searchText);
  },

  searchStudents(searchText) {
    if (!searchText) {
      this.loadStudents();
      return;
    }

    request.get('/api/admin/student/search', {
      keyword: searchText
    }).then(res => {
      this.setData({
        students: res.data || []
      });
    }).catch(err => {
      console.error('搜索失败:', err);
    });
  },

  editStudent(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/admin/student/edit/edit?id=${id}`
    });
  },

  resetPassword(e) {
    const id = e.currentTarget.dataset.id;
    wx.showModal({
      title: '确认重置密码',
      content: '确定要重置该学生的密码为默认密码吗？',
      confirmColor: '#1890ff',
      success: (res) => {
        if (res.confirm) {
          request.put(`/api/admin/password/reset/${id}`).then(() => {
            wx.showToast({
              title: '密码重置成功',
              icon: 'success'
            });
          }).catch(err => {
            console.error('重置密码失败:', err);
          });
        }
      }
    });
  },

  deleteStudent(e) {
    const id = e.currentTarget.dataset.id;
    wx.showModal({
      title: '确认删除',
      content: '确定要删除该学生吗？此操作不可恢复',
      confirmColor: '#ff4d4f',
      success: (res) => {
        if (res.confirm) {
          request.delete(`/api/admin/student/${id}`).then(() => {
            wx.showToast({
              title: '删除成功',
              icon: 'success'
            });
            this.loadStudents();
          }).catch(err => {
            console.error('删除失败:', err);
          });
        }
      }
    });
  }
});