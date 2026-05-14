const request = require('../../../utils/request.js');

Page({
  data: {
    repairList: [],
    allRepairList: [],
    currentTab: 0,
    pendingCount: 0,
    processingCount: 0,
    completedCount: 0,
    showModal: false,
    modalTitle: '',
    replyContent: '',
    currentRepairId: null,
    currentStatus: null
  },

  onLoad() {
    this.loadRepairList();
  },

  onShow() {
    this.loadRepairList();
  },

  // 加载报修列表
  loadRepairList() {
    wx.showLoading({
      title: '加载中...'
    });

    request.get('/api/admin/repair/list').then(res => {
      wx.hideLoading();
      if (res.data) {
        // 处理数据
        const processedData = res.data.map(item => ({
          ...item,
          createTime: item.createTime ? item.createTime.substring(0, 16) : '',
          handleTime: item.handleTime ? item.handleTime.substring(0, 16) : ''
        }));

        // 计算统计数据
        const pendingCount = processedData.filter(item => item.status === 0).length;
        const processingCount = processedData.filter(item => item.status === 1).length;
        const completedCount = processedData.filter(item => item.status === 2).length;

        this.setData({
          allRepairList: processedData,
          pendingCount,
          processingCount,
          completedCount
        });

        // 根据当前标签筛选
        this.filterRepairList();
      }
    }).catch(err => {
      wx.hideLoading();
      console.error('加载报修列表失败:', err);
      wx.showToast({
        title: '加载失败',
        icon: 'none'
      });
    });
  },

  // 筛选报修列表
  filterRepairList() {
    const { allRepairList, currentTab } = this.data;
    let filteredList = [];

    switch (currentTab) {
      case 0: // 全部
        filteredList = allRepairList;
        break;
      case 1: // 待处理
        filteredList = allRepairList.filter(item => item.status === 0);
        break;
      case 2: // 处理中
        filteredList = allRepairList.filter(item => item.status === 1);
        break;
      case 3: // 已完成
        filteredList = allRepairList.filter(item => item.status === 2);
        break;
    }

    this.setData({
      repairList: filteredList
    });
  },

  // 切换标签
  switchTab(e) {
    const index = parseInt(e.currentTarget.dataset.index);
    this.setData({
      currentTab: index
    }, () => {
      this.filterRepairList();
    });
  },

  // 处理报修
  handleRepair(e) {
    const { id, status } = e.currentTarget.dataset;
    const newStatus = status === 0 ? 1 : 2;
    const title = status === 0 ? '开始处理' : '完成处理';

    this.setData({
      showModal: true,
      modalTitle: title,
      currentRepairId: id,
      currentStatus: newStatus,
      replyContent: ''
    });
  },

  // 输入回复
  onReplyInput(e) {
    this.setData({
      replyContent: e.detail.value
    });
  },

  // 关闭弹窗
  closeModal() {
    this.setData({
      showModal: false,
      currentRepairId: null,
      currentStatus: null,
      replyContent: ''
    });
  },

  // 确认处理
  confirmHandle() {
    const { currentRepairId, currentStatus, replyContent } = this.data;

    if (!currentRepairId || currentStatus === null) {
      return;
    }

    wx.showLoading({
      title: '处理中...'
    });

    request.post('/api/admin/repair/handle', {
      repairId: currentRepairId,
      status: currentStatus,
      reply: replyContent
    }).then(() => {
      wx.hideLoading();
      wx.showToast({
        title: '处理成功',
        icon: 'success'
      });
      this.closeModal();
      this.loadRepairList();
    }).catch(err => {
      wx.hideLoading();
      console.error('处理报修失败:', err);
      wx.showToast({
        title: '处理失败',
        icon: 'none'
      });
    });
  },

  // 预览图片
  previewImage(e) {
    const url = e.currentTarget.dataset.url;
    wx.previewImage({
      urls: [url]
    });
  }
});
