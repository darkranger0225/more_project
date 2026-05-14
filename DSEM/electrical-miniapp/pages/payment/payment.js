const request = require('../../utils/request.js');

Page({
  data: {
    totalAmount: '0.00',
    unpaidList: [],
    historyList: [],
    accountBalance: '0.00'
  },

  onShow() {
    this.loadData();
  },

  onLoad() {
    this.loadData();
  },

  // 下拉刷新
  onPullDownRefresh() {
    this.loadData().then(() => {
      wx.stopPullDownRefresh();
    });
  },

  loadData() {
    return Promise.all([
      this.loadAccountInfo(),
      this.loadUnpaidList(),
      this.loadHistoryList()
    ]);
  },

  // 加载账户信息
  loadAccountInfo() {
    return request.get('/api/account/info').then(res => {
      if (res.data) {
        this.setData({
          accountBalance: parseFloat(res.data.balance).toFixed(2)
        });
      }
    }).catch(() => {
      // 加载失败不处理
    });
  },

  loadUnpaidList() {
    return request.get('/api/electricity/unpaid').then(res => {
      const list = res.data || [];
      let total = 0;
      list.forEach(item => {
        // 如果balance为负数，表示欠费，需要加到总额中
        const balance = parseFloat(item.balance || 0);
        if (balance < 0) {
          total += Math.abs(balance);
        } else {
          total += parseFloat(item.amount || 0);
        }
      });
      this.setData({
        unpaidList: list,
        totalAmount: total.toFixed(2)
      });
    }).catch(() => {
      // 加载失败不处理
    });
  },

  loadHistoryList() {
    return request.get('/api/payment/bill/list').then(res => {
      this.setData({
        historyList: res.data || []
      });
    }).catch(() => {
      // 加载失败不处理
    });
  },

  // 获取账单类型描述
  getBillType(item) {
    if (item.usageAmount === 0) {
      return '登录扣费';
    }
    return '电费';
  },

  // 获取账单状态
  getBillStatus(item) {
    const balance = parseFloat(item.balance || 0);
    if (balance < 0) {
      return '欠费';
    }
    return '待缴费';
  },

  payBill(e) {
    const usageId = e.currentTarget.dataset.id;
    wx.showModal({
      title: '确认缴费',
      content: '确定要缴纳该笔费用吗？',
      success: (res) => {
        if (res.confirm) {
          // 使用URL参数传递usageId
          request.post(`/api/payment/pay?usageId=${usageId}`).then(() => {
            wx.showToast({
              title: '缴费成功',
              icon: 'success'
            });
            this.loadData();
          }).catch(err => {
            wx.showModal({
              title: '缴费失败',
              content: err.message || '请检查账户余额是否充足',
              showCancel: false
            });
          });
        }
      }
    });
  },

  payAll() {
    wx.showModal({
      title: '确认缴费',
      content: `确定要缴纳全部费用 ¥${this.data.totalAmount} 吗？`,
      success: (res) => {
        if (res.confirm) {
          const promises = this.data.unpaidList.map(item => {
            // 使用URL参数传递usageId
            return request.post(`/api/payment/pay?usageId=${item.id}`);
          });
          
          Promise.all(promises).then(() => {
            wx.showToast({
              title: '缴费成功',
              icon: 'success'
            });
            this.loadData();
          }).catch(err => {
            wx.showModal({
              title: '缴费失败',
              content: err.message || '部分账单缴费失败，请检查账户余额',
              showCancel: false
            });
          });
        }
      }
    });
  }
});
