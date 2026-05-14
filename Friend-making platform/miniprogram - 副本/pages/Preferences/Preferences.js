const app = getApp();

Page({
  data: {
    userId: wx.getStorageSync('userId') || '', // 获取当前用户的 ID
    userPreferences: {
      preferredSex: null, // 期望性别：0-女, 1-男, null-不限
      preferredAgeRange: '', // 年龄范围
      preferredAddress: '',
      preferredSchool: '',
      preferredPersonality: '',
      otherPreferences: ''
    },
    sexOptions: ['不限', '女', '男'], // 性别选项
    sexIndex: 0, // 默认选中"不限"
    ageRangeOptions: [
      '18-20', '21-23', '24-26', '27-29',
      '30-32', '33-35', '36-38', '39-41',
      '42-44', '45-47', '48-50', '50+'
    ],
    personalityOptions: ['外向', '内向', '乐观', '悲观', '稳重', '活泼', '理性', '感性'],
    interestOptions: ['运动', '音乐', '旅行', '阅读', '电影', '摄影', '美食', '游戏', '艺术', '科技'],
    otherPreferenceOptions: ['有幽默感', '喜欢宠物', '顾家', '有上进心', '善于沟通', '孝顺', '有责任心', '温柔体贴','善良'],
    interestLevels: ['一般', '喜欢', '热爱'], // 兴趣等级选项
    selectedInterests: [], // 选中的兴趣爱好
    interestLevelsMap: {} // 存储每个兴趣对应的等级
  },

  onLoad() {
    this.showModalExample();
    const userId = wx.getStorageSync('userId') || '';
    this.setData({ 
      userId,
      // 确保selectedInterests和interestLevelsMap被正确初始化
      selectedInterests: [],
      interestLevelsMap: {}
    });
    
    // 尝试获取已保存的偏好设置
    this.getUserPreferences();
  },

  // 获取用户偏好设置
  getUserPreferences() {
    const { userId } = this.data;
    if (!userId) return;

    wx.request({
      url: `http://localhost:8080/api/user/preferences/${userId}`,
      method: 'GET',
      success: (res) => {
        if (res.statusCode === 200 && res.data) {
          // 处理期望性别的索引
          let sexIndex = 0; // 默认"不限"
          if (res.data.preferredSex === 0) {
            sexIndex = 1; // "女"
          } else if (res.data.preferredSex === 1) {
            sexIndex = 2; // "男"
          }
          
          this.setData({
            userPreferences: {
              preferredSex: res.data.preferredSex !== undefined ? res.data.preferredSex : null,
              preferredAgeRange: res.data.preferredAgeRange || '',
              preferredAddress: res.data.preferredAddress || '',
              preferredSchool: res.data.preferredSchool || '',
              preferredPersonality: res.data.preferredPersonality || '',
              otherPreferences: res.data.otherPreferences || ''
            },
            sexIndex: sexIndex
          });
        }
      }
    });
    
    // 同时获取用户兴趣爱好
    this.getUserInterests();
  },
  
  // 获取用户兴趣爱好
  getUserInterests() {
    const { userId } = this.data;
    if (!userId) return;

    wx.request({
      url: `http://localhost:8080/api/interests/${userId}`,
      method: 'GET',
      success: (res) => {
        if (res.statusCode === 200 && res.data && res.data.success && Array.isArray(res.data.data)) {
          // 提取兴趣爱好名称和等级
          const selectedInterests = [];
          const interestLevelsMap = {};
          
          res.data.data.forEach(interest => {
            if (interest.interestName && typeof interest.interestLevel === 'number') {
              selectedInterests.push(interest.interestName);
              // 后端返回的等级是1-3，前端使用的是0-2的索引，需要减1
              interestLevelsMap[interest.interestName] = interest.interestLevel - 1;
            }
          });
          
          this.setData({
            selectedInterests,
            interestLevelsMap
          });
          
          console.log('已加载的兴趣爱好:', selectedInterests);
          console.log('已加载的兴趣等级:', interestLevelsMap);
        } 
      },
      fail: (err) => {
        console.error('获取兴趣爱好失败:', err);
      }
    });
  },

  // 期望性别选择器变化
  onPreferredSexChange(e) {
    const selectedIndex = parseInt(e.detail.value);
    let preferredSex = null;
    
    // 根据选择的索引设置preferredSex值
    // 0-不限(null), 1-女(0), 2-男(1)
    if (selectedIndex === 1) {
      preferredSex = 0; // 女
    } else if (selectedIndex === 2) {
      preferredSex = 1; // 男
    }
    
    this.setData({
      sexIndex: selectedIndex,
      'userPreferences.preferredSex': preferredSex
    });
  },

  // 地址选择器变化
  onAddressChange(e) {
    const selectedAddress = e.detail.value;
    this.setData({
      'userPreferences.preferredAddress': selectedAddress.join('-')
    });
  },

  // 学校输入框绑定
  bindPreferredSchoolInput(e) {
    this.setData({
      'userPreferences.preferredSchool': e.detail.value
    });
  },

  // 年龄范围选择
  onAgeRangeChange(e) {
    const selectedValue = this.data.ageRangeOptions[e.detail.value];
    this.setData({
      'userPreferences.preferredAgeRange': selectedValue
    });
  },

  // 性格特征选择
  onPersonalityChange(e) {
    const selectedValue = this.data.personalityOptions[e.detail.value];
    this.setData({
      'userPreferences.preferredPersonality': selectedValue
    });
  },

  // 兴趣爱好复选框变化
  onInterestChange(e) {
    // 获取选中的兴趣爱好数组
    const selectedInterests = e.detail.value || [];
    // 创建新的兴趣等级映射
    const updatedLevelsMap = {};
    
    // 为每个选中的兴趣设置默认等级0("一般")
    selectedInterests.forEach(interest => {
      updatedLevelsMap[interest] = 0;
    });
    
    this.setData({
      selectedInterests,
      interestLevelsMap: updatedLevelsMap
    });
  },



  // 兴趣等级变化
  onInterestLevelChange(e) {
    const { interest } = e.currentTarget.dataset;
    const levelIndex = parseInt(e.detail.value);
    const { interestLevelsMap } = this.data;
    
    this.setData({
      [`interestLevelsMap.${interest}`]: levelIndex
    });
  },

  // 其他偏好复选框变化
  onOtherPreferenceChange(e) {
    const selectedValues = e.detail.value;
    this.setData({
      'userPreferences.otherPreferences': selectedValues.join(',')
    });
  },

  // 保存兴趣爱好到单独的表
  saveUserInterests(userId, interests, interestLevelsMap) {
    if (!interests || interests.length === 0) return Promise.resolve();

    // 构建兴趣爱好数据，使用用户选择的等级
    const interestsData = interests.map(interest => ({
      interestName: interest,
      interestLevel: (interestLevelsMap[interest] || 0) + 1 // 将索引转换为1-3的等级值
    }));

    return new Promise((resolve, reject) => {
      wx.request({
        url: `http://localhost:8080/api/interests/batch/${userId}`,
        method: 'POST',
        header: { 'content-type': 'application/json' },
        data: interestsData,
        success: (res) => resolve(res),
        fail: (err) => reject(err)
      });
    });
  },

  // 提交表单
  onSubmit(e) {
    const { userId, userPreferences, selectedInterests, interestLevelsMap } = this.data;
    
    // 校验必填项
    if (!userId) {
      wx.showToast({ title: '用户未登录', icon: 'none' });
      return;
    }

    // 显示加载提示
    wx.showLoading({ title: '提交中...' });

    // 首先保存偏好设置
    wx.request({
      url: `http://localhost:8080/api/user/preferences/${userId}`,
      method: 'PUT', // 改为PUT请求，调用更新方法
      header: { 'content-type': 'application/json' },
      data: userPreferences,
      success: (res) => {
        // 然后保存兴趣爱好
        this.saveUserInterests(userId, selectedInterests, interestLevelsMap).then(() => {
          wx.hideLoading();
          wx.showToast({ title: '提交成功', icon: 'success' });
          // 保存成功后跳转
          wx.navigateBack();
        }).catch(() => {
          wx.hideLoading();
          wx.showToast({ title: '兴趣爱好保存失败', icon: 'none' });
        });
      },
      fail: (err) => {
        wx.hideLoading();
        wx.showToast({ title: '提交失败，请重试', icon: 'none' });
      }
    });
  },
  showModalExample() {
    wx.showModal({
      title: '提示：此处填写期望对象的信息', // 标题
      content: '填写的信息越多越容易匹配到合适的朋友哦', // 内容
      showCancel: false, // 隐藏取消按钮
      confirmText: '确定', // 确认按钮文字
    });
  }
});