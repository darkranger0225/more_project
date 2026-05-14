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
    classId: null,
    parentList: [],
    parentArray: [],
    parentIndex: -1,
    selectedParent: null,
    parentId: null,
    genderArray: ['女', '男'],
    genderIndex: -1,
    gender: null,
    birthDate: ''
  },

  onLoad(options) {
    this.loadClassList();
    this.loadParentList();
    if (options.mode === 'edit' && options.id) {
      this.setData({ isEdit: true, studentId: options.id });
      this.loadStudentDetail(options.id);
    }
  },

  async loadClassList() {
    try {
      const res = await app.request({ url: '/class/list' });
      if (res.code === 200) {
        this.setData({ classList: res.data, classArray: res.data });
      }
    } catch (error) {}
  },

  async loadParentList() {
    try {
      const res = await app.request({ url: '/user/list', data: { role: 'PARENT' } });
      if (res.code === 200) {
        this.setData({ parentList: res.data, parentArray: res.data });
      }
    } catch (error) {}
  },

  async loadStudentDetail(id) {
    try {
      const res = await app.request({ url: `/student/${id}` });
      if (res.code === 200) {
        const student = res.data;
        let classIndex = -1, selectedClass = null;
        if (student.classId) {
          classIndex = this.data.classList.findIndex(c => c.id === student.classId);
          selectedClass = this.data.classList[classIndex];
        }
        let parentIndex = -1, selectedParent = null;
        if (student.parentId) {
          parentIndex = this.data.parentList.findIndex(p => p.id === student.parentId);
          selectedParent = this.data.parentList[parentIndex];
        }
        let genderIndex = student.gender !== null ? student.gender : -1;
        this.setData({
          studentName: student.studentName,
          studentNo: student.studentNo || '',
          classIndex, selectedClass, classId: student.classId,
          parentIndex, selectedParent, parentId: student.parentId,
          genderIndex, gender: student.gender,
          birthDate: student.birthDate || ''
        });
      }
    } catch (error) { wx.showToast({ title: '加载学生信息失败', icon: 'none' }); }
  },

  onStudentNameInput(e) { this.setData({ studentName: e.detail.value }); },
  onStudentNoInput(e) { this.setData({ studentNo: e.detail.value }); },
  onClassChange(e) {
    const index = e.detail.value;
    const cls = this.data.classList[index];
    this.setData({ classIndex: index, selectedClass: cls, classId: cls.id });
  },
  onParentChange(e) {
    const index = e.detail.value;
    const parent = this.data.parentList[index];
    this.setData({ parentIndex: index, selectedParent: parent, parentId: parent.id });
  },
  onGenderChange(e) {
    const index = e.detail.value;
    this.setData({ genderIndex: index, gender: parseInt(index) });
  },
  onBirthDateChange(e) { this.setData({ birthDate: e.detail.value }); },

  async saveStudent() {
    const { isEdit, studentId, studentName, studentNo, classId, parentId, gender, birthDate } = this.data;
    if (!studentName.trim()) { wx.showToast({ title: '请输入学生姓名', icon: 'none' }); return; }
    wx.showLoading({ title: isEdit ? '保存中...' : '添加中...' });
    try {
      const data = {
        studentName: studentName.trim(),
        studentNo: studentNo.trim() || null,
        classId, parentId, gender, birthDate: birthDate || null
      };
      let res;
      if (isEdit) {
        data.id = studentId;
        res = await app.request({ url: '/student', method: 'PUT', data });
      } else {
        res = await app.request({ url: '/student', method: 'POST', data });
      }
      wx.hideLoading();
      if (res.code === 200) {
        wx.showToast({ title: isEdit ? '修改成功' : '添加成功', icon: 'success' });
        setTimeout(() => { wx.navigateBack(); }, 1500);
      }
    } catch (error) {
      wx.hideLoading();
      let errorMsg = isEdit ? '修改失败' : '添加失败';
      if (error.message) {
        if (error.message.includes('Duplicate entry') && error.message.includes('student_no')) {
          errorMsg = '学号已存在，请更换学号';
        } else if (error.message.includes('Duplicate')) {
          errorMsg = '数据重复，请检查输入';
        } else {
          errorMsg = error.message;
        }
      }
      wx.showToast({ title: errorMsg, icon: 'none', duration: 3000 });
    }
  }
});