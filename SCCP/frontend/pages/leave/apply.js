const app = getApp();

Page({
  data: {
    studentList: [],
    studentIndex: -1,
    selectedStudent: null,
    studentId: null,
    leaveTypeArray: ['病假', '事假', '其他'],
    leaveTypeIndex: -1,
    leaveType: '',
    startDate: '',
    endDate: '',
    days: 0,
    reason: ''
  },

  onLoad() {
    this.loadMyStudents();
    // 设置默认开始日期为今天
    const today = new Date();
    const year = today.getFullYear();
    const month = (today.getMonth() + 1).toString().padStart(2, '0');
    const day = today.getDate().toString().padStart(2, '0');
    this.setData({
      startDate: `${year}-${month}-${day}`
    });
  },

  // 加载我的孩子列表
  async loadMyStudents() {
    try {
      const res = await app.request({
        url: '/student/my-students'
      });

      if (res.code === 200) {
        this.setData({
          studentList: res.data
        });
      }
    } catch (error) {
      wx.showToast({
        title: '加载孩子列表失败',
        icon: 'none'
      });
    }
  },

  onStudentChange(e) {
    const index = e.detail.value;
    const student = this.data.studentList[index];
    this.setData({
      studentIndex: index,
      selectedStudent: student,
      studentId: student.id
    });
  },

  onLeaveTypeChange(e) {
    const index = e.detail.value;
    const typeMap = ['SICK', 'PERSONAL', 'OTHER'];
    this.setData({
      leaveTypeIndex: index,
      leaveType: this.data.leaveTypeArray[index],
      leaveTypeCode: typeMap[index]
    });
  },

  onStartDateChange(e) {
    const startDate = e.detail.value;
    this.setData({ startDate });
    this.calculateDays(startDate, this.data.endDate);
  },

  onEndDateChange(e) {
    const endDate = e.detail.value;
    this.setData({ endDate });
    this.calculateDays(this.data.startDate, endDate);
  },

  calculateDays(startDate, endDate) {
    if (startDate && endDate) {
      const start = new Date(startDate);
      const end = new Date(endDate);
      const diffTime = end - start;
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1;
      this.setData({
        days: diffDays > 0 ? diffDays : 0
      });
    }
  },

  onReasonInput(e) {
    this.setData({ reason: e.detail.value });
  },

  async submitLeave() {
    const { studentId, leaveTypeCode, startDate, endDate, days, reason } = this.data;

    if (!studentId) {
      wx.showToast({ title: '请选择孩子', icon: 'none' });
      return;
    }

    if (!leaveTypeCode) {
      wx.showToast({ title: '请选择请假类型', icon: 'none' });
      return;
    }

    if (!startDate) {
      wx.showToast({ title: '请选择开始日期', icon: 'none' });
      return;
    }

    if (!endDate) {
      wx.showToast({ title: '请选择结束日期', icon: 'none' });
      return;
    }

    if (days <= 0) {
      wx.showToast({ title: '结束日期不能早于开始日期', icon: 'none' });
      return;
    }

    if (!reason.trim()) {
      wx.showToast({ title: '请输入请假原因', icon: 'none' });
      return;
    }

    wx.showLoading({ title: '提交中...' });

    try {
      const res = await app.request({
        url: '/leave',
        method: 'POST',
        data: {
          studentId,
          leaveType: leaveTypeCode,
          startDate,
          endDate,
          days,
          reason: reason.trim()
        }
      });

      wx.hideLoading();

      if (res.code === 200) {
        wx.showToast({
          title: '申请成功',
          icon: 'success'
        });
        setTimeout(() => {
          wx.navigateBack();
        }, 1500);
      } else {
        wx.showToast({
          title: res.message || '申请失败',
          icon: 'none'
        });
      }
    } catch (error) {
      wx.hideLoading();
      wx.showToast({
        title: error.message || '申请失败',
        icon: 'none'
      });
    }
  }
});
