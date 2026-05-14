const app = getApp();
Page({
  data: {
    formData: {
      userID: wx.getStorageSync('userId') || '', // 获取当前用户的 ID
      name: '', // 姓名
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
    // sexIndex: 0, // 默认选中第一个性别
    passwordError: '' // 密码错误提示
  },

  onLoad() {
    // 初始化用户 ID
    const userId = wx.getStorageSync('userId') || '';
    this.setData({
      'userPreferences.userID': userId // 设置用户ID到页面数据
    });
  },

  // 输入框绑定事件
  bindInput(e) {
    const { field } = e.currentTarget.dataset; // 获取输入框对应的字段名
    this.setData({
      [`formData.${field}`]: e.detail.value // 更新对应字段的值
    });

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

  // 更新按钮点击事件
  update() {
    const { formData } = this.data;

    // 校验密码
    if (formData.passWord.length < 6) {
      this.setData({ passwordError: '密码长度至少为6位' });
      return;
    }

    // 构造后端期望的数据结构
    const postData = {
      userId:formData.userID,
      name: formData.name,
      passWord: formData.passWord,
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
    console.log(postData); // 打印请求数据，检查 userID 是否正确

    // 显示加载提示
    wx.showLoading({ title: '更新中...' });

    // 发送更新请求
    wx.request({
      url: app.globalData.baseUrl + '/user/update', // 后端接口地址
      method: 'PUT',
      header: { 'content-type': 'application/json' },
      data: postData,
      success(res) {
        wx.hideLoading(); // 隐藏加载提示

        if (res.statusCode === 200 && res.data === "更新成功") {
          wx.showToast({
            title: '更新成功',
            icon: 'success'
          });
        } else {
          wx.showToast({
            title: res.data || '更新失败，请重试',
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
        console.error('更新失败:', err);
      }
    });
  }
});