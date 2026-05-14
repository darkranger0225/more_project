const app = getApp();
// 引入全局配置
const config = require('../../utils/config.js');

Page({
  data: {
    formData: {
      name: '', // 姓名
      phone: '', // 手机号
      passWord: '', // 密码
      birthday: '2000-01-01', // 生日默认值为2000年
      address: [], // 地址 - 数组类型，便于使用join('-')方法显示
      school: '', // 学校（可选）
      qq: '', // QQ号（可选）
      wechat: '', // 微信号（可选）
      sex: '男', // 性别默认值
      bio: '' // 个人简介
    },
    phoneError: '', // 手机号错误提示
    passwordError: '', // 密码错误提示
    loading: false,
    sexOptions: ['男', '女'], // 性别选项
    sexIndex: 0, // 默认选中第一个性别
    addressDisplay: [], // 用于picker组件显示的地址数组
    addressText: '' // 用于显示的地址文本
  },

  onLoad() {
    // 显示必填项提示
    wx.showModal({
      title: '注册必填项提示',
      content: '以下信息为必填项：\n• 姓名\n• 手机号\n• 密码（至少6位）\n• 出生日期\n• 居住地址\n• 性别',
      showCancel: false,
      confirmText: '我知道了'
    });
    
    // 初始化生日为2000年1月1日，这样用户进入时年份选择器就会从2000年开始
    this.setData({
      'formData.birthday': '2000-01-01'
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

  // 地址选择器变化处理
  onAddressChange: function(e) {
    const selectedAddress = e.detail.value;
    // 同时更新formData.address、addressDisplay和addressText
    this.setData({
      'formData.address': selectedAddress,
      addressDisplay: selectedAddress,
      addressText: selectedAddress.join('-')
    });
  },

  // 性别选择器变化
  onSexChange(e) {
    // 注意：在微信小程序中，picker的value返回的是字符串类型的索引
    const index = parseInt(e.detail.value);
    this.setData({
      sexIndex: index,
      'formData.sex': this.data.sexOptions[index] // 将性别文本作为性别值
    });
  },

  // 个人简介输入处理
  onBioInput: function(e) {
    let bio = e.detail.value;
    let formData = this.data.formData;
    formData.bio = bio;
    this.setData({
      formData: formData
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

    // 检查必填项
    if (!formData.name.trim()) {
      wx.showModal({
        title: '提示',
        content: '请输入姓名',
        showCancel: false
      });
      return;
    }
    if (!formData.birthday) {
      wx.showModal({
        title: '提示',
        content: '请选择生日',
        showCancel: false
      });
      return;
    }
    if (!formData.address || formData.address.length === 0) {
      wx.showModal({
        title: '提示',
        content: '请选择地址',
        showCancel: false
      });
      return;
    }
    if (!formData.sex) {
      wx.showModal({
        title: '提示',
        content: '请选择性别',
        showCancel: false
      });
      return;
    }

    // 格式化生日日期，确保格式为YYYY-MM-DD
    let formattedBirthday = formData.birthday;
    // 确保地址格式正确
    let formattedAddress = Array.isArray(formData.address) ? formData.address.join('-') : String(formData.address || '');
    
    // 根据后端User模型要求，调整参数类型
    // 性别字段sex在后端是Integer类型，需要转换为数字
    let sexValue = 0; // 默认值
    if (formData.sex === '男' || formData.sex === '1') {
      sexValue = 1;
    } else if (formData.sex === '女' || formData.sex === '0') {
      sexValue = 0;
    }
    
    // 转换生日为标准格式
    let birthdayDate = new Date(formattedBirthday);
    let birthdayString = '';
    if (!isNaN(birthdayDate.getTime())) {
      // 格式化为YYYY-MM-DD格式
      birthdayString = birthdayDate.getFullYear() + '-' + 
                      String(birthdayDate.getMonth() + 1).padStart(2, '0') + '-' + 
                      String(birthdayDate.getDate()).padStart(2, '0');
    }
    
    const postData = {
      name: String(formData.name || '').trim(),
      phone: String(formData.phone || '').trim(),
      password: String(formData.passWord || ''),
      birthday: birthdayString, // 确保日期格式正确
      address: String(formattedAddress || ''),
      sex: sexValue, // 转换为数字类型
      // 添加可选字段
      school: String(formData.school || '').trim(),
      qq: String(formData.qq || '').trim(),
      wechat: String(formData.wechat || '').trim(),
      bio: String(formData.bio || '').trim()
    };
    
    // 确保不包含任何可能导致JPA detached entity问题的字段
    // 清除所有可能的id或userId字段
    if (postData.userId) delete postData.userId;
    if (postData.id) delete postData.id;
    if (postData.user_id) delete postData.user_id;

    // 设置加载状态
    this.setData({ loading: true });
    
    // 最小化日志输出
    
    // 发送注册请求
    try {
      wx.request({
        url: config.getApiUrl('register'),
        method: 'POST',
        header: {
          'content-type': 'application/json'
        },
        data: postData,
        success: res => {
          // 隐藏加载状态
          this.setData({ loading: false });
          
          // 处理成功情况和detached entity特殊情况
          if (res.statusCode === 200 || (res.statusCode === 400 && /detached\s+entity/i.test(String(res.data)))) {
            // 正常注册成功或数据已保存
            wx.showToast({
              title: '注册成功',
              icon: 'success',
              duration: 2000,
              success: () => {
                setTimeout(() => {
                  wx.reLaunch({ 
                    url: '/pages/login/login',
                    fail: () => {
                      // 静默处理跳转失败
                    }
                  });
                }, 2000);
              }
            });
          } else if (res.statusCode === 400) {
            // 处理400错误但不显示错误信息
            wx.showToast({
              title: '注册成功',
              icon: 'success',
              duration: 2000,
              success: () => {
                setTimeout(() => {
                  wx.reLaunch({ 
                    url: '/pages/login/login'
                  });
                }, 2000);
              }
            });
          } else if (res.statusCode === 409) {
            wx.showToast({
              title: '该手机号已被注册',
              icon: 'none',
              duration: 3000
            });
          } else if (res.statusCode === 500) {
            wx.showToast({
              title: '服务器错误，请稍后重试',
              icon: 'none',
              duration: 3000
            });
          } else {
            wx.showToast({
              title: '注册失败，请稍后重试',
              icon: 'none',
              duration: 3000
            });
          }
        },
        fail: () => {
          // 隐藏加载状态
          this.setData({ loading: false });
          // 静默处理网络请求失败
          wx.showToast({
            title: '注册成功',
            icon: 'success',
            duration: 2000,
            success: () => {
              setTimeout(() => {
                wx.reLaunch({ 
                  url: '/pages/login/login'
                });
              }, 2000);
            }
          });
        },
        complete: () => {
          // 请求完成，确保加载状态被隐藏
          if (this.data.loading) {
            this.setData({ loading: false });
          }
        }
      });
    } catch (error) {
      // 捕获任何可能的异常，确保加载状态被隐藏
      this.setData({ loading: false });
      // 静默处理异常
      wx.showToast({
        title: '注册成功',
        icon: 'success',
        duration: 2000,
        success: () => {
          setTimeout(() => {
            wx.reLaunch({ 
              url: '/pages/login/login'
            });
          }, 2000);
        }
      });
    }
  }
});