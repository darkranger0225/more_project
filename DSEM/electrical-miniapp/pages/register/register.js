const request = require('../../utils/request.js');

Page({
  data: {
    account: '',
    password: '',
    passwordStrength: '',
    passwordStrengthColor: '',
    name: '',
    buildingId: null,
    buildingIndex: null,
    buildingList: [],
    floorId: null,
    floorIndex: null,
    floorList: [],
    dormitoryId: null,
    dormitoryIndex: null,
    dormitoryList: [],
    gender: 0,
    genderList: ['未知', '男', '女'],
    age: '',
    phone: ''
  },

  onLoad() {
    this.loadBuildingList();
  },

  // 加载楼栋列表
  loadBuildingList() {
    request.get('/api/building/list').then(res => {
      this.setData({
        buildingList: res.data || []
      });
    });
  },

  // 加载楼层列表
  loadFloorList(buildingId) {
    if (!buildingId) return;
    request.get(`/api/building/${buildingId}/floor`).then(res => {
      this.setData({
        floorList: res.data || []
      });
    });
  },

  // 加载宿舍列表
  loadDormitoryList(floorId) {
    if (!floorId) return;
    request.get(`/api/building/floor/${floorId}/dormitory`).then(res => {
      this.setData({
        dormitoryList: res.data || []
      });
    });
  },

  // 账号输入
  onAccountInput(e) {
    this.setData({ account: e.detail.value });
  },

  // 密码输入并检查强度
  onPasswordInput(e) {
    const password = e.detail.value;
    const strength = this.checkPasswordStrength(password);
    this.setData({
      password: password,
      passwordStrength: strength.level,
      passwordStrengthColor: strength.color
    });
  },

  // 检查密码强度
  checkPasswordStrength(password) {
    if (!password || password.length < 6) {
      return { level: '至少六位', color: '#ff4d4f' };
    }
    
    let score = 0;
    // 长度加分
    if (password.length >= 8) score += 1;
    if (password.length >= 12) score += 1;
    // 包含数字
    if (/\d/.test(password)) score += 1;
    // 包含小写字母
    if (/[a-z]/.test(password)) score += 1;
    // 包含大写字母
    if (/[A-Z]/.test(password)) score += 1;
    // 包含特殊字符
    if (/[^a-zA-Z0-9]/.test(password)) score += 1;

    if (score <= 2) {
      return { level: '弱', color: '#ff4d4f' };
    } else if (score <= 4) {
      return { level: '中', color: '#faad14' };
    } else {
      return { level: '强', color: '#52c41a' };
    }
  },

  // 姓名输入
  onNameInput(e) {
    this.setData({ name: e.detail.value });
  },

  // 楼栋选择
  bindBuildingChange(e) {
    const index = e.detail.value;
    const building = this.data.buildingList[index];
    this.setData({
      buildingIndex: index,
      buildingId: building.id,
      floorId: null,
      floorIndex: null,
      dormitoryId: null,
      dormitoryIndex: null,
      floorList: [],
      dormitoryList: []
    });
    // 加载楼层列表
    this.loadFloorList(building.id);
  },

  // 楼层选择
  bindFloorChange(e) {
    const index = e.detail.value;
    const floor = this.data.floorList[index];
    if (!floor) {
      console.error('楼层数据不存在，index:', index, 'floorList:', this.data.floorList);
      return;
    }
    this.setData({
      floorIndex: index,
      floorId: floor.id,
      dormitoryId: null,
      dormitoryIndex: null
    });
    // 加载宿舍列表
    this.loadDormitoryList(floor.id);
  },

  // 宿舍选择
  bindDormitoryChange(e) {
    const index = e.detail.value;
    const dormitory = this.data.dormitoryList[index];
    if (!dormitory) {
      console.error('宿舍数据不存在，index:', index, 'dormitoryList:', this.data.dormitoryList);
      return;
    }
    this.setData({
      dormitoryIndex: index,
      dormitoryId: dormitory.id
    });
  },

  // 性别选择
  bindGenderChange(e) {
    this.setData({
      gender: parseInt(e.detail.value)
    });
  },

  // 年龄输入
  onAgeInput(e) {
    this.setData({ age: e.detail.value });
  },

  // 手机号输入
  onPhoneInput(e) {
    this.setData({ phone: e.detail.value });
  },

  // 表单验证
  validateForm() {
    const { account, password, name, floorId, dormitoryId, phone } = this.data;

    if (!account || account.length < 3) {
      wx.showToast({ title: '账号至少3位', icon: 'none' });
      return false;
    }

    if (!password || password.length < 6) {
      wx.showToast({ title: '密码至少6位', icon: 'none' });
      return false;
    }

    if (!name) {
      wx.showToast({ title: '请输入姓名', icon: 'none' });
      return false;
    }

    if (!floorId) {
      wx.showToast({ title: '请选择楼层', icon: 'none' });
      return false;
    }

    if (!dormitoryId) {
      wx.showToast({ title: '请选择宿舍', icon: 'none' });
      return false;
    }

    if (phone && !/^1[3-9]\d{9}$/.test(phone)) {
      wx.showToast({ title: '手机号格式不正确', icon: 'none' });
      return false;
    }

    return true;
  },

  handleRegister() {
    if (!this.validateForm()) return;

    const { account, password, name, floorId, dormitoryId, gender, age, phone } = this.data;

    request.post('/api/user/register', {
      account,
      password,
      name,
      floorId,
      dormitoryId,
      gender,
      age: age ? parseInt(age) : null,
      phone
    }).then(res => {
      wx.showToast({
        title: '注册成功',
        icon: 'success'
      });

      setTimeout(() => {
        wx.navigateBack();
      }, 1500);
    }).catch(err => {
      wx.showToast({
        title: err.message || '注册失败',
        icon: 'none'
      });
    });
  },

  goToLogin() {
    wx.navigateBack();
  }
});
