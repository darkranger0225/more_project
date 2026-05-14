const request = require('../../../utils/request.js');

Page({
  data: {
    announcementList: []
  },

  onLoad() {
    this.loadAnnouncementList();
  },

  onShow() {
    this.loadAnnouncementList();
  },

  // 加载公告列表
  loadAnnouncementList() {
    wx.showLoading({
      title: '加载中...'
    });

    request.get('/api/announcement/all').then(res => {
      wx.hideLoading();
      if (res.data) {
        // 处理数据
        const processedData = res.data.map(item => ({
          ...item,
          createTime: item.createTime ? item.createTime.substring(0, 16) : ''
        }));

        this.setData({
          announcementList: processedData
        });
      }
    }).catch(err => {
      wx.hideLoading();
      console.error('加载公告列表失败:', err);
      wx.showToast({
        title: '加载失败',
        icon: 'none'
      });
    });
  },

  // 跳转到添加页面
  goToAdd() {
    wx.navigateTo({
      url: '/pages/admin/announcement/edit/edit'
    });
  },

  // 跳转到编辑页面
  goToEdit(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/admin/announcement/edit/edit?id=${id}`
    });
  },

  // 删除公告
  deleteAnnouncement(e) {
    const id = e.currentTarget.dataset.id;

    wx.showModal({
      title: '确认删除',
      content: '确定要删除这条公告吗？删除后无法恢复。',
      confirmColor: '#ff4d4f',
      success: (res) => {
        if (res.confirm) {
          wx.showLoading({
            title: '删除中...'
          });

          request.del(`/api/announcement/delete/${id}`).then(() => {
            wx.hideLoading();
            wx.showToast({
              title: '删除成功',
              icon: 'success'
            });
            this.loadAnnouncementList();
          }).catch(err => {
            wx.hideLoading();
            console.error('删除公告失败:', err);
            wx.showToast({
              title: '删除失败',
              icon: 'none'
            });
          });
        }
      }
    });
  },

  // 预览图片
  previewImage(e) {
    const url = e.currentTarget.dataset.url;
    wx.previewImage({
      urls: [url]
    });
  }
});
