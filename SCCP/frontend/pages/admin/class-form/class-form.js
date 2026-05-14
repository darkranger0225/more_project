const app = getApp();

Page({
  data: {
    isEdit: false,
    classId: null,
    className: '',
    gradeArray: ['一年级', '二年级', '三年级', '四年级', '五年级', '六年级', '七年级', '八年级', '九年级', '高一', '高二', '高三'],
    gradeIndex: -1,
    grade: '',
    teacherList: [],
    teacherArray: [],
    teacherIndex: -1,
    selectedTeacher: null,
    headTeacherId: null,
    description: '',
    classTeachers: [] // 班级任课教师列表
  },

  onLoad(options) {
    this.loadTeacherList();
    if (options.mode === 'edit' && options.id) {
      this.setData({ isEdit: true, classId: options.id });
      this.loadClassDetail(options.id);
      this.loadClassTeachers(options.id);
    }
  },

  async loadTeacherList() {
    try {
      const res = await app.request({
        url: '/user/list',
        data: { role: 'TEACHER' }
      });
      if (res.code === 200) {
        this.setData({
          teacherList: res.data,
          teacherArray: res.data
        });
      }
    } catch (error) {
      // 加载教师列表失败
    }
  },

  async loadClassDetail(id) {
    try {
      const res = await app.request({ url: `/class/${id}` });
      if (res.code === 200) {
        const cls = res.data;
        const gradeIndex = this.data.gradeArray.indexOf(cls.grade);
        let teacherIndex = -1;
        let selectedTeacher = null;
        if (cls.headTeacherId) {
          teacherIndex = this.data.teacherList.findIndex(t => t.id === cls.headTeacherId);
          selectedTeacher = this.data.teacherList[teacherIndex];
        }
        this.setData({
          className: cls.className,
          gradeIndex: gradeIndex,
          grade: cls.grade,
          teacherIndex: teacherIndex,
          selectedTeacher: selectedTeacher,
          headTeacherId: cls.headTeacherId,
          description: cls.description || ''
        });
      }
    } catch (error) {
      wx.showToast({ title: '加载班级信息失败', icon: 'none' });
    }
  },

  onClassNameInput(e) { this.setData({ className: e.detail.value }); },
  onGradeChange(e) {
    const index = e.detail.value;
    this.setData({ gradeIndex: index, grade: this.data.gradeArray[index] });
  },
  onTeacherChange(e) {
    const index = e.detail.value;
    const teacher = this.data.teacherList[index];
    this.setData({ teacherIndex: index, selectedTeacher: teacher, headTeacherId: teacher.id });
  },
  onDescriptionInput(e) { this.setData({ description: e.detail.value }); },

  async saveClass() {
    const { isEdit, classId, className, grade, headTeacherId, description } = this.data;
    if (!className.trim()) { wx.showToast({ title: '请输入班级名称', icon: 'none' }); return; }
    if (!grade) { wx.showToast({ title: '请选择年级', icon: 'none' }); return; }
    wx.showLoading({ title: isEdit ? '保存中...' : '添加中...' });
    try {
      const data = { className: className.trim(), grade, headTeacherId, description: description.trim() };
      let res;
      if (isEdit) {
        data.id = classId;
        res = await app.request({ url: '/class', method: 'PUT', data });
      } else {
        res = await app.request({ url: '/class', method: 'POST', data });
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
        if (error.message.includes('Duplicate')) {
          errorMsg = '班级名称已存在';
        } else {
          errorMsg = error.message;
        }
      }
      wx.showToast({ title: errorMsg, icon: 'none', duration: 3000 });
    }
  },

  // 加载班级任课教师列表
  async loadClassTeachers(classId) {
    try {
      const res = await app.request({
        url: `/teacher-class/class/${classId}`
      });
      if (res.code === 200) {
        this.setData({
          classTeachers: res.data || []
        });
      }
    } catch (error) {
      console.error('加载班级教师失败', error);
    }
  },

  // 显示分配教师对话框
  showAssignTeacher() {
    console.log('showAssignTeacher called');
    const { teacherList, classTeachers } = this.data;
    console.log('teacherList:', teacherList, 'classTeachers:', classTeachers);

    // 过滤掉已分配的教师
    const assignedTeacherIds = classTeachers.map(t => t.teacherId);
    const availableTeachers = teacherList.filter(t => !assignedTeacherIds.includes(t.id));
    console.log('availableTeachers:', availableTeachers);

    if (availableTeachers.length === 0) {
      wx.showToast({ title: '所有教师已分配', icon: 'none' });
      return;
    }

    // 显示教师选择弹窗（最多6个）
    const teacherNames = availableTeachers.map(t => t.realName).slice(0, 6);
    console.log('teacherNames:', teacherNames);

    wx.showActionSheet({
      itemList: teacherNames,
      success: (res) => {
        console.log('教师选择成功:', res);
        const selectedTeacher = availableTeachers[res.tapIndex];
        // 选择教师后，选择科目
        this.showSubjectSelect(selectedTeacher.id);
      },
      fail: (err) => {
        console.error('教师选择失败:', err);
      }
    });
  },

  // 显示科目选择
  showSubjectSelect(teacherId) {
    // 科目列表，分为三批显示（每批不超过6个）
    const subjects1 = ['语文', '数学', '英语', '物理', '化学', '更多...'];
    const subjects2 = ['生物', '历史', '地理', '政治', '体育', '更多...'];
    const subjects3 = ['音乐', '美术', '其他'];

    wx.showActionSheet({
      itemList: subjects1,
      success: (res) => {
        if (res.tapIndex === 5) {
          // 选择了"更多..."，显示第二批
          wx.showActionSheet({
            itemList: subjects2,
            success: (res2) => {
              if (res2.tapIndex === 5) {
                // 选择了"更多..."，显示第三批
                wx.showActionSheet({
                  itemList: subjects3,
                  success: (res3) => {
                    const selectedSubject = subjects3[res3.tapIndex];
                    this.assignTeacher(teacherId, selectedSubject);
                  }
                });
              } else {
                const selectedSubject = subjects2[res2.tapIndex];
                this.assignTeacher(teacherId, selectedSubject);
              }
            }
          });
        } else {
          const selectedSubject = subjects1[res.tapIndex];
          this.assignTeacher(teacherId, selectedSubject);
        }
      }
    });
  },

  // 分配教师到班级
  async assignTeacher(teacherId, subject) {
    const { classId } = this.data;
    try {
      const res = await app.request({
        url: '/teacher-class',
        method: 'POST',
        data: {
          teacherId: teacherId,
          classId: classId,
          subject: subject
        }
      });
      if (res.code === 200) {
        wx.showToast({ title: '添加成功', icon: 'success' });
        this.loadClassTeachers(classId);
      } else {
        wx.showToast({ title: res.message || '添加失败', icon: 'none' });
      }
    } catch (error) {
      wx.showToast({ title: error.message || '添加失败', icon: 'none' });
    }
  },

  // 移除教师
  async removeTeacher(e) {
    const id = e.currentTarget.dataset.id;
    const { classId } = this.data;
    
    wx.showModal({
      title: '提示',
      content: '确定要移除该教师吗？',
      success: async (res) => {
        if (res.confirm) {
          try {
            const result = await app.request({
              url: `/teacher-class/${id}`,
              method: 'DELETE'
            });
            if (result.code === 200) {
              wx.showToast({ title: '移除成功', icon: 'success' });
              this.loadClassTeachers(classId);
            }
          } catch (error) {
            wx.showToast({ title: error.message || '移除失败', icon: 'none' });
          }
        }
      }
    });
  }
});