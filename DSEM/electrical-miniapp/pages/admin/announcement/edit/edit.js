const request = require('../../../../utils/request.js');

Page({
  data: {
    isEdit: false,
    announcementId: null,
    title: '',
    content: '',
    image: ''
  },

  onLoad(options) {
    if (options.id) {
      // 编辑模式
      this.setData({
        isEdit: true,
        announcementId: options.id
      });
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
        this.setData({
          title: res.data.title || '',
          content: res.data.content || '',
          image: res.data.image || ''
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

  // 输入标题
  onTitleInput(e) {
    this.setData({
      title: e.detail.value
    });
  },

  // 输入内容
  onContentInput(e) {
    this.setData({
      content: e.detail.value
    });
  },

  // 选择图片
  chooseImage() {
    wx.chooseImage({
      count: 1,
      sizeType: ['compressed'],
      sourceType: ['album', 'camera'],
      success: (res) => {
        const tempFilePath = res.tempFilePaths[0];
        // 这里简化处理，直接使用本地路径
        // 实际项目中应该上传到服务器
        this.setData({
          image: tempFilePath
        });
      }
    });
  },

  // 删除图片
  deleteImage() {
    this.setData({
      image: ''
    });
  },

  // 提交
  submit() {
    const { isEdit, announcementId, title, content, image } = this.data;

    // 验证
    if (!title.trim()) {
      wx.showToast({
        title: '请输入标题',
        icon: 'none'
      });
      return;
    }

    if (!content.trim()) {
      wx.showToast({
        title: '请输入内容',
        icon: 'none'
      });
      return;
    }

    wx.showLoading({
      title: isEdit ? '保存中...' : '发布中...'
    });

    const data = {
      title: title.trim(),
      content: content.trim(),
      image: image
    };

    if (isEdit) {
      // 编辑
      request.put(`/api/announcement/update/${announcementId}`, data).then(() => {
        wx.hideLoading();
        wx.showToast({
          title: '保存成功',
          icon: 'success'
        });
        setTimeout(() => {
          wx.navigateBack();
        }, 1500);
      }).catch(err => {
        wx.hideLoading();
        console.error('保存公告失败:', err);
        wx.showToast({
          title: '保存失败',
          icon: 'none'
        });
      });
    } else {
      // 新增
      request.post('/api/announcement/create', data).then(() => {
        wx.hideLoading();
        wx.showToast({
          title: '发布成功',
          icon: 'success'
        });
        setTimeout(() => {
          wx.navigateBack();
        }, 1500);
      }).catch(err => {
        wx.hideLoading();
        console.error('发布公告失败:', err);
        wx.showToast({
          title: '发布失败',
          icon: 'none'
        });
      });
    }
  }
});
