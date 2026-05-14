const request = require('../../../utils/request.js');

Page({
  data: {
    electricityPrice: '0.60',
    buildingList: [],
    selectedBuilding: null,
    dormitoryKeyword: '',
    dormitoryList: []
  },

  onLoad() {
    this.loadBuildingList();
    this.loadElectricityPrice();
  },

  onShow() {
    // 每次显示页面时刷新数据
    if (this.data.selectedBuilding) {
      this.searchDormitories();
    }
  },

  // 加载楼号列表
  loadBuildingList() {
    request.get('/api/building/list').then(res => {
      if (res.data) {
        this.setData({
          buildingList: res.data
        });
      }
    }).catch(err => {
      console.error('加载楼号列表失败:', err);
      wx.showToast({
        title: '加载楼号列表失败',
        icon: 'none'
      });
    });
  },

  // 加载当前电价
  loadElectricityPrice() {
    request.get('/api/admin/config/list').then(res => {
      if (res.data) {
        const priceConfig = res.data.find(item => item.configKey === 'electricity_price');
        if (priceConfig) {
          this.setData({
            electricityPrice: priceConfig.configValue
          });
        }
      }
    }).catch(err => {
      console.error('加载电价失败:', err);
    });
  },

  // 电价输入
  onPriceInput(e) {
    this.setData({
      electricityPrice: e.detail.value
    });
  },

  // 修改电价
  updatePrice() {
    const price = parseFloat(this.data.electricityPrice);
    if (isNaN(price) || price <= 0) {
      wx.showToast({
        title: '请输入有效的电价',
        icon: 'none'
      });
      return;
    }

    // 保留两位小数
    const formattedPrice = price.toFixed(2);

    wx.showModal({
      title: '确认修改',
      content: `确定将电价修改为 ${formattedPrice} 元/度吗？`,
      success: (res) => {
        if (res.confirm) {
          request.put('/api/admin/config/update', {
            configKey: 'electricity_price',
            configValue: formattedPrice,
            description: '每度电单价'
          }).then(() => {
            wx.showToast({
              title: '修改成功',
              icon: 'success'
            });
            this.setData({
              electricityPrice: formattedPrice
            });
          }).catch(err => {
            console.error('修改电价失败:', err);
            wx.showToast({
              title: '修改失败',
              icon: 'none'
            });
          });
        }
      }
    });
  },

  // 选择楼号
  onBuildingChange(e) {
    const index = e.detail.value;
    this.setData({
      selectedBuilding: this.data.buildingList[index]
    });
  },

  // 寝室号输入
  onDormitoryInput(e) {
    this.setData({
      dormitoryKeyword: e.detail.value
    });
  },

  // 查询宿舍用电信息
  searchDormitories() {
    if (!this.data.selectedBuilding) {
      wx.showToast({
        title: '请选择楼号',
        icon: 'none'
      });
      return;
    }

    wx.showLoading({
      title: '加载中...'
    });

    const params = {
      buildingId: this.data.selectedBuilding.id
    };

    if (this.data.dormitoryKeyword) {
      params.dormitoryNumber = this.data.dormitoryKeyword;
    }

    request.get('/api/admin/electricity/usage', params).then(res => {
      wx.hideLoading();
      if (res.data) {
        // 处理数据，保留两位小数
        const processedData = res.data.map(item => {
          // 处理缴费记录中的金额
          const paymentRecords = item.paymentRecords ? item.paymentRecords.map(payment => ({
            ...payment,
            amount: parseFloat(payment.amount || 0).toFixed(2)
          })) : [];
          
          return {
            ...item,
            balance: parseFloat(item.balance || 0).toFixed(2),
            totalUsage: parseFloat(item.totalUsage || 0).toFixed(2),
            totalCost: parseFloat(item.totalCost || 0).toFixed(2),
            unpaidAmount: parseFloat(item.unpaidAmount || 0).toFixed(2),
            paymentRecords: paymentRecords
          };
        });
        this.setData({
          dormitoryList: processedData
        });
      }
    }).catch(err => {
      wx.hideLoading();
      console.error('查询用电信息失败:', err);
      wx.showToast({
        title: '查询失败',
        icon: 'none'
      });
    });
  }
});
