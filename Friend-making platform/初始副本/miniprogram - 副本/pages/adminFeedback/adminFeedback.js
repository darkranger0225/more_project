const app = getApp();

Page({
  data: {
    feedbacks: [], // 所有反馈
    filteredFeedbacks: [], // 过滤后的反馈
    filter: 'all', // 过滤条件：all, PENDING, PROCESSING, RESOLVED
    replyContent: {}, // 存储每个反馈的回复内容 {feedbackId: content}
    toast: {
      show: false,
      message: '',
      type: 'success' // success 或 error
    }
  },

  onLoad() {
    this.getAllFeedbacks();
  },

  // 获取所有反馈
  getAllFeedbacks() {
    const token = wx.getStorageSync('token');
    wx.request({
      url: app.globalData.baseUrl + '/feedback/all',
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
            feedbacks: feedbacks,
            filteredFeedbacks: feedbacks
          });
          this.applyFilter();
        } else {
          this.showToast('获取反馈失败：' + (res.data.message || '未知错误'), 'error');
        }
      },
      fail: (err) => {
        this.showToast('网络请求失败：' + err.errMsg, 'error');
      }
    });
  },

  // 设置过滤条件
  setFilter(e) {
    const filter = e.currentTarget.dataset.filter;
    this.setData({
      filter: filter
    });
    this.applyFilter();
  },

  // 应用过滤
  applyFilter() {
    if (this.data.filter === 'all') {
      this.setData({
        filteredFeedbacks: this.data.feedbacks
      });
    } else {
      this.setData({
        filteredFeedbacks: this.data.feedbacks.filter(item => item.status === this.data.filter)
      });
    }
  },

  // 输入回复内容
  inputReply(e) {
    const id = e.currentTarget.dataset.id;
    const content = e.detail.value;
    let replyContent = this.data.replyContent;
    replyContent[id] = content;
    this.setData({
      replyContent: replyContent
    });
  },

  // 提交回复
  replyFeedback(e) {
    const id = e.currentTarget.dataset.id;
    const content = this.data.replyContent[id];
    
    if (!content || !content.trim()) {
      this.showToast('回复内容不能为空', 'error');
      return;
    }
    
    const token = wx.getStorageSync('token');
    const adminId = wx.getStorageSync('adminId'); // 获取存储的管理员ID
    
    // 打印调试信息
    console.log('发送回复请求的数据：', {
      feedbackId: id,
      reply: content,
      adminId: adminId,
      token: token
    });
    
    wx.request({
      url: app.globalData.baseUrl + `/feedback/reply/${id}`,
      method: 'POST',
      header: {
        'Authorization': `Bearer ${token}`
      },
      data: {
        reply: content,
        adminId: adminId // 添加管理员ID
      },
      success: (res) => {
        // 打印响应结果
        // console.log('回复请求响应：', res.data);
        
        if (res.statusCode === 200 && res.data.success) {
          this.showToast('回复成功');
          // 重新加载数据
          this.getAllFeedbacks();
        } else {
          this.showToast('回复失败：' + (res.data.message || '未知错误'), 'error');
        }
      },
      fail: (err) => {
        // 打印错误信息
        console.error('回复请求失败：', err);
        this.showToast('网络请求失败：' + err.errMsg, 'error');
      }
    });
  },

  // 更新反馈状态
  updateStatus(e) {
    const id = e.currentTarget.dataset.id;
    const status = e.currentTarget.dataset.status;
    
    const token = wx.getStorageSync('token');
    wx.request({
      url: app.globalData.baseUrl + `/feedback/status/${id}`,
      method: 'PUT',
      header: {
        'Authorization': `Bearer ${token}`
      },
      data: {
        status: status
      },
      success: (res) => {
        if (res.statusCode === 200 && res.data.success) {
          this.showToast(`已将反馈标记为${status === 'RESOLVED' ? '已解决' : '处理中'}`);
          // 重新加载数据
          this.getAllFeedbacks();
        } else {
          this.showToast('更新状态失败：' + (res.data.message || '未知错误'), 'error');
        }
      },
      fail: (err) => {
        this.showToast('网络请求失败：' + err.errMsg, 'error');
      }
    });
  },

  // 显示提示
  showToast(message, type = 'success') {
    this.setData({
      toast: {
        show: true,
        message: message,
        type: type
      }
    });
    
    setTimeout(() => {
      this.setData({
        'toast.show': false
      });
    }, 2000);
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