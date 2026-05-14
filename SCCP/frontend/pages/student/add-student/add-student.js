const app = getApp();

Page({
  data: {
    isEdit: false,
    studentId: null,
    studentName: '',
    studentNo: '',
    classList: [],
    classArray: [],
    classIndex: -1,
    selectedClass: null,
    genderArray: ['女', '男'],
    genderIndex: -1,
    gender: null,
    birthDate: ''
  },

  onLoad(options) {
    // 加载班级列表
    this.loadClassList();

    // 如果是编辑模式
    if (options.mode === 'edit' && options.id) {
      this.setData({ isEdit: true, studentId: options.id });
      this.loadStudentDetail(options.id);
    }
  },

  async loadClassList() {
    try {
      const res = await app.request({
        url: '/class/list'
      });

      if (res.code === 200) {
        this.setData({
          classList: res.data,
          classArray: res.data
        });
      }
    } catch (error) {
      // 加载班级列表失败
    }
  },

  async loadStudentDetail(id) {
    try {
      const res = await app.request({
        url: `/student/${id}`
      });

      if (res.code === 200) {
        const student = res.data;
        
        // 找到班级索引
        let classIndex = -1;
        let selectedClass = null;
        if (student.classId) {
          const index = this.data.classList.findIndex(c => c.id === student.classId);
          if (index !== -1) {
            classIndex = index;
            selectedClass = this.data.classList[index];
          }
        }

        // 找到性别索引
        let genderIndex = -1;
        if (student.gender !== null) {
          genderIndex = student.gender;
        }

        this.setData({
          studentName: student.studentName,
          studentNo: student.studentNo || '',
          classIndex: classIndex,
          selectedClass: selectedClass,
          genderIndex: genderIndex,
          gender: student.gender,
          birthDate: student.birthDate || ''
        });
      }
    } catch (error) {
      wx.showToast({
        title: '加载学生信息失败',
        icon: 'none'
      });
    }
  },

  onStudentNameInput(e) {
    this.setData({ studentName: e.detail.value });
  },

  onStudentNoInput(e) {
    this.setData({ studentNo: e.detail.value });
  },

  onClassChange(e) {
    const index = e.detail.value;
    this.setData({
      classIndex: index,
      selectedClass: this.data.classList[index]
    });
  },

  onGenderChange(e) {
    const index = e.detail.value;
    this.setData({
      genderIndex: index,
      gender: parseInt(index)
    });
  },

  onBirthDateChange(e) {
    this.setData({ birthDate: e.detail.value });
  },

  async saveStudent() {
    const { isEdit, studentId, studentName, studentNo, selectedClass, gender, birthDate } = this.data;

    if (!studentName.trim()) {
      wx.showToast({ title: '请输入学生姓名', icon: 'none' });
      return;
    }

    wx.showLoading({ title: isEdit ? '保存中...' : '添加中...' });

    try {
      // 获取当前登录用户ID作为家长ID
      const userInfo = app.globalData.userInfo || wx.getStorageSync('userInfo');
      const parentId = userInfo ? userInfo.userId : null;

      const data = {
        studentName: studentName.trim(),
        studentNo: studentNo.trim() || null,
        classId: selectedClass ? selectedClass.id : null,
        parentId: parentId,
        gender: gender,
        birthDate: birthDate || null
      };

      let res;
      if (isEdit) {
        data.id = studentId;
        res = await app.request({
          url: '/student',
          method: 'PUT',
          data: data
        });
      } else {
        res = await app.request({
          url: '/student',
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
      } else {
        wx.showToast({
          title: res.message || (isEdit ? '修改失败' : '添加失败'),
          icon: 'none'
        });
      }
    } catch (error) {
      wx.hideLoading();
      let errorMsg = isEdit ? '修改失败' : '添加失败';
      if (error.message) {
        if (error.message.includes('Duplicate entry') && error.message.includes('student_no')) {
          errorMsg = '学号已存在，请更换学号';
        } else if (error.message.includes('Duplicate entry')) {
          errorMsg = '数据重复，请检查输入';
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