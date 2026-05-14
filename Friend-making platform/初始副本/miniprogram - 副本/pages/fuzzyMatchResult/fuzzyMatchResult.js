// fuzzyMatchResult.js
Page({
  data: {
    matches: [], // 匹配结果数组
  },

  onLoad(options) {
    try {
      // 从 URL 参数中解析匹配结果
      let matches = [];
      
      if (options.matches) {
        matches = JSON.parse(decodeURIComponent(options.matches));
        
        // 格式化每个匹配项的 score
        const formattedMatches = matches.map(item => ({
          ...item,
          formattedScore: (item.score * 100).toFixed(2) // 保留两位小数
        }));
        
        this.setData({ matches: formattedMatches });
      }
    } catch (error) {
      console.error('解析匹配数据出错:', error);
      this.setData({ matches: [] });
    }
  },
});