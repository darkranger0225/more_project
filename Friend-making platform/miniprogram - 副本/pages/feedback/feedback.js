const app = getApp();

Page({
  data: {
    feedbackTypes: [
      {name: '功能建议', value: '功能建议', checked: true},
      {name: '界面优化', value: '界面优化'},
      {name: '使用问题', value: '使用问题'},
      {name: '其他', value: '其他'}
    ],
    feedbackType: '功能建议',  // 默认反馈类型
    content: '',  // 反馈内容
    contentLength: 0, // 内容长度计数
    loading: false, // 提交按钮loading状态
    showSuccess: false, // 是否显示提交成功
    errorMsg: '', // 错误信息
    feedbacks: [], // 历史反馈
    showHistory: false // 是否显示历史反馈
  },

  onLoad() {
    // 页面加载时获取历史反馈
    this.getHistoryFeedbacks();
  },

  // 选择反馈类型
  radioChange(e) {
    this.setData({
      feedbackType: e.detail.value
    });
  },

  // 监听反馈内容输入
  inputContent(e) {
    this.setData({
      content: e.detail.value,
      contentLength: e.detail.value.length
    });
  },

  // 提交反馈
  submitFeedback() {
    // 表单验证
    if (!this.data.content.trim()) {
      this.setData({
        errorMsg: '反馈内容不能为空'
      });
      return;
    }

    this.setData({
      loading: true,
      errorMsg: ''
    });

    const token = wx.getStorageSync('token');
    if (!token) {
      wx.showToast({
        title: '请先登录',
        icon: 'none'
      });
      wx.navigateTo({
        url: '/pages/login/login'
      });
      return;
    }

    // 提交反馈到服务器
    wx.request({
      url: app.globalData.baseUrl + '/feedback/submit',
      method: 'POST',
      header: {
        'Authorization': `Bearer ${token}`
      },
      data: {
        content: this.data.content,
        type: this.data.feedbackType
      },
      success: (res) => {
        if (res.statusCode === 200 && res.data.success) {
          // 提交成功
          this.setData({
            showSuccess: true,
            content: '',
            contentLength: 0
          });
          // 刷新历史反馈
          this.getHistoryFeedbacks();
        } else {
          this.setData({
            errorMsg: '提交失败：' + (res.data.message || '未知错误')
          });
        }
      },
      fail: (err) => {
        this.setData({
          errorMsg: '网络请求失败：' + err.errMsg
        });
      },
      complete: () => {
        this.setData({
          loading: false
        });
      }
    });
  },

  // 获取历史反馈
  getHistoryFeedbacks() {
    const token = wx.getStorageSync('token');
    if (!token) return;

    wx.request({
      url: app.globalData.baseUrl + '/feedback/user',
      method: 'GET',
      header: {
        'Authorization': `Bearer ${token}`
      },
      success: (res) => {
        if (res.statusCode === 200 && res.data.success) {
          // 格式化时间
          const feedbacks = res.data.feedbacks.map(item => {
            if (item.feedbackTime) {
              item.feedbackTime = this.formatTime(new Date(item.feedbackTime));
            }
            if (item.responseTime) {
              item.responseTime = this.formatTime(new Date(item.responseTime));
            }
            return item;
          });
          
          this.setData({
            feedbacks: feedbacks
          });
        }
      }
    });
  },

  // 返回我的页面
  backToMe() {
    wx.navigateBack();
  },

  // 切换显示历史反馈
  toggleHistory() {
    this.setData({
      showHistory: !this.data.showHistory
    });
    
    if (this.data.showHistory && this.data.feedbacks.length === 0) {
      this.getHistoryFeedbacks();
    }
  },
  
  // 格式化时间
  formatTime(date) {
    const year = date.getFullYear();
    const month = date.getMonth() + 1;
    const day = date.getDate();
    const hour = date.getHours();
    const minute = date.getMinutes();
    
    return `${year}年${month}月${day}日 ${hour}:${minute < 10 ? '0' + minute : minute}`;
  }
}); 