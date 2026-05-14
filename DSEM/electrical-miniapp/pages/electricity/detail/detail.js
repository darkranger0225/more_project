Page({
  data: {
    usageDetail: {}
  },

  onLoad(options) {
    if (options.id) {
      this.loadUsageDetail(options.id);
    }
  },

  loadUsageDetail(id) {
    // 这里可以根据需要调用接口获取详情
    // 目前使用列表页传递的数据或重新请求
    const eventChannel = this.getOpenerEventChannel();
    if (eventChannel) {
      eventChannel.on('acceptDataFromOpenerPage', (data) => {
        this.setData({
          usageDetail: data
        });
      });
    }
  }
});
