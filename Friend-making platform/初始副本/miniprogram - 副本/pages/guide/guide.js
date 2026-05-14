Page({
  data: {
    // 页面的初始数据
  },

  onLoad: function(options) {
    // 页面加载时执行
  },

  onShareAppMessage: function() {
    return {
      title: '欢迎加入寻爱之旅',
      path: '/pages/guide/guide',
      imageUrl: '/image/login/guide.png'
    };
  }
}); 