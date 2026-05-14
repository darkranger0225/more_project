const request = require('../../../utils/request.js');

Page({
  data: {
    amount: '',
    balance: '0.00',
    showQRCode: false,
    rechargeNo: '',
    countdown: 3
  },

  onShow() {
    this.loadAccountInfo();
  },

  onUnload() {
    // 页面卸载时清除定时器
    if (this.countdownTimer) {
      clearInterval(this.countdownTimer);
    }
    if (this.confirmTimer) {
      clearTimeout(this.confirmTimer);
    }
  },

  loadAccountInfo() {
    request.get('/api/account/info').then(res => {
      if (res.data) {
        this.setData({
          balance: parseFloat(res.data.balance).toFixed(2)
        });
      }
    }).catch(() => {
      // 加载失败不处理
    });
  },

  onAmountInput(e) {
    this.setData({
      amount: e.detail.value
    });
  },

  selectAmount(e) {
    const amount = e.currentTarget.dataset.amount;
    this.setData({
      amount: amount
    });
  },

  createRechargeOrder() {
    const { amount } = this.data;

    if (!amount || parseFloat(amount) <= 0) {
      wx.showToast({
        title: '请输入充值金额',
        icon: 'none'
      });
      return;
    }

    // 将参数放在 data 中作为 JSON 请求体
    request.post('/api/account/recharge/create', {
      amount: parseFloat(amount)
    }).then(res => {
      this.setData({
        showQRCode: true,
        rechargeNo: res.data,
        countdown: 3
      });

      // 开始倒计时
      this.startCountdown();

      // 3秒后自动确认充值
      this.confirmTimer = setTimeout(() => {
        this.confirmRecharge();
      }, 3000);
    }).catch(() => {
      wx.showToast({
        title: '创建订单失败',
        icon: 'none'
      });
    });
  },

  startCountdown() {
    this.countdownTimer = setInterval(() => {
      let countdown = this.data.countdown - 1;
      if (countdown <= 0) {
        clearInterval(this.countdownTimer);
        countdown = 0;
      }
      this.setData({ countdown });
    }, 1000);
  },

  confirmRecharge() {
    const { rechargeNo } = this.data;

    // 清除定时器
    if (this.countdownTimer) {
      clearInterval(this.countdownTimer);
    }
    if (this.confirmTimer) {
      clearTimeout(this.confirmTimer);
    }

    // 将参数放在 data 中作为 JSON 请求体
    request.post('/api/account/recharge/confirm', {
      rechargeNo: rechargeNo
    }).then(() => {
      this.setData({
        showQRCode: false,
        amount: ''
      });

      wx.showToast({
        title: '充值成功',
        icon: 'success'
      });

      // 刷新账户信息
      this.loadAccountInfo();
    }).catch(() => {
      this.setData({
        showQRCode: false
      });
      wx.showToast({
        title: '充值失败，请重试',
        icon: 'none'
      });
    });
  },

  closeQRCode() {
    // 用户主动关闭，清除定时器
    if (this.countdownTimer) {
      clearInterval(this.countdownTimer);
    }
    if (this.confirmTimer) {
      clearTimeout(this.confirmTimer);
    }

    this.setData({
      showQRCode: false
    });

    wx.showModal({
      title: '提示',
      content: '您取消了充值，是否继续？',
      success: (res) => {
        if (res.confirm) {
          // 用户点击确定，重新确认充值
          this.confirmRecharge();
        }
      }
    });
  }
});
