const app = getApp();
Page({
  data: {
    userId: wx.getStorageSync('userId') || '', // 获取当前用户的 ID
    formData: {
      name: '', // 姓名
      password: '', // 密码
      birthday: '', // 生日
      address: '', // 地址
      school: '', // 学校（可选）
      qq: '', // QQ号（可选）
      wechat: '', // 微信号（可选）
      sex: 1, // 性别（0-女，1-男）
      bio: '' // 个人简介
    },
    sexOptions: ['女', '男'], // 性别选项
    sexIndex: 1, // 默认选中男
    passwordError: '', // 密码错误提示
    addressArray: [] // 地址数组形式（用于picker）
  },

  onLoad() {
    // 初始化用户 ID
    const userId = wx.getStorageSync('userId') || '';
    this.setData({ userId });
    
    // 加载用户信息
    this.loadUserInfo();
  },

  // 加载用户信息
  loadUserInfo() {
    const { userId } = this.data;
    if (!userId) {
      wx.showToast({
        title: '未登录',
        icon: 'none'
      });
      return;
    }

    wx.showLoading({ title: '加载中...' });
    
    wx.request({
      url: `http://localhost:8080/api/user/${userId}`,
      method: 'GET',
      success: (res) => {
        wx.hideLoading();
        if (res.statusCode === 200 && res.data.user) {
          const user = res.data.user;
          // 处理地址，将字符串转为数组
          const addressArray = user.address ? user.address.split('-') : [];
          
          // 处理生日，只显示年月日（YYYY-MM-DD）
          let birthday = '';
          if (user.birthday) {
            // 如果是时间戳或完整日期时间格式，只取日期部分
            const dateObj = new Date(user.birthday);
            if (!isNaN(dateObj.getTime())) {
              const year = dateObj.getFullYear();
              const month = String(dateObj.getMonth() + 1).padStart(2, '0');
              const day = String(dateObj.getDate()).padStart(2, '0');
              birthday = `${year}-${month}-${day}`;
            } else {
              birthday = user.birthday.split(' ')[0]; // 如果已经是字符串，取空格前的部分
            }
          }
          
          this.setData({
            formData: {
              name: user.name || '',
              password: '', // 密码不显示
              birthday: birthday,
              address: user.address || '',
              school: user.school || '',
              qq: user.qq || '',
              wechat: user.wechat || '',
              sex: user.sex || 1,
              bio: user.bio || ''
            },
            sexIndex: user.sex || 1,
            addressArray: addressArray
          });
        } else {
          wx.showToast({
            title: '获取用户信息失败',
            icon: 'none'
          });
        }
      },
      fail: () => {
        wx.hideLoading();
        wx.showToast({
          title: '网络请求失败',
          icon: 'none'
        });
      }
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
    const selectedAddress = e.detail.value; // 获取用户选择的地址数组
    this.setData({
      'formData.address': selectedAddress.join('-'), // 将省市区拼接成字符串
      addressArray: selectedAddress // 保存数组形式用于显示
    });
  },

  // 性别选择器变化
  onSexChange(e) {
    const index = parseInt(e.detail.value);
    this.setData({
      sexIndex: index,
      'formData.sex': index // 0-女，1-男
    });
  },

  // 更新按钮点击事件
  update() {
    const { formData, userId } = this.data;

    // 验证必填项
    if (!formData.name || !formData.name.trim()) {
      wx.showToast({
        title: '请输入姓名',
        icon: 'none'
      });
      return;
    }

    // 如果填写了密码，验证密码长度
    if (formData.password && formData.password.length > 0 && formData.password.length < 6) {
      this.setData({ passwordError: '密码长度至少为6位' });
      return;
    }

    // 构造后端期望的数据结构
    const postData = {
      userId: parseInt(userId),
      name: formData.name.trim(),
      birthday: formData.birthday,
      address: formData.address,
      school: formData.school ? formData.school.trim() : '',
      qq: formData.qq ? formData.qq.trim() : '',
      wechat: formData.wechat ? formData.wechat.trim() : '',
      sex: formData.sex,
      bio: formData.bio ? formData.bio.trim() : ''
    };

    // 只有在用户输入了新密码时才更新密码
    if (formData.password && formData.password.trim()) {
      postData.password = formData.password;
    }

    // 显示加载提示
    wx.showLoading({ title: '更新中...' });

    // 发送更新请求
    wx.request({
      url: 'http://localhost:8080/api/user/update',
      method: 'PUT',
      header: { 'content-type': 'application/json' },
      data: postData,
      success: (res) => {
        wx.hideLoading();

        if (res.statusCode === 200 && res.data === "更新成功") {
          wx.showToast({
            title: '更新成功',
            icon: 'success',
            duration: 1500,
            success: () => {
              // 1.5秒后自动返回上级页面
              setTimeout(() => {
                wx.navigateBack({
                  delta: 1
                });
              }, 1500);
            }
          });
        } else {
          wx.showToast({
            title: res.data || '更新失败，请重试',
            icon: 'none'
          });
        }
      },
      fail: (err) => {
        wx.hideLoading();
        wx.showToast({
          title: '网络请求失败，请稍后再试',
          icon: 'none'
        });
        console.error('更新失败:', err);
      }
    });
  }
});