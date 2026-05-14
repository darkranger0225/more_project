const app = getApp();

Page({
  data: {
    roleArray: ['全部角色', '家长', '教师', '管理员'],
    roleIndex: 0,
    roleFilter: '',
    userList: []
  },

  onShow() {
    this.loadUserList();
  },

  async loadUserList() {
    try {
      const roleFilter = this.data.roleFilter;
      const res = await app.request({
        url: '/user/list',
        data: {
          role: roleFilter
        }
      });

      if (res.code === 200) {
        const users = res.data.map(user => {
          let roleText = '';
          switch(user.role) {
            case 'PARENT':
              roleText = '家长';
              break;
            case 'TEACHER':
              roleText = '教师';
              break;
            case 'ADMIN':
              roleText = '管理员';
              break;
          }
          return {
            ...user,
            roleText
          };
        });

        this.setData({
          userList: users
        });
      }
    } catch (error) {
      wx.showToast({
        title: '加载失败',
        icon: 'none'
      });
    }
  },

  onRoleChange(e) {
    const index = e.detail.value;
    const roleMap = ['', 'PARENT', 'TEACHER', 'ADMIN'];
    this.setData({
      roleIndex: index,
      roleFilter: roleMap[index]
    }, () => {
      this.loadUserList();
    });
  },

  addUser() {
    wx.navigateTo({
      url: '/pages/admin/user-form/user-form'
    });
  },

  editUser(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/admin/user-form/user-form?id=${id}&mode=edit`
    });
  },

  async toggleStatus(e) {
    const id = e.currentTarget.dataset.id;
    const currentStatus = e.currentTarget.dataset.status;
    const newStatus = currentStatus === 1 ? 0 : 1;

    wx.showLoading({ title: '处理中...' });

    try {
      // 先获取用户详情
      const userRes = await app.request({
        url: `/user/${id}`
      });

      if (userRes.code !== 200) {
        throw new Error('获取用户信息失败');
      }

      const user = userRes.data;
      
      // 更新状态
      const res = await app.request({
        url: '/user',
        method: 'PUT',
        data: {
          id: id,
          username: user.username,
          realName: user.realName,
          phone: user.phone,
          role: user.role,
          status: newStatus
        }
      });

      wx.hideLoading();

      if (res.code === 200) {
        wx.showToast({
          title: newStatus === 1 ? '已启用' : '已禁用',
          icon: 'success'
        });
        this.loadUserList();
      }
    } catch (error) {
      wx.hideLoading();
      wx.showToast({
        title: error.message || '操作失败',
        icon: 'none'
      });
    }
  },

  deleteUser(e) {
    const id = e.currentTarget.dataset.id;
    
    wx.showModal({
      title: '提示',
      content: '确定要删除该用户吗？此操作不可恢复！',
      confirmColor: '#f44336',
      success: async (res) => {
        if (res.confirm) {
          try {
            const result = await app.request({
              url: `/user/${id}`,
              method: 'DELETE'
            });

            if (result.code === 200) {
              wx.showToast({
                title: '删除成功',
                icon: 'success'
              });
              this.loadUserList();
            }
          } catch (error) {
            wx.showToast({
              title: error.message || '删除失败',
              icon: 'none'
            });
          }
        }
      }
    });
  }
});