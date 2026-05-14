const app = getApp();

Page({
  data: {
    activity: {
      title: '2025年春季交友活动',
      date: '2025年3月15日',
      time: '3月15日 14:00-18:00',
      location: '南阳市中心广场花园咖啡厅',
      quota: '限50人，是否有名额：是',
      fee: '150元/人（含下午茶、活动物料）',
      description: '春暖花开，万物复苏，一年一度的春季交友活动要和大家见面啦！本次活动以"心之所向，爱之所至"为主题，为单身男女提供一个轻松愉快的交友平台。我们精心设计了丰富的互动环节和游戏，帮助大家打破陌生感，自然交流。同时邀请了情感心理专家进行分享，助您找到心动的TA。',
      agenda: [
        {
          time: '13:30-14:00',
          content: '签到入场'
        },
        {
          time: '14:00-14:30',
          content: '活动介绍与“破冰游戏”'
        },
        {
          time: '14:30-15:30',
          content: '心动配对环节'
        },
        {
          time: '15:30-16:00',
          content: '下午茶休息'
        },
        {
          time: '16:00-17:00',
          content: '特邀情感专家座谈'
        },
        {
          time: '17:00-18:00',
          content: '自由交流环节'
        }
      ],
      tips: [
        '请提前15分钟到达活动现场，签到入场',
        '活动期间请遵守场地规则，文明礼貌参与',
        '为保证活动质量，报名成功后若需取消，请至少提前48小时取消',
        '为活动创造良好氛围，请着装得体，准时参加',
        '活动全程禁止吸烟，请勿大声喧哗',
        '现场提供免费WiFi和充电设备'
      ]
    }
  },

  onLoad: function(options) {
    // 页面加载时可以从options获取参数
    console.log('活动页面加载', options);
    
    // 如果有活动ID，可以通过API获取详细信息
    if (options.id) {
      this.getActivityDetail(options.id);
    }
  },

  // 获取活动详情
  getActivityDetail: function(id) {
    // 模拟API请求获取活动详情
    // wx.request({
    //   url: 'your-api-url/activity/' + id,
    //   success: (res) => {
    //     if (res.data && res.data.success) {
    //       this.setData({
    //         activity: res.data.data
    //       });
    //     }
    //   }
    // });
  },

  // 报名活动
  joinActivity: function() {
    // 检查用户是否登录
    const auth = require('../../utils/auth');
    if (!auth.checkLogin()) {
      wx.showModal({
        title: '提示',
        content: '请先登录后再报名活动',
        showCancel: false,
        success: (res) => {
          if (res.confirm) {
            wx.navigateTo({
              url: '/pages/login/login'
            });
          }
        }
      });
      return;
    }

    // 显示报名确认弹窗
    wx.showModal({
      title: '确认报名',
      content: '您确定要报名参加该活动吗？报名成功后将收到确认通知。',
      success: (res) => {
        if (res.confirm) {
          this.submitJoinRequest();
        }
      }
    });
  },

  // 提交报名请求
  submitJoinRequest: function() {
    // 显示加载提示
    wx.showLoading({
      title: '报名中...',
    });

    // 模拟API请求
    setTimeout(() => {
      wx.hideLoading();
      
      // 显示报名成功
      wx.showToast({
        title: '报名成功！',
        icon: 'success',
        duration: 2000,
        success: () => {
          // 2秒后返回首页
          setTimeout(() => {
            wx.switchTab({
              url: '/pages/home/home'
            });
          }, 2000);
        }
      });
    }, 1500);
  },

  // 分享
  onShareAppMessage: function() {
    return {
      title: this.data.activity.title,
      path: '/pages/activity/activity',
      imageUrl: '/image/login/spring.png' // 分享图片
    };
  }
}); 