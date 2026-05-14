// fuzzyMatchResult.js
Page({
  data: {
    matches: [], // 匹配结果数组
    loading: false
  },

  onLoad(options) {
    // 显示加载状态
    this.setData({ loading: true });
    
    try {
      // 从 URL 参数中解析匹配结果
      let matches = [];
      
      if (options && options.matches) {
        try {
          // 安全地解码和解析数据
          const decodedData = decodeURIComponent(options.matches);
          console.log('接收到的匹配数据:', decodedData);
          
          const parsedData = JSON.parse(decodedData);
          
          // 验证数据格式
          if (Array.isArray(parsedData)) {
            // 安全地格式化每个匹配项，处理可能缺失的字段
            const formattedMatches = parsedData.map(item => {
              // 确保item是对象
              if (!item || typeof item !== 'object') {
                return {
                  id: '',
                  name: '未知用户',
                  avatar: '/image/login/defaultava.png',
                  gender: '其他',
                  interests: '未提供',
                  formattedScore: '0.00',
                  score: 0
                };
              }
              
              return {
                ...item,
                // 确保所有必要字段都有默认值
                id: item.id || '',
                name: item.name || '未知用户',
                avatar: item.avatar || '/image/login/defaultava.png',
                gender: item.gender || '其他',
                interests: item.interests || '未提供',
                // 安全地格式化分数
                formattedScore: item.score !== undefined && item.score !== null 
                  ? (parseFloat(item.score) * 100).toFixed(2) 
                  : '0.00',
                score: item.score || 0
              };
            });
            
            this.setData({ matches: formattedMatches });
          } else {
            console.error('匹配结果不是数组:', parsedData);
            wx.showToast({
              title: '数据格式错误',
              icon: 'error'
            });
          }
        } catch (parseError) {
          console.error('JSON解析错误:', parseError);
          wx.showToast({
            title: '数据解析失败',
            icon: 'error'
          });
        }
      } else {
        console.warn('未收到匹配结果数据');
        wx.showToast({
          title: '暂无匹配结果',
          icon: 'none'
        });
      }
    } catch (error) {
      console.error('处理匹配数据时发生错误:', error);
      wx.showToast({
        title: '加载失败，请重试',
        icon: 'error'
      });
    } finally {
      // 无论成功失败都关闭加载状态
      this.setData({ loading: false });
    }
  },
  
  // 返回上一页
  onBackPress() {
    wx.navigateBack();
    return true;
  }
});