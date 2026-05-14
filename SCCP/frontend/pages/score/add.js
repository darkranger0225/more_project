const app = getApp();

Page({
  data: {
    examName: '',
    subjectArray: ['语文', '数学', '英语', '物理', '化学', '生物', '历史', '地理', '政治', '体育', '音乐', '美术', '其他'],
    subjectIndex: -1,
    subject: '',
    classList: [],
    classArray: [],
    classIndex: -1,
    selectedClass: null,
    classId: null,
    examDate: '',
    studentList: [],
    studentScores: {}
  },

  onLoad() {
    this.loadTeacherClasses();
  },

  // 加载教师所教的班级
  async loadTeacherClasses() {
    try {
      const res = await app.request({
        url: '/class/my-classes'
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

  // 加载班级学生列表
  async loadClassStudents(classId) {
    try {
      const res = await app.request({
        url: `/student/class/${classId}`
      });

      if (res.code === 200) {
        this.setData({
          studentList: res.data
        });
      }
    } catch (error) {
      wx.showToast({
        title: '加载学生列表失败',
        icon: 'none'
      });
    }
  },

  onExamNameInput(e) {
    this.setData({ examName: e.detail.value });
  },

  onSubjectChange(e) {
    const index = e.detail.value;
    this.setData({
      subjectIndex: index,
      subject: this.data.subjectArray[index]
    });
  },

  onClassChange(e) {
    const index = e.detail.value;
    const cls = this.data.classList[index];
    this.setData({
      classIndex: index,
      selectedClass: cls,
      classId: cls.id
    });
    // 加载该班级的学生
    this.loadClassStudents(cls.id);
  },

  onExamDateChange(e) {
    this.setData({ examDate: e.detail.value });
  },

  onScoreInput(e) {
    const studentId = e.currentTarget.dataset.studentid;
    const score = e.detail.value;
    this.setData({
      [`studentScores.${studentId}`]: score
    });
  },

  async saveScores() {
    const { examName, subject, classId, examDate, studentScores, studentList } = this.data;

    if (!examName.trim()) {
      wx.showToast({ title: '请输入考试名称', icon: 'none' });
      return;
    }

    if (!subject) {
      wx.showToast({ title: '请选择科目', icon: 'none' });
      return;
    }

    if (!classId) {
      wx.showToast({ title: '请选择班级', icon: 'none' });
      return;
    }

    if (!examDate) {
      wx.showToast({ title: '请选择考试日期', icon: 'none' });
      return;
    }

    // 收集有分数的学生
    const scores = [];
    studentList.forEach(student => {
      const score = studentScores[student.id];
      if (score !== undefined && score !== '') {
        scores.push({
          studentId: student.id,
          score: parseFloat(score)
        });
      }
    });

    if (scores.length === 0) {
      wx.showToast({ title: '请至少录入一个学生的成绩', icon: 'none' });
      return;
    }

    wx.showLoading({ title: '保存中...' });

    try {
      // 逐个保存学生成绩
      let successCount = 0;
      let failCount = 0;

      for (const item of scores) {
        try {
          const res = await app.request({
            url: '/score',
            method: 'POST',
            data: {
              examName: examName.trim(),
              subject,
              classId,
              examDate,
              studentId: item.studentId,
              score: item.score
            }
          });
          if (res.code === 200) {
            successCount++;
          } else {
            failCount++;
          }
        } catch (err) {
          failCount++;
        }
      }

      wx.hideLoading();

      if (failCount === 0) {
        wx.showToast({
          title: `成功录入${successCount}人`,
          icon: 'success'
        });
        setTimeout(() => {
          wx.navigateBack();
        }, 1500);
      } else {
        wx.showToast({
          title: `成功${successCount}人，失败${failCount}人`,
          icon: 'none'
        });
      }
    } catch (error) {
      wx.hideLoading();
      wx.showToast({
        title: error.message || '保存失败',
        icon: 'none'
      });
    }
  }
});
