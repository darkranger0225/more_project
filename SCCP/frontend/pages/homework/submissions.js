const app = getApp();

Page({
  data: {
    homeworkId: null,
    homeworkTitle: '',
    submissions: [],
    submitCount: 0,
    totalCount: 0,
    submitRate: 0
  },

  onLoad(options) {
    this.setData({
      homeworkId: options.id,
      homeworkTitle: decodeURIComponent(options.title || '')
    });
    
    if (options.id) {
      this.loadSubmissions(options.id);
    }
  },

  async loadSubmissions(homeworkId) {
    try {
      const res = await app.request({
        url: `/homework/${homeworkId}/submits`
      });

      if (res.code === 200) {
        const submissions = res.data.map(item => {
          const processedItem = {
            ...item,
            submitTime: item.submitTime ? this.formatDateTime(item.submitTime) : null
          };
          // 处理图片URL
          if (item.attachmentUrl) {
            processedItem.attachmentUrl = this.processImageUrls(item.attachmentUrl);
            // 将图片URL转换为数组
            processedItem.imageList = processedItem.attachmentUrl.split(',').map(url => url.trim());
          }
          return processedItem;
        });

        const submitCount = submissions.filter(s => s.submitTime).length;
        const totalCount = submissions.length;
        const submitRate = totalCount > 0 ? Math.round((submitCount / totalCount) * 100) : 0;

        this.setData({
          submissions,
          submitCount,
          totalCount,
          submitRate
        });
      }
    } catch (error) {
      wx.showToast({
        title: '加载提交列表失败',
        icon: 'none'
      });
    }
  },

  // 处理图片URL，将相对路径转换为完整URL
  processImageUrls(urls) {
    if (!urls) return '';
    // 使用app配置的baseUrl拼接完整URL
    const baseUrl = app.globalData.baseUrl;
    
    return urls.split(',').map(url => {
      url = url.trim();
      // 如果已经是完整URL，直接返回
      if (url.startsWith('http://') || url.startsWith('https://')) {
        return url;
      }
      // 根据baseUrl拼接完整URL
      // url 可能是 /uploads/xxx.jpg 或 uploads/xxx.jpg
      if (url.startsWith('/')) {
        return baseUrl + url;
      } else {
        return baseUrl + '/' + url;
      }
    }).join(',');
  },

  formatDateTime(dateTimeStr) {
    if (!dateTimeStr) return '';
    const date = new Date(dateTimeStr);
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');
    return `${month}-${day} ${hours}:${minutes}`;
  },

  // 预览图片
  previewImage(e) {
    const url = e.currentTarget.dataset.url;
    const urls = e.currentTarget.dataset.urls.split(',');
    wx.previewImage({
      current: url,
      urls: urls
    });
  },

  showGradeDialog(e) {
    const submitId = e.currentTarget.dataset.id;
    const studentName = e.currentTarget.dataset.student;
    
    wx.showModal({
      title: `批改 - ${studentName}`,
      editable: true,
      placeholderText: '请输入分数',
      success: (res) => {
        if (res.confirm && res.content) {
          const score = parseFloat(res.content);
          if (isNaN(score)) {
            wx.showToast({
              title: '请输入有效的分数',
              icon: 'none'
            });
            return;
          }
          this.showCommentDialog(submitId, score);
        }
      }
    });
  },

  showCommentDialog(submitId, score) {
    wx.showModal({
      title: '添加评语（可选）',
      editable: true,
      placeholderText: '请输入评语',
      success: (res) => {
        if (res.confirm) {
          this.submitGrade(submitId, score, res.content);
        }
      }
    });
  },

  async submitGrade(submitId, score, comment) {
    wx.showLoading({ title: '提交中...' });
    
    try {
      const res = await app.request({
        url: '/homework/grade',
        method: 'POST',
        data: {
          id: submitId,
          score,
          comment: comment || ''
        }
      });

      wx.hideLoading();

      if (res.code === 200) {
        wx.showToast({
          title: '批改成功',
          icon: 'success'
        });
        this.loadSubmissions(this.data.homeworkId);
      } else {
        wx.showToast({
          title: res.message || '批改失败',
          icon: 'none'
        });
      }
    } catch (error) {
      wx.hideLoading();
      wx.showToast({
        title: error.message || '批改失败',
        icon: 'none'
      });
    }
  }
});
