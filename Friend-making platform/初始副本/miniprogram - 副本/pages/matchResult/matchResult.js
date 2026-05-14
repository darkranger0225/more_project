Page({
  data: {
    matches: [], // 双向匹配结果
    attentions: [], // 单向关注关系
  },

  onLoad(options) {
    // 获取传递的匹配结果和关注关系
    let matches = [];
    let attentions = [];
    
    try {
      if (options.matches) {
        matches = JSON.parse(decodeURIComponent(options.matches));
      }
      
      if (options.attentions) {
        attentions = JSON.parse(decodeURIComponent(options.attentions));
      }
    } catch (error) {
      console.error('解析匹配数据出错:', error);
    }

    this.setData({
      matches,
      attentions,
    });
  },
});