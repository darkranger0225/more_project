const app = getApp();

Page({
  data: {
    isEdit: false,
    userId: null,
    username: '',
    password: '',
    realName: '',
    phone: '',
    roleArray: ['家长', '教师', '管理员'],
    roleIndex: -1,
    role: '',
    statusArray: ['启用', '禁用'],
    statusIndex: 0,
    status: 1
  },

  onLoad(options) {
    // 如果是编辑模式
    if (options.mode === 'edit' && options.id) {
      this.setData({ isEdit: true, userId: options.id });
      this.loadUserDetail(options.id);
    }
  },

  async loadUserDetail(id) {
    try {
      const res = await app.request({
        url: `/user/${id}`
      });

      if (res.code === 200) {
        const user = res.data;
        
        // 找到角色索引
        let roleIndex = -1;
        let role = '';
        switch(user.role) {
          case 'PARENT':
            roleIndex = 0;
            role = 'PARENT';
            break;
          case 'TEACHER':
            roleIndex = 1;
            role = 'TEACHER';
            break;
          case 'ADMIN':
            roleIndex = 2;
            role = 'ADMIN';
            break;
        }

        this.setData({
          username: user.username,
          realName: user.realName,
          phone: user.phone || '',
          roleIndex: roleIndex,
          role: role,
          statusIndex: user.status === 1 ? 0 : 1,
          status: user.status
        });
      }
    } catch (error) {
      wx.showToast({
        title: '加载用户信息失败',
        icon: 'none'
      });
    }
  },

  onUsernameInput(e) {
    this.setData({ username: e.detail.value });
  },

  onPasswordInput(e) {
    this.setData({ password: e.detail.value });
  },

  onRealNameInput(e) {
    this.setData({ realName: e.detail.value });
  },

  onPhoneInput(e) {
    this.setData({ phone: e.detail.value });
  },

  onRoleChange(e) {
    const index = e.detail.value;
    const roleMap = ['PARENT', 'TEACHER', 'ADMIN'];
    this.setData({
      roleIndex: index,
      role: roleMap[index]
    });
  },

  onStatusChange(e) {
    const index = e.detail.value;
    this.setData({
      statusIndex: index,
      status: index === 0 ? 1 : 0
    });
  },

  async saveUser() {
    const { isEdit, userId, username, password, realName, phone, role, status } = this.data;

    // 表单验证
    if (!username.trim()) {
      wx.showToast({ title: '请输入用户名', icon: 'none' });
      return;
    }

    if (username.length < 3) {
      wx.showToast({ title: '用户名至少3位', icon: 'none' });
      return;
    }

    if (!isEdit && !password) {
      wx.showToast({ title: '请输入密码', icon: 'none' });
      return;
    }

    if (!isEdit && password.length < 6) {
      wx.showToast({ title: '密码至少6位', icon: 'none' });
      return;
    }

    if (!realName.trim()) {
      wx.showToast({ title: '请输入真实姓名', icon: 'none' });
      return;
    }

    if (!role) {
      wx.showToast({ title: '请选择角色', icon: 'none' });
      return;
    }

    wx.showLoading({ title: isEdit ? '保存中...' : '添加中...' });

    try {
      const data = {
        username: username.trim(),
        realName: realName.trim(),
        phone: phone.trim() || null,
        role: role,
        status: status
      };

      // 编辑时，只有输入了新密码才更新
      if (!isEdit) {
        data.password = password;
      }

      let res;
      if (isEdit) {
        data.id = userId;
        res = await app.request({
          url: '/user',
          method: 'PUT',
          data: data
        });
      } else {
        res = await app.request({
          url: '/user',
          method: 'POST',
          data: data
        });
      }

      wx.hideLoading();

      if (res.code === 200) {
        wx.showToast({
          title: isEdit ? '修改成功' : '添加成功',
          icon: 'success'
        });

        setTimeout(() => {
          wx.navigateBack();
        }, 1500);
      }
    } catch (error) {
      wx.hideLoading();
      let errorMsg = isEdit ? '修改失败' : '添加失败';
      if (error.message) {
        if (error.message.includes('Duplicate') || error.message.includes('已存在')) {
          errorMsg = '用户名已存在';
        } else {
          errorMsg = error.message;
        }
      }
      wx.showToast({
        title: errorMsg,
        icon: 'none',
        duration: 3000
      });
    }
  }
});