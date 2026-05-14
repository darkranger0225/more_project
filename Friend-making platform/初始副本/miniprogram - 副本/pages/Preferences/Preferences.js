const app = getApp();

Page({
  data: {
    userPreferences: {
      userID: wx.getStorageSync('userId') || '', // 获取当前用户的 ID
      preferredName: '',
      preferredBirthdayRangeStart: '', // 生日起始日期
      preferredBirthdayRangeEnd: '', // 生日结束日期
      preferredAddress: '',
      preferredPhone: '',
      preferredSchool: '',
      preferredPersonalityTraits: '', // 性格特征
      preferredInterests: [], // 兴趣爱好（数组存储）
      otherPreferences: [], // 其他偏好（数组存储）
      preferredQQ: '',
      preferredWeChat: ''
    },
    interestOptions: ['运动', '音乐', '旅行', '阅读'], // 兴趣爱好选项
    personalityOptions: ['外向', '内向', '乐观', '悲观'], // 性格特征选项
    otherPreferenceOptions: ['高个子', '幽默感', '喜欢宠物', '喜欢美食'] // 其他偏好选项
  },

  onLoad() {
    this.showModalExample();
    // 从本地存储中获取 userId
    const userId = wx.getStorageSync('userId') || '';
    this.setData({
      'userPreferences.userID': userId // 设置用户ID到页面数据
    });
  },

  // 姓名输入框绑定
  bindPreferredNameInput(e) {
    this.setData({
      'userPreferences.preferredName': e.detail.value
    });
  },

  // 生日起始日期选择
  bindPreferredBirthdayRangeStart(e) {
    const startDate = e.detail.value;
    this.setData({
      'userPreferences.preferredBirthdayRangeStart': startDate
    });
    
    // 如果结束日期早于开始日期，则清空结束日期
    const endDate = this.data.userPreferences.preferredBirthdayRangeEnd;
    if (endDate && endDate < startDate) {
      this.setData({
        'userPreferences.preferredBirthdayRangeEnd': ''
      });
    }
  },

  // 生日结束日期选择
  bindPreferredBirthdayRangeEnd(e) {
    const endDate = e.detail.value;
    const startDate = this.data.userPreferences.preferredBirthdayRangeStart;
    
    // 如果结束日期早于开始日期，显示提示并阻止选择
    if (startDate && endDate < startDate) {
      wx.showToast({
        title: '结束日期不能早于开始日期',
        icon: 'none',
        duration: 2000
      });
      return;
    }
    
    this.setData({
      'userPreferences.preferredBirthdayRangeEnd': endDate
    });
  },

  // 地址选择器变化
  onAddressChange(e) {
    const selectedAddress = e.detail.value; // 获取用户选择的地址
    this.setData({
      'userPreferences.preferredAddress': selectedAddress.join('-') // 将省市区拼接成字符串
    });
  },

  // 电话输入框绑定
  bindPreferredPhoneInput(e) {
    this.setData({
      'userPreferences.preferredPhone': e.detail.value
    });
  },

  // 学校输入框绑定
  bindPreferredSchoolInput(e) {
    this.setData({
      'userPreferences.preferredSchool': e.detail.value
    });
  },

  // QQ 输入框绑定
  bindPreferredQQInput(e) {
    this.setData({
      'userPreferences.preferredQQ': e.detail.value
    });
  },

  // 微信输入框绑定
  bindPreferredWeChatInput(e) {
    this.setData({
      'userPreferences.preferredWeChat': e.detail.value
    });
  },

// 兴趣爱好复选框变化
onInterestChange(e) {
  const selectedValues = e.detail.value; // 获取所有选中的值的数组
  // console.log('Selected Interests:', selectedValues);

  // 直接更新 userPreferences 中的值
  this.setData({
    'userPreferences.preferredInterests': selectedValues
  });
},

// 其他偏好复选框变化
onOtherPreferenceChange(e) {
  const selectedValues = e.detail.value; // 获取所有选中的值的数组
  // console.log('Selected Other Preferences:', selectedValues);

  // 直接更新 userPreferences 中的值
  this.setData({
    'userPreferences.otherPreferences': selectedValues
  });
},




  // 性格特征选择
  onPersonalityChange(e) {
    const selectedValue = this.data.personalityOptions[e.detail.value];
    this.setData({
      'userPreferences.preferredPersonalityTraits': selectedValue
    });
  },

  // 提交表单
  onSubmit(e) {
    const { userPreferences } = this.data;
    // console.log('User Preferences:', userPreferences);
    // 校验必填项
    if (!userPreferences.preferredName) {
      wx.showToast({ title: '请填写期望对象的名字', icon: 'none' });
      return;
    }
    if (!userPreferences.preferredPersonalityTraits) {
      wx.showToast({ title: '请选择期望对象的性格特征', icon: 'none' });
      return;
    }
    // 处理生日范围
  let preferredBirthdayRange = ''; // 默认值为空
  const { preferredBirthdayRangeStart, preferredBirthdayRangeEnd } = userPreferences;

  // 如果起始日期和结束日期都不为空，则拼接日期范围
  if (preferredBirthdayRangeStart && preferredBirthdayRangeEnd) {
    preferredBirthdayRange = `${preferredBirthdayRangeStart} to ${preferredBirthdayRangeEnd}`;
  }

    // 显示加载提示
    wx.showLoading({ title: '提交中...' });

    // 发送请求到后端
    wx.request({
      url: app.globalData.baseUrl + '/user/preferences',
      method: 'POST',
      header: { 'content-type': 'application/json' },
      data: {
        ...userPreferences,
        preferredInterests: userPreferences.preferredInterests.join(','), // 转为字符串
        otherPreferences: userPreferences.otherPreferences.join(','), // 转为字符串
        preferredBirthdayRange: preferredBirthdayRange
      },
      success(res) {
        wx.hideLoading();

        if (res.statusCode === 200) {
          wx.showToast({ title: '提交成功', icon: 'success' });
          console.log('Response:', res);
        } else {
          wx.showToast({ title: '提交失败，请重试', icon: 'none' });
        }
      },
      fail(err) {
        wx.hideLoading();
        wx.showToast({ title: '请求失败，请检查网络', icon: 'none' });
        console.error('Request Failed:', err);
      }
    });
  },
  showModalExample() {
    wx.showModal({
      title: '提示：所有信息均填写你期望对象的相关信息', // 标题
      content: '生日一栏若已知对方生日可选择同一个日期，若不确定可选范围，范围越小，填写字段越多匹配越准确，亦可不填写', // 内容
      showCancel: false, // 隐藏取消按钮
      confirmText: '确定', // 确认按钮文字
    });
  }
});