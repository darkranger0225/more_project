const app = getApp();

Page({
  data: {
    title: '',
    content: '',
    targetType: 'ALL', // ALL: 全部, CLASS: 指定班级, PARENT: 指定家长
    targetTypeArray: [
      { value: 'ALL', name: '全部用户' },
      { value: 'CLASS', name: '指定班级' },
      { value: 'PARENT', name: '指定家长' }
    ],
    targetTypeIndex: 0,
    priority: 0, // 0: 普通, 1: 重要
    priorityArray: [
      { value: 0, name: '普通' },
      { value: 1, name: '重要' }
    ],
    priorityIndex: 0,
    classList: [],
    selectedClassIds: [],
    parentList: [],
    selectedParentIds: [],
    role: ''
  },

  onLoad() {
    const role = app.globalData.role || wx.getStorageSync('userInfo')?.role;
    this.setData({ role });
    
    if (role === 'TEACHER') {
      this.loadTeacherClasses();
    } else if (role === 'ADMIN') {
      this.loadAllClasses();
    }
  },

  // 加载教师任教的班级
  async loadTeacherClasses() {
    try {
      const res = await app.request({
        url: '/class/my-classes'
      });
      if (res.code === 200) {
        this.setData({ classList: res.data || [] });
      }
    } catch (error) {
      console.error('加载班级失败', error);
    }
  },

  // 加载所有班级（管理员）
  async loadAllClasses() {
    try {
      const res = await app.request({
        url: '/class/list'
      });
      if (res.code === 200) {
        this.setData({ classList: res.data || [] });
      }
    } catch (error) {
      console.error('加载班级失败', error);
    }
  },

  // 标题输入
  onTitleInput(e) {
    this.setData({ title: e.detail.value });
  },

  // 内容输入
  onContentInput(e) {
    this.setData({ content: e.detail.value });
  },

  // 目标类型选择
  onTargetTypeChange(e) {
    const index = parseInt(e.detail.value);
    const targetType = this.data.targetTypeArray[index].value;
    this.setData({
      targetTypeIndex: index,
      targetType: targetType,
      selectedClassIds: [],
      selectedParentIds: []
    });

    if (targetType === 'PARENT') {
      this.loadParentList();
    }
  },

  // 优先级选择
  onPriorityChange(e) {
    const index = parseInt(e.detail.value);
    this.setData({
      priorityIndex: index,
      priority: this.data.priorityArray[index].value
    });
  },

  // 加载家长列表
  async loadParentList() {
    try {
      const res = await app.request({
        url: '/user/list',
        data: { role: 'PARENT' }
      });
      if (res.code === 200) {
        this.setData({ parentList: res.data || [] });
      }
    } catch (error) {
      console.error('加载家长列表失败', error);
    }
  },

  // 班级选择
  onClassSelect(e) {
    const classId = parseInt(e.currentTarget.dataset.id);
    const { selectedClassIds } = this.data;
    const index = selectedClassIds.indexOf(classId);
    
    if (index > -1) {
      selectedClassIds.splice(index, 1);
    } else {
      selectedClassIds.push(classId);
    }
    
    this.setData({ selectedClassIds });
  },

  // 家长选择
  onParentSelect(e) {
    const parentId = parseInt(e.currentTarget.dataset.id);
    const { selectedParentIds } = this.data;
    const index = selectedParentIds.indexOf(parentId);
    
    if (index > -1) {
      selectedParentIds.splice(index, 1);
    } else {
      selectedParentIds.push(parentId);
    }
    
    this.setData({ selectedParentIds });
  },

  // 发布通知
  async publishNotice() {
    const { title, content, targetType, priority, selectedClassIds, selectedParentIds } = this.data;
    
    if (!title.trim()) {
      wx.showToast({ title: '请输入通知标题', icon: 'none' });
      return;
    }
    
    if (!content.trim()) {
      wx.showToast({ title: '请输入通知内容', icon: 'none' });
      return;
    }
    
    if (targetType === 'CLASS' && selectedClassIds.length === 0) {
      wx.showToast({ title: '请选择目标班级', icon: 'none' });
      return;
    }
    
    if (targetType === 'PARENT' && selectedParentIds.length === 0) {
      wx.showToast({ title: '请选择目标家长', icon: 'none' });
      return;
    }
    
    wx.showLoading({ title: '发布中...' });
    
    try {
      const data = {
        title: title.trim(),
        content: content.trim(),
        targetType: targetType,
        priority: priority
      };
      
      if (targetType === 'CLASS') {
        data.targetIds = selectedClassIds.join(',');
      } else if (targetType === 'PARENT') {
        data.targetIds = selectedParentIds.join(',');
      }
      
      const res = await app.request({
        url: '/notice',
        method: 'POST',
        data: data
      });
      
      wx.hideLoading();
      
      if (res.code === 200) {
        wx.showToast({ title: '发布成功', icon: 'success' });
        setTimeout(() => {
          wx.navigateBack();
        }, 1500);
      } else {
        wx.showToast({ title: res.message || '发布失败', icon: 'none' });
      }
    } catch (error) {
      wx.hideLoading();
      wx.showToast({ title: error.message || '发布失败', icon: 'none' });
    }
  },

  // 取消发布
  cancelPublish() {
    wx.navigateBack();
  }
});
