const request = require('../../../utils/request.js');

Page({
  data: {
    title: '',
    content: '',
    image: ''
  },

  onTitleInput(e) {
    this.setData({
      title: e.detail.value
    });
  },

  onContentInput(e) {
    this.setData({
      content: e.detail.value
    });
  },

  chooseImage() {
    wx.chooseImage({
      count: 1,
      sizeType: ['compressed'],
      sourceType: ['album', 'camera'],
      success: (res) => {
        this.setData({
          image: res.tempFilePaths[0]
        });
      }
    });
  },

  submitRepair() {
    const { title, content, image } = this.data;
    
    if (!title) {
      wx.showToast({
        title: '请输入标题',
        icon: 'none'
      });
      return;
    }
    
    if (!content) {
      wx.showToast({
        title: '请输入内容',
        icon: 'none'
      });
      return;
    }
    
    request.post('/api/repair/add', {
      title,
      content,
      image
    }).then(() => {
      wx.showToast({
        title: '提交成功',
        icon: 'success'
      });
      setTimeout(() => {
        wx.navigateBack();
      }, 1500);
    });
  }
});
