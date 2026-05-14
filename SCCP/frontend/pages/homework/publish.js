const app = getApp();

Page({
  data: {
    title: '',
    content: '',
    subjectArray: ['语文', '数学', '英语', '物理', '化学', '生物', '历史', '地理', '政治', '体育', '音乐', '美术', '其他'],
    subjectIndex: -1,
    subject: '',
    classList: [],
    classArray: [],
    classIndex: -1,
    selectedClass: null,
    classId: null,
    deadline: '',
    deadlineIndex: [0, 0, 0, 0],
    deadlineArray: []
  },

  onLoad() {
    this.loadTeacherClasses();
    this.initDeadlinePicker();
  },

  // 初始化截止时间选择器
  initDeadlinePicker() {
    const now = new Date();
    const years = [];
    const months = [];
    const days = [];
    const hours = [];

    // 年份（当前年到下一年）
    for (let i = now.getFullYear(); i <= now.getFullYear() + 1; i++) {
      years.push(i + '年');
    }

    // 月份
    for (let i = 1; i <= 12; i++) {
      months.push(i + '月');
    }

    // 日期（先初始化31天）
    for (let i = 1; i <= 31; i++) {
      days.push(i + '日');
    }

    // 小时
    for (let i = 0; i < 24; i++) {
      hours.push(i.toString().padStart(2, '0') + '时');
    }

    this.setData({
      deadlineArray: [years, months, days, hours]
    });
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

  onTitleInput(e) {
    this.setData({ title: e.detail.value });
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
  },

  onDeadlineColumnChange(e) {
    // 可以在这里处理日期联动，简化处理暂时不做
  },

  onDeadlineChange(e) {
    const value = e.detail.value;
    const year = this.data.deadlineArray[0][value[0]].replace('年', '');
    const month = this.data.deadlineArray[1][value[1]].replace('月', '').padStart(2, '0');
    const day = this.data.deadlineArray[2][value[2]].replace('日', '').padStart(2, '0');
    const hour = this.data.deadlineArray[3][value[3]].replace('时', '');

    const deadline = `${year}-${month}-${day}T${hour}:00:00`;
    this.setData({
      deadlineIndex: value,
      deadline: deadline
    });
  },

  onContentInput(e) {
    this.setData({ content: e.detail.value });
  },

  async publishHomework() {
    const { title, subject, classId, deadline, content } = this.data;

    if (!title.trim()) {
      wx.showToast({ title: '请输入作业标题', icon: 'none' });
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

    if (!deadline) {
      wx.showToast({ title: '请选择截止时间', icon: 'none' });
      return;
    }

    if (!content.trim()) {
      wx.showToast({ title: '请输入作业内容', icon: 'none' });
      return;
    }

    wx.showLoading({ title: '发布中...' });

    try {
      const res = await app.request({
        url: '/homework',
        method: 'POST',
        data: {
          title: title.trim(),
          subject,
          classId,
          deadline,
          content: content.trim()
        }
      });

      wx.hideLoading();

      if (res.code === 200) {
        wx.showToast({
          title: '发布成功',
          icon: 'success'
        });

        setTimeout(() => {
          wx.navigateBack();
        }, 1500);
      } else {
        wx.showToast({
          title: res.message || '发布失败',
          icon: 'none'
        });
      }
    } catch (error) {
      wx.hideLoading();
      wx.showToast({
        title: error.message || '发布失败',
        icon: 'none'
      });
    }
  }
});
