const app = getApp();

Page({
  data: {
    role: '',
    homeworkId: null,
    homework: {},
    isExpired: false,
    mySubmit: null,
    myStudents: []
  },

  onLoad(options) {
    const role = app.globalData.role || wx.getStorageSync('userInfo')?.role;
    this.setData({
      role,
      homeworkId: options.id
    });

    if (options.id) {
      this.loadHomeworkDetail(options.id);
      if (role === 'PARENT') {
        this.loadMyStudents();
      } else if (role === 'TEACHER' && options.studentId) {
        // 教师查看指定学生的提交
        this.loadStudentSubmit(options.studentId);
      }
    }
  },

  async loadHomeworkDetail(id) {
    try {
      const res = await app.request({
        url: `/homework/${id}`
      });

      if (res.code === 200) {
        const homework = res.data;
        const deadline = new Date(homework.deadline);
        const now = new Date();

        this.setData({
          homework: {
            ...homework,
            deadline: this.formatDateTime(homework.deadline)
          },
          isExpired: deadline < now
        });
      }
    } catch (error) {
      wx.showToast({
        title: '加载作业详情失败',
        icon: 'none'
      });
    }
  },

  // 加载我的孩子列表
  async loadMyStudents() {
    try {
      const res = await app.request({
        url: '/student/my-students'
      });

      if (res.code === 200) {
        const students = res.data;
        this.setData({ myStudents: students });
        // 加载每个孩子的提交情况
        for (const student of students) {
          await this.loadStudentSubmit(student.id);
        }
      }
    } catch (error) {
      console.error('加载孩子列表失败', error);
    }
  },

  // 加载孩子的提交详情
  async loadStudentSubmit(studentId) {
    const { homeworkId } = this.data;
    try {
      const res = await app.request({
        url: `/homework/${homeworkId}/submit/${studentId}`
      });

      if (res.code === 200 && res.data) {
        const submit = res.data;
        // 格式化提交时间
        if (submit.submitTime) {
          submit.submitTime = this.formatDateTime(submit.submitTime);
        }
        // 处理图片URL，拼接 baseUrl
        if (submit.attachmentUrl) {
          submit.attachmentUrl = this.processImageUrls(submit.attachmentUrl);
          // 将图片URL转换为数组
          submit.imageList = submit.attachmentUrl.split(',').map(url => url.trim());
        }
        this.setData({
          mySubmit: submit
        });
      }
    } catch (error) {
      console.error('加载提交详情失败', error);
    }
  },

  // 处理图片URL，将相对路径转换为完整URL
  processImageUrls(urls) {
    if (!urls) return '';
    const baseUrl = app.globalData.baseUrl;
    
    return urls.split(',').map(url => {
      url = url.trim();
      // 如果已经是完整URL，直接返回
      if (url.startsWith('http://') || url.startsWith('https://')) {
        return url;
      }
      // 根据baseUrl拼接完整URL
      return url.startsWith('/') ? baseUrl + url : baseUrl + '/' + url;
    }).join(',');
  },

  formatDateTime(dateTimeStr) {
    if (!dateTimeStr) return '';
    const date = new Date(dateTimeStr);
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');
    return `${month}-${day} ${hours}:${minutes}`;
  },

  downloadAttachment() {
    const url = this.data.homework.attachmentUrl;
    if (url) {
      wx.showToast({
        title: '开始下载...',
        icon: 'none'
      });
      // 实际下载逻辑
    }
  },

  // 预览提交的图片
  previewImage(e) {
    const src = e.currentTarget.dataset.src;
    const urls = this.data.mySubmit.attachmentUrl.split(',');
    wx.previewImage({
      current: src,
      urls: urls
    });
  },

  goToSubmit() {
    const { homeworkId, homework } = this.data;
    wx.navigateTo({
      url: `/pages/homework/submit?id=${homeworkId}&title=${encodeURIComponent(homework.title)}`
    });
  },

  viewSubmissions() {
    const { homeworkId, homework } = this.data;
    wx.navigateTo({
      url: `/pages/homework/submissions?id=${homeworkId}&title=${encodeURIComponent(homework.title)}`
    });
  }
});
