const auth = require('../../utils/auth');
const app = getApp();
Page({
  data: {
    matchResults: [], // 匹配结果
    errorMessage: '', // 错误信息
    showError: false, // 是否显示错误信息
    recommendUsers: [], // 推荐用户列表
    latestActivity: {
      title: '2024年春季交友活动',
      time: '2024-03-15 14:00'
    }
  },

  // 修正：将 checkLoginStatus 方法改为普通函数定义
  checkLoginStatus() {
    if (!auth.checkLogin()) {
      // 如果未登录，显示只有一个"确定"按钮的提示框
      this.showModalExample();
    }
  },

  onLoad() {
    // 页面加载时调用
    // this.checkLoginStatus();
  },

  onShow() {
    this.checkLoginStatus();
  },

  // 显示错误信息的方法
  showErrorMessage(message) {
    // 如果是对象，提取error字段
    if (typeof message === 'object') {
      if (message.error) {
        message = message.error;
      } else if (message.message) {
        message = message.message;
      }
    }
    
    this.setData({
      errorMessage: message,
      showError: true
    });
    
    // 3秒后自动隐藏错误信息
    setTimeout(() => {
      this.setData({
        showError: false
      });
    }, 3000);
  },

  // 刷新推荐用户
  refreshRecommendUsers() {
    if (this.data.recommendUsers.length === 0) {
      wx.showToast({
        title: '请先进行散落匹配',
        icon: 'none',
        duration: 2000,
        mask: true
      });
      return;
    }
    // 随机打乱当前推荐用户顺序
    const shuffledUsers = [...this.data.recommendUsers].sort(() => Math.random() - 0.5);
    this.setData({
      recommendUsers: shuffledUsers
    });
  },

  // 翻转卡片
  flipCard(e) {
    const index = e.currentTarget.dataset.index;
    const users = [...this.data.recommendUsers];
    users[index] = {
      ...users[index],
      isFlipped: !users[index].isFlipped
    };
    this.setData({
      recommendUsers: users
    });
  },

  // 格式化手机号码 (XXX XXXX XXXX)
  formatPhoneNumber(phone) {
    if (!phone || phone === '暂无') {
      return '暂无';
    }
    // 清除非数字字符
    const numbers = phone.replace(/\D/g, '');
    if (numbers.length !== 11) {
      return phone; // 如果不是标准的11位手机号，则返回原始值
    }
    // 格式化为 XXX XXXX XXXX
    return `${numbers.substring(0, 3)} ${numbers.substring(3, 7)} ${numbers.substring(7, 11)}`;
  },

  // 调用精准匹配接口
  getPreciseMatchResults() {
    // 判断用户是否是管理员
    const isAdmin = auth.getIsAdmin();
    const userId = auth.getUserId();
    // 根据 isAdmin 的值选择不同的接口 URL
    const url = isAdmin 
      ? app.globalData.baseUrl + '/admin/matchAll' // 管理员接口
      : app.globalData.baseUrl + `/user/match?userId=${encodeURIComponent(userId)}`// 普通用户接口
    // 发起请求
    wx.request({
      url: url,
      method: 'POST',
      header: {
        'Authorization': `Bearer ${wx.getStorageSync('token')}` // 添加 token 验证
      },
      success: (res) => { // 使用箭头函数确保 this 指向页面实例
        console.log('全局匹配结果:', res.data);
        // 检查返回数据结构
        if (!res.data || typeof res.data !== 'object') {
          console.error('返回数据格式不正确:', res.data);
          this.showErrorMessage('服务器返回数据格式不正确');
          return;
        }
        // 检查是否有错误信息
        if (res.data.error) {
          this.showErrorMessage(res.data);
          return;
        }
        // 创建返回数据的副本
        const data = Object.assign({}, res.data);
        // 获取 matches 和 attentions 属性
        let matches = data.matches || [];
        let attentions = data.attentions || [];
        // 确保 matches 和 attentions 是数组
        if (!Array.isArray(matches)) matches = [];
        if (!Array.isArray(attentions)) attentions = [];
        // 跳转到新页面并传递匹配结果
        wx.navigateTo({
          url: `/pages/matchResult/matchResult?matches=${encodeURIComponent(JSON.stringify(matches))}&attentions=${encodeURIComponent(JSON.stringify(attentions))}`,
        });
      },
      fail: (err) => { // 使用箭头函数确保 this 指向页面实例
        console.error('全局匹配失败:', err);
        this.showErrorMessage(err.errMsg || '请求失败，请稍后重试');
      },
    });
  },

  // 调用模糊匹配接口
  goToRandomMatch() {
    const userId = auth.getUserId(); // 获取当前用户ID
    const url = app.globalData.baseUrl + `/user/fuzzyMatch?userId=${encodeURIComponent(userId)}`; // 普通用户模糊匹配接口

    // 发起请求
    wx.request({
      url: url,
      method: 'POST',
      header: {
        'Authorization': `Bearer ${wx.getStorageSync('token')}` // 添加 token 验证
      },
      success: (res) => { // 使用箭头函数确保 this 指向页面实例
        console.log('模糊匹配结果:', res.data);

        // 检查返回数据结构
        if (!res.data || typeof res.data !== 'object') {
          console.error('返回数据格式不正确:', res.data);
          this.showErrorMessage('服务器返回数据格式不正确');
          return;
        }

        // 检查是否有错误信息
        if (res.data.error) {
          this.showErrorMessage(res.data);
          return;
        }

        // 创建返回数据的副本
        const data = Object.assign({}, res.data);

        // 获取 matches 属性
        let matches = data.matches || [];
        if (!Array.isArray(matches)) matches = [];

        // 更新推荐用户列表
        this.setData({
          recommendUsers: matches.slice(0, 5).map(user => {
            // 格式化手机号码
            const formattedPhone = this.formatPhoneNumber(user.phone || '暂无');
            return {
              id: user.id,
              name: user.name,
              avatar: user.avatar || '/image/login/defaultava.png',
              tag: user.tag || '单身',
              phone: formattedPhone,
              isFlipped: false
            };
          })
        });

        // 跳转到新页面并传递匹配结果
        wx.navigateTo({
          url: `/pages/fuzzyMatchResult/fuzzyMatchResult?matches=${encodeURIComponent(JSON.stringify(matches))}`,
        });
      },
      fail: (err) => { // 使用箭头函数确保 this 指向页面实例
        console.error('模糊匹配失败:', err);
        this.showErrorMessage(err.errMsg || '请求失败，请稍后重试');
      },
    });
  },

  // 处理精准匹配按钮点击事件
  goToPreciseMatch() {
    console.log('精准匹配按钮被点击');
    this.getPreciseMatchResults(); // 调用匹配接口
  },

  // 跳转到活动详情页
  goToActivity() {
    console.log('活动卡片被点击');
    wx.navigateTo({
      url: '/pages/activity/activity'
    });
  },

  // 跳转到使用说明页
  goToGuide() {
    console.log('使用说明被点击');
    wx.navigateTo({
      url: '/pages/guide/guide'
    });
  },

  // 查看更多活动
  goToMoreActivities() {
    console.log('更多活动被点击');
    wx.showToast({
      title: '更多活动正在开发中',
      icon: 'none',
      duration: 2000
    });
  },

  showModalExample() {
    wx.showModal({
      title: '提示', // 标题
      content: '请先登录以继续操作！', // 内容
      showCancel: false, // 隐藏取消按钮
      confirmText: '确定', // 确认按钮文字
      success: (res) => { // 回调函数
        if (res.confirm) {
          console.log('用户点击了确定');
          // 用户点击确定后跳转到登录页面
          wx.navigateTo({
            url: '/pages/login/login'
          });
        }
      }
    });
  }
});