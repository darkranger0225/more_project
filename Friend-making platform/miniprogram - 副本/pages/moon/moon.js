// pages/moon/moon.js
const app = getApp()

Page({

  /**
   * 页面的初始数据
   */
  data: {
    time: '30:00',
    taskName: '',
    isRunning: false,
    remainTime: 30 * 60, // 30分钟
    timer: null,
    progress: 0,
    leftDeg: 0,
    rightDeg: 0,
    completed: false,
    remainTimeText: '30:00',
    timerType: 'work', // work 或 rest
    workTime: 30,
    restTime: 5,
    soundEnabled: true,
    vibrationEnabled: true,
    bgMusicEnabled: false,
    bgMusicRandom: false,
    bgMusicContext: null,
    currentBgMusic: 0
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.loadSettings()
    this.initTimer()
    this.initBgMusic()
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {
    // 每次页面显示时重新加载设置
    this.loadSettings()
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {
    this.stopTimer()
    this.stopBgMusic()
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  },

  navigateToFocusClock: function() {
    wx.navigateTo({
      url: '/packages/focus-clock/pages/index/index'
    })
  },

  navigateToSettings() {
    wx.navigateTo({
      url: '/pages/moon-settings/moon-settings'
    })
  },

  loadSettings() {
    const settings = wx.getStorageSync('moonSettings')
    if (settings) {
      const oldBgMusicEnabled = this.data.bgMusicEnabled
      this.setData({
        workTime: settings.workTime || 30,
        restTime: settings.restTime || 5,
        soundEnabled: settings.soundEnabled !== false,
        vibrationEnabled: settings.vibrationEnabled !== false,
        bgMusicEnabled: settings.bgMusicEnabled || false,
        bgMusicRandom: settings.bgMusicRandom || false
      })
      
      // 如果背景音乐状态发生变化，相应地停止或播放
      if (oldBgMusicEnabled !== settings.bgMusicEnabled) {
        if (settings.bgMusicEnabled) {
          this.playBgMusic()
        } else {
          this.stopBgMusic()
        }
      }
    }
  },

  initTimer() {
    const workTime = this.data.workTime
    const restTime = this.data.restTime
    this.setData({
      remainTime: this.data.timerType === 'work' ? workTime * 60 : restTime * 60,
      remainTimeText: this.data.timerType === 'work' ? 
        `${workTime.toString().padStart(2, '0')}:00` : 
        `${restTime.toString().padStart(2, '0')}:00`,
      progress: 0,
      leftDeg: 0,
      rightDeg: 0,
      completed: false
    })
  },

  onTaskNameInput(e) {
    this.setData({
      taskName: e.detail.value
    })
  },

  onStartWork() {
    if (!this.data.taskName) {
      wx.showToast({
        title: '请输入任务名称',
        icon: 'none'
      })
      return
    }
    this.setData({
      timerType: 'work',
      remainTime: this.data.workTime * 60,
      remainTimeText: `${this.data.workTime.toString().padStart(2, '0')}:00`
    })
    this.startTimer()
  },

  onStartRest() {
    this.setData({
      timerType: 'rest',
      remainTime: this.data.restTime * 60,
      remainTimeText: `${this.data.restTime.toString().padStart(2, '0')}:00`
    })
    this.startTimer()
  },

  startTimer() {
    if (this.data.isRunning) return
    this.setData({ isRunning: true })
    this.data.timer = setInterval(() => {
      if (this.data.remainTime <= 0) {
        this.completeTimer()
        return
      }
      this.updateTimer()
    }, 1000)
  },

  stopTimer() {
    if (this.data.timer) {
      clearInterval(this.data.timer)
      this.setData({
        isRunning: false,
        timer: null
      })
    }
  },

  resetTimer() {
    this.stopTimer()
    this.initTimer()
  },

  completeTimer() {
    this.stopTimer()
    this.setData({
      completed: true,
      progress: 270 // 完成时旋转到360度（-90 + 360 = 270）
    })

    // 播放提示音
    if (this.data.soundEnabled) {
      const innerAudioContext = wx.createInnerAudioContext()
      innerAudioContext.src = this.data.timerType === 'work' ? '/audio/complete2.mp3' : '/audio/complete.mp3'
      innerAudioContext.play()
    }

    // 震动提醒
    if (this.data.vibrationEnabled) {
      wx.vibrateLong({
        type: 'medium'
      })
    }

    wx.showToast({
      title: this.data.timerType === 'work' ? '工作完成！' : '休息结束！',
      icon: 'success'
    })
  },

  updateTimer() {
    const remainTime = this.data.remainTime - 1
    const totalTime = this.data.timerType === 'work' ? this.data.workTime * 60 : this.data.restTime * 60
    const progress = ((totalTime - remainTime) / totalTime) * 100
    
    // 计算进度条旋转角度（从-90度开始，顺时针旋转）
    const rotation = (progress * 3.6) - 90

    // 更新状态
    this.setData({
      remainTime,
      remainTimeText: this.formatTime(remainTime),
      progress: rotation
    })

    // 如果时间到了，完成计时
    if (remainTime <= 0) {
      this.completeTimer()
    }
  },

  formatTime(seconds) {
    const minutes = Math.floor(seconds / 60)
    const remainingSeconds = seconds % 60
    return `${minutes.toString().padStart(2, '0')}:${remainingSeconds.toString().padStart(2, '0')}`
  },

  initBgMusic() {
    if (this.data.bgMusicContext) {
      this.data.bgMusicContext.destroy()
    }
    this.data.bgMusicContext = wx.createInnerAudioContext()
    this.data.bgMusicContext.loop = true
  },

  playBgMusic() {
    if (!this.data.bgMusicEnabled) {
      this.stopBgMusic()
      return
    }
    
    const musicList = ['music01', 'music02', 'music03']
    let musicIndex = this.data.currentBgMusic
    
    if (this.data.bgMusicRandom) {
      musicIndex = Math.floor(Math.random() * musicList.length)
    }
    
    this.data.bgMusicContext.src = `/audio/${musicList[musicIndex]}.mp3`
    this.data.bgMusicContext.play()
    
    this.setData({
      currentBgMusic: (musicIndex + 1) % musicList.length
    })
  },

  stopBgMusic() {
    if (this.data.bgMusicContext) {
      this.data.bgMusicContext.stop()
      this.data.bgMusicContext.destroy()
      this.data.bgMusicContext = null
    }
  }
})