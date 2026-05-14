const request = require('../../../utils/request.js');

Page({
  data: {
    announcement: {}
  },

  onLoad(options) {
    if (options.id) {
      this.loadAnnouncementDetail(options.id);
    }
  },

  // 加载公告详情
  loadAnnouncementDetail(id) {
    wx.showLoading({
      title: '加载中...'
    });

    request.get(`/api/announcement/detail/${id}`).then(res => {
      wx.hideLoading();
      if (res.data) {
        // 处理时间格式
        const announcement = {
          ...res.data,
          createTime: res.data.createTime ? res.data.createTime.substring(0, 16) : ''
        };
        
        this.setData({
          announcement: announcement
        });
        
        // 设置页面标题
        wx.setNavigationBarTitle({
          title: announcement.title || '公告详情'
        });
      }
    }).catch(err => {
      wx.hideLoading();
      console.error('加载公告详情失败:', err);
      wx.showToast({
        title: '加载失败',
        icon: 'none'
      });
    });
  },

  // 预览图片
  previewImage() {
    const url = this.data.announcement.image;
    if (url) {
      wx.previewImage({
        urls: [url]
      });
    }
  }
});
