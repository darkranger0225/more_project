// 全局配置文件
const config = {
  // 服务器基础地址
  baseUrl: 'http://localhost:8080',
  
  // API接口地址
  api: {
    // 用户相关接口
    register: '/api/user/register',
    login: '/api/user/login',
    getUserInfo: '/api/user/info',
    
    // 偏好设置相关接口
    savePreferences: '/api/user/preference/save'
  },
  
  // 获取完整API地址
  getApiUrl: function(apiName) {
    return this.baseUrl + (this.api[apiName] || '');
  }
};

module.exports = config;