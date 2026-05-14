const app = getApp();

Page({
  data: {
    homeworkId: null,
    homeworkTitle: '',
    studentList: [],
    studentIndex: -1,
    selectedStudent: null,
    studentId: null,
    content: '',
    images: []
  },

  onLoad(options) {
    this.setData({
      homeworkId: options.id,
      homeworkTitle: decodeURIComponent(options.title || '')
    });
    this.loadMyStudents();
  },

  // 加载我的孩子列表
  async loadMyStudents() {
    try {
      const res = await app.request({
        url: '/student/my-students'
      });

      if (res.code === 200) {
        const students = res.data;
        this.setData({
          studentList: students
        });
        // 如果只有一个孩子，自动选中
        if (students.length === 1) {
          this.setData({
            studentIndex: 0,
            selectedStudent: students[0],
            studentId: students[0].id
          });
        }
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

  onContentInput(e) {
    this.setData({ content: e.detail.value });
  },

  // 选择图片
  chooseImage() {
    const remainingCount = 9 - this.data.images.length;
    wx.chooseImage({
      count: remainingCount,
      sizeType: ['compressed'],
      sourceType: ['album', 'camera'],
      success: (res) => {
        const newImages = [...this.data.images, ...res.tempFilePaths];
        this.setData({ images: newImages });
      }
    });
  },

  // 预览图片
  previewImage(e) {
    const src = e.currentTarget.dataset.src;
    wx.previewImage({
      current: src,
      urls: this.data.images
    });
  },

  // 删除图片
  deleteImage(e) {
    const index = e.currentTarget.dataset.index;
    const images = this.data.images.filter((_, i) => i !== index);
    this.setData({ images });
  },

  // 上传图片到服务器
  async uploadImages() {
    const { images } = this.data;
    if (images.length === 0) return [];

    const uploadedUrls = [];

    for (let i = 0; i < images.length; i++) {
      try {
        wx.showLoading({ title: `上传第${i + 1}/${images.length}张图片...` });
        const res = await this.uploadFile(images[i]);
        wx.hideLoading();
        if (res) {
          uploadedUrls.push(res);
        } else {
          throw new Error('上传返回空');
        }
      } catch (error) {
        wx.hideLoading();
        console.error('上传图片失败:', error);
        wx.showToast({
          title: `第${i + 1}张图片上传失败`,
          icon: 'none'
        });
        throw error; // 抛出错误，阻止提交
      }
    }

    return uploadedUrls;
  },

  // 单张图片上传
  uploadFile(filePath) {
    return new Promise((resolve, reject) => {
      console.log('开始上传图片:', filePath);
      wx.uploadFile({
        url: app.globalData.baseUrl + '/file/upload',
        filePath: filePath,
        name: 'file',
        header: {
          'Authorization': 'Bearer ' + app.globalData.token
        },
        success: (res) => {
          console.log('上传响应:', res);
          if (res.statusCode === 200) {
            const data = JSON.parse(res.data);
            console.log('上传响应数据:', data);
            if (data.code === 200) {
              console.log('上传成功,返回URL:', data.data);
              resolve(data.data);
            } else {
              reject(data.message);
            }
          } else {
            reject('上传失败');
          }
        },
        fail: (err) => {
          console.error('上传失败:', err);
          reject(err);
        }
      });
    });
  },

  // 提交作业
  async submitHomework() {
    const { homeworkId, studentId, content, images } = this.data;

    if (!studentId) {
      wx.showToast({ title: '请选择孩子', icon: 'none' });
      return;
    }

    if (images.length === 0) {
      wx.showToast({ title: '请至少上传一张图片', icon: 'none' });
      return;
    }

    wx.showLoading({ title: '提交中...' });

    try {
      // 先上传图片
      console.log('开始上传图片, images:', images);
      const imageUrls = await this.uploadImages();
      console.log('上传完成, imageUrls:', imageUrls);

      // 检查是否所有图片都上传成功
      if (imageUrls.length === 0 || imageUrls.length !== images.length) {
        wx.hideLoading();
        wx.showToast({ title: '图片上传失败，请重试', icon: 'none' });
        return;
      }

      // 提交作业
      const res = await app.request({
        url: '/homework/submit',
        method: 'POST',
        data: {
          homeworkId,
          studentId,
          content: content.trim(),
          attachmentUrl: imageUrls.join(',')
        }
      });

      wx.hideLoading();

      if (res.code === 200) {
        wx.showToast({
          title: '提交成功',
          icon: 'success'
        });
        setTimeout(() => {
          wx.navigateBack();
        }, 1500);
      } else {
        wx.showToast({
          title: res.message || '提交失败',
          icon: 'none'
        });
      }
    } catch (error) {
      wx.hideLoading();
      wx.showToast({
        title: error.message || '提交失败',
        icon: 'none'
      });
    }
  }
});
