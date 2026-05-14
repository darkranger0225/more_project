const app = getApp()

Page({
  data: {
    workTime: 30,
    restTime: 5,
    soundEnabled: true,
    vibrationEnabled: true,
    bgMusicEnabled: false,
    bgMusicRandom: false
  },

  onLoad() {
    this.loadSettings()
  },

  loadSettings() {
    const settings = wx.getStorageSync('moonSettings')
    if (settings) {
      this.setData({
        workTime: settings.workTime || 30,
        restTime: settings.restTime || 5,
        soundEnabled: settings.soundEnabled !== false,
        vibrationEnabled: settings.vibrationEnabled !== false,
        bgMusicEnabled: settings.bgMusicEnabled || false,
        bgMusicRandom: settings.bgMusicRandom || false
      })
    }
  },

  onWorkTimeChange(e) {
    this.setData({
      workTime: e.detail.value
    })
  },

  onRestTimeChange(e) {
    this.setData({
      restTime: e.detail.value
    })
  },

  onSoundChange(e) {
    this.setData({
      soundEnabled: e.detail.value
    })
  },

  onVibrationChange(e) {
    this.setData({
      vibrationEnabled: e.detail.value
    })
  },

  onBgMusicChange(e) {
    this.setData({
      bgMusicEnabled: e.detail.value
    })
  },

  onBgMusicRandomChange(e) {
    this.setData({
      bgMusicRandom: e.detail.value
    })
  },

  saveSettings() {
    const settings = {
      workTime: this.data.workTime,
      restTime: this.data.restTime,
      soundEnabled: this.data.soundEnabled,
      vibrationEnabled: this.data.vibrationEnabled,
      bgMusicEnabled: this.data.bgMusicEnabled,
      bgMusicRandom: this.data.bgMusicRandom
    }

    wx.setStorageSync('moonSettings', settings)
    
    // 更新全局设置
    if (app.globalData) {
      app.globalData.moonSettings = settings
    }

    wx.showToast({
      title: '设置已保存',
      icon: 'success',
      duration: 1500
    })

    // 返回上一页
    setTimeout(() => {
      wx.navigateBack()
    }, 1500)
  }
}) 