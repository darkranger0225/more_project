const request = require('../../utils/request.js');
const app = getApp();

Page({
  data: {
    userInfo: {},
    balance: '0.00',
    announcements: [],
    trendType: 'day',
    trendData: {}
  },

  onShow() {
    // 统一在 onShow 中检查登录状态和加载数据
    this.checkLoginAndLoadData();
  },

  checkLoginAndLoadData() {
    if (!app.globalData.token) {
      wx.redirectTo({
        url: '/pages/login/login'
      });
      return;
    }

    // 已登录，加载数据
    this.loadUserInfo();
    this.loadBalance();
    this.loadAnnouncements();
    this.loadTrendData();
  },

  loadUserInfo() {
    request.get('/api/user/info').then(res => {
      this.setData({
        userInfo: res.data
      });
      wx.setStorageSync('userInfo', res.data);
    }).catch(() => {
      // 加载失败不处理
    });
  },

  loadBalance() {
    request.get('/api/electricity/balance').then(res => {
      this.setData({
        balance: res.data ? parseFloat(res.data).toFixed(2) : '0.00'
      });
    }).catch(() => {
      // 加载失败不处理
    });
  },

  loadAnnouncements() {
    request.get('/api/announcement/list').then(res => {
      this.setData({
        announcements: res.data || []
      });
    }).catch(() => {
      // 加载失败不处理
    });
  },

  // 加载用电趋势数据
  loadTrendData() {
    const { trendType } = this.data;
    
    request.get('/api/electricity/trend', { type: trendType }).then(res => {
      if (res.data) {
        this.setData({
          trendData: res.data
        }, () => {
          // 数据加载完成后绘制图表
          this.drawChart();
        });
      }
    }).catch(err => {
      console.error('加载用电趋势失败:', err);
    });
  },

  // 切换趋势类型
  switchTrendType(e) {
    const type = e.currentTarget.dataset.type;
    this.setData({
      trendType: type
    }, () => {
      this.loadTrendData();
    });
  },

  // 绘制图表
  drawChart() {
    const { trendData, trendType } = this.data;
    
    if (!trendData.labels || trendData.labels.length === 0) {
      return;
    }

    const query = wx.createSelectorQuery();
    query.select('#trendChart')
      .fields({ node: true, size: true })
      .exec((res) => {
        if (!res[0]) return;
        
        const canvas = res[0].node;
        const ctx = canvas.getContext('2d');
        const width = res[0].width;
        const height = res[0].height;
        
        // 设置canvas尺寸
        canvas.width = width;
        canvas.height = height;
        
        // 清空画布
        ctx.clearRect(0, 0, width, height);
        
        // 绘制图表
        this.renderChart(ctx, width, height, trendData, trendType);
      });
  },

  // 渲染图表
  renderChart(ctx, width, height, data, type) {
    const labels = data.labels;
    const usageData = data.usageData;
    const padding = { top: 40, right: 20, bottom: 60, left: 60 };
    const chartWidth = width - padding.left - padding.right;
    const chartHeight = height - padding.top - padding.bottom;
    
    // 计算数据范围
    const maxValue = Math.max(...usageData) * 1.1;
    const minValue = 0;
    
    // 绘制坐标轴
    ctx.strokeStyle = '#e8e8e8';
    ctx.lineWidth = 1;
    
    // Y轴
    ctx.beginPath();
    ctx.moveTo(padding.left, padding.top);
    ctx.lineTo(padding.left, height - padding.bottom);
    ctx.stroke();
    
    // X轴
    ctx.beginPath();
    ctx.moveTo(padding.left, height - padding.bottom);
    ctx.lineTo(width - padding.right, height - padding.bottom);
    ctx.stroke();
    
    // 绘制Y轴刻度和标签
    ctx.fillStyle = '#999';
    ctx.font = '20rpx sans-serif';
    ctx.textAlign = 'right';
    
    const ySteps = 5;
    for (let i = 0; i <= ySteps; i++) {
      const y = padding.top + (chartHeight / ySteps) * i;
      const value = maxValue - (maxValue / ySteps) * i;
      
      // 刻度线
      ctx.strokeStyle = '#e8e8e8';
      ctx.beginPath();
      ctx.moveTo(padding.left, y);
      ctx.lineTo(width - padding.right, y);
      ctx.stroke();
      
      // 标签
      ctx.fillText(value.toFixed(1), padding.left - 10, y + 5);
    }
    
    // 绘制X轴标签
    ctx.textAlign = 'center';
    ctx.fillStyle = '#999';
    const xStep = chartWidth / (labels.length - 1);
    
    labels.forEach((label, index) => {
      // 显示所有标签（因为现在数据点很少）
      const x = padding.left + xStep * index;
      ctx.fillText(label, x, height - padding.bottom + 30);
    });
    
    // 绘制折线
    ctx.strokeStyle = '#1890ff';
    ctx.lineWidth = 3;
    ctx.beginPath();
    
    usageData.forEach((value, index) => {
      const x = padding.left + xStep * index;
      const y = padding.top + chartHeight - (value / maxValue) * chartHeight;
      
      if (index === 0) {
        ctx.moveTo(x, y);
      } else {
        ctx.lineTo(x, y);
      }
    });
    
    ctx.stroke();
    
    // 绘制数据点
    ctx.fillStyle = '#1890ff';
    usageData.forEach((value, index) => {
      const x = padding.left + xStep * index;
      const y = padding.top + chartHeight - (value / maxValue) * chartHeight;
      
      ctx.beginPath();
      ctx.arc(x, y, 4, 0, Math.PI * 2);
      ctx.fill();
    });
    
    // 绘制渐变填充
    const gradient = ctx.createLinearGradient(0, padding.top, 0, height - padding.bottom);
    gradient.addColorStop(0, 'rgba(24, 144, 255, 0.3)');
    gradient.addColorStop(1, 'rgba(24, 144, 255, 0.05)');
    
    ctx.fillStyle = gradient;
    ctx.beginPath();
    ctx.moveTo(padding.left, height - padding.bottom);
    
    usageData.forEach((value, index) => {
      const x = padding.left + xStep * index;
      const y = padding.top + chartHeight - (value / maxValue) * chartHeight;
      ctx.lineTo(x, y);
    });
    
    ctx.lineTo(width - padding.right, height - padding.bottom);
    ctx.closePath();
    ctx.fill();
  },

  // 图表触摸事件
  onChartTouch(e) {
    // 可以在这里添加显示具体数值的交互
    console.log('图表触摸:', e);
  },

  goToAnnouncement(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: '/pages/announcement/detail/detail?id=' + id
    });
  }
});
