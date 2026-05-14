const app = getApp();
Page({
  data: {
    formData: {
      name: '', // 姓名
      phone: '', // 手机号
      passWord: '', // 密码
      birthday: '', // 生日
      preferredAddress: '', // 地址
      school: '', // 学校（可选）
      qq: '', // QQ号（可选）
      wechat: '', // 微信号（可选）
      preferredInterests: [], // 兴趣爱好（数组存储）
      preferredPersonalityTraits: '', // 性格特点
      otherPreferences: [] // 其他偏好（数组存储）
    },
    interestOptions: ['运动', '音乐', '旅行', '阅读'], // 兴趣爱好选项
    personalityOptions: ['外向', '内向', '乐观', '悲观'], // 性格特征选项
    otherPreferenceOptions: ['高个子', '幽默感', '喜欢宠物', '喜欢美食'], // 其他偏好选项
    sexOptions: ['女', '男'], // 性别选项
    sexIndex: 0, // 默认选中第一个性别
    // personalityIndex: 0, // 默认选中第一个性格特征
    phoneError: '', // 手机号错误提示
    passwordError: '' // 密码错误提示
  },

  onLoad() {
    // 初始化用户 ID
    const userId = wx.getStorageSync('userId') || '';
    this.setData({
      'formData.userID': userId
    });

    // 显示必填项提示
    wx.showModal({
      title: '注册必填项提示',
      content: '以下信息为必填项：\n• 姓名\n• 手机号\n• 密码（至少6位）\n• 出生日期\n• 居住地址\n• 性别\n• 性格特征',
      showCancel: false,
      confirmText: '我知道了'
    });
  },

  // 输入框绑定事件
  bindInput(e) {
    const { field } = e.currentTarget.dataset; // 获取输入框对应的字段名
    this.setData({
      [`formData.${field}`]: e.detail.value // 更新对应字段的值
    });

    // 校验手机号
    if (field === 'phone') {
      const phoneRegex = /^1[3-9]\d{9}$/; // 中国手机号正则表达式
      if (!phoneRegex.test(e.detail.value)) {
        this.setData({ phoneError: '请输入有效的手机号' });
      } else {
        this.setData({ phoneError: '' });
      }
    }

    // 校验密码
    if (field === 'passWord') {
      if (e.detail.value.length < 6) {
        this.setData({ passwordError: '密码长度至少为6位' });
      } else {
        this.setData({ passwordError: '' });
      }
    }
  },

  // 生日日期选择器变化
  onBirthdayChange(e) {
    const selectedDate = e.detail.value; // 获取用户选择的日期
    this.setData({
      'formData.birthday': selectedDate // 更新生日字段
    });
  },

  // 地址选择器变化
  onAddressChange(e) {
    const selectedAddress = e.detail.value; // 获取用户选择的地址
    this.setData({
      'formData.preferredAddress': selectedAddress.join('-') // 将省市区拼接成字符串
    });
  },

  // 性别选择器变化
  onSexChange(e) {
    this.setData({
      sexIndex: e.detail.value,
      'formData.sex': e.detail.value // 将索引值作为性别值
    });
  },

  // 性格特征选择器变化
  onPersonalityChange(e) {
    const selectedValue = this.data.personalityOptions[e.detail.value];
    this.setData({
      'formData.preferredPersonalityTraits': selectedValue,
      personalityIndex: e.detail.value // 更新选中索引
    });
  },

  // 兴趣爱好复选框变化
  onInterestChange(e) {
    const selectedValues = e.detail.value; // 获取所有选中的值的数组
    this.setData({
      'formData.preferredInterests': selectedValues
    });
  },

  // 其他偏好复选框变化
  onOtherPreferenceChange(e) {
    const selectedValues = e.detail.value; // 获取所有选中的值的数组
    this.setData({
      'formData.otherPreferences': selectedValues
    });
  },

  // 注册按钮点击事件
  register() {
    const { formData } = this.data;

    // 校验手机号
    const phoneRegex = /^1[3-9]\d{9}$/;
    if (!phoneRegex.test(formData.phone)) {
      this.setData({ phoneError: '请输入有效的手机号' });
      return;
    }

    // 校验密码
    if (formData.passWord.length < 6) {
      this.setData({ passwordError: '密码长度至少为6位' });
      return;
    }

    // 校验其他必填字段是否为空
    if (
      !formData.name || 
      !formData.birthday || 
      !formData.preferredAddress || 
      this.data.sexIndex == null || 
      this.data.personalityIndex == null
    ) {
      wx.showToast({
        title: '请填写所有必填项',
        icon: 'none'
      });
      return;
    }

    // 构造后端期望的数据结构
    const postData = {
      name: formData.name,
      phone: formData.phone,
      password: formData.passWord,
      birthday: formData.birthday,
      address: formData.preferredAddress,
      school: formData.school,
      qq: formData.qq,
      wechat: formData.wechat,
      interests: formData.preferredInterests.join(','), // 将兴趣爱好数组转为字符串
      personalityTraits: formData.preferredPersonalityTraits,
      otherPreferences: formData.otherPreferences.join(','), // 将其他偏好数组转为字符串
      sex: this.data.sexIndex // 性别值
    };

    // 显示加载提示
    wx.showLoading({ title: '注册中...' });

    // 发送注册请求
    wx.request({
      url: app.globalData.baseUrl + '/user/register', // 后端接口地址
      method: 'POST',
      header: { 'content-type': 'application/json' },
      data: postData,
      success(res) {
        wx.hideLoading(); // 隐藏加载提示

        if (res.statusCode === 200 && res.data === "注册成功") {
          wx.showToast({
            title: '注册成功',
            icon: 'success',
            duration: 1500,
            success: () => {
              // 延迟1.5秒后跳转，让用户看到成功提示
              setTimeout(() => {
                // 使用reLaunch关闭所有页面，打开首页
                wx.reLaunch({
                  url: '/pages/login/login'
                });
              }, 1500);
            }
          });
        } else {
          wx.showToast({
            title: res.data || '注册失败，请重试',
            icon: 'none'
          });
        }
      },
      fail(err) {
        wx.hideLoading(); // 隐藏加载提示
        wx.showToast({
          title: '网络请求失败，请稍后再试',
          icon: 'none'
        });
        console.error('注册失败:', err);
      }
    });
  }
});