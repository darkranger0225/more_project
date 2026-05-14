// utils/auth.js

function checkLogin() {
  const token = wx.getStorageSync('token');
  if (!token) {
    // 如果没有 token，返回 false
    return false;
  }
  return true;
}

// 获取用户是否是管理员
function getIsAdmin() {
  return wx.getStorageSync('isAdmin'); // 返回布尔值
}

// 获取 userId
function getUserId() {
  return wx.getStorageSync('userId'); // 从本地存储中读取 userId
}

module.exports = {
  checkLogin,
  getIsAdmin,
  getUserId // 导出新方法
};