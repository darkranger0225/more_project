const request = require('../../../../utils/request.js');

Page({
  data: {
    id: '',
    account: '',
    studentName: '',
    name: '',
    phone: '',
    gender: '1',
    age: '',
    status: 1,
    newPassword: '',
    confirmPassword: ''
  },

  onLoad(options) {
    const id = options.id;
    this.setData({ id });
    this.loadStudentInfo(id);
  },

  loadStudentInfo(id) {
    request.get(`/api/admin/student/${id}`).then(res => {
      const student = res.data;
      this.setData({
        account: student.account,
        studentName: student.name,
        name: student.name,
        phone: student.phone || '',
        gender: student.gender !== undefined ? student.gender.toString() : '1',
        age: student.age ? student.age.toString() : '',
        status: student.status || 1
      });
    });
  },

  selectGender(e) {
    const value = e.currentTarget.dataset.value;
    this.setData({
      gender: value
    });
  },

  toggleStatus(e) {
    this.setData({
      status: e.detail.value ? 1 : 0
    });
  },

  handleSubmit() {
    const { id, name, phone, gender, age, status, newPassword, confirmPassword } = this.data;

    if (!name) {
      wx.showToast({
        title: '请输入姓名',
        icon: 'none'
      });
      return;
    }

    // 验证手机号格式
    if (phone) {
      const phoneRegex = /^1[3-9]\d{9}$/;
      if (!phoneRegex.test(phone)) {
        wx.showToast({
          title: '手机号格式不正确',
          icon: 'none'
        });
        return;
      }
    }

    // 验证密码
    if (newPassword) {
      if (newPassword.length < 6) {
        wx.showToast({
          title: '密码至少6位',
          icon: 'none'
        });
        return;
      }
      if (newPassword.length > 20) {
        wx.showToast({
          title: '密码最多20位',
          icon: 'none'
        });
        return;
      }
      if (newPassword !== confirmPassword) {
        wx.showToast({
          title: '两次密码不一致',
          icon: 'none'
        });
        return;
      }
    }

    const updateData = {
      id,
      name,
      phone,
      gender: parseInt(gender),
      age: age ? parseInt(age) : null,
      status
    };

    // 如果有新密码，添加到更新数据中
    if (newPassword) {
      updateData.password = newPassword;
    }

    request.put('/api/admin/student/update', updateData).then(res => {
      wx.showToast({
        title: '保存成功',
        icon: 'success'
      });
      
      setTimeout(() => {
        wx.navigateBack();
      }, 1000);
    }).catch(err => {
      console.error('保存失败:', err);
    });
  }
});