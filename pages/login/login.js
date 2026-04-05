const app = getApp()

Page({
  data: {
    loginType: 'wechat', // wechat | phone
    phone: '',
    code: '',
    counting: false,
    count: 60,
    redirectUrl: '/pages/index/index'
  },

  onLoad(options) {
    if (options.redirect) {
      this.setData({ redirectUrl: decodeURIComponent(options.redirect) })
    }
  },

  switchTab(e) {
    this.setData({ loginType: e.currentTarget.dataset.type })
  },

  onInputPhone(e) {
    this.setData({ phone: e.detail.value })
  },

  onInputCode(e) {
    this.setData({ code: e.detail.value })
  },

  sendCode() {
    if (this.data.counting || !this.data.phone) return
    if (!/^1\d{10}$/.test(this.data.phone)) {
      wx.showToast({ title: '手机号格式错误', icon: 'none' })
      return
    }
    
    this.setData({ counting: true })
    wx.showToast({ title: '验证码已发送', icon: 'none' })
    
    let timer = setInterval(() => {
      if (this.data.count <= 1) {
        clearInterval(timer)
        this.setData({ counting: false, count: 60 })
      } else {
        this.setData({ count: this.data.count - 1 })
      }
    }, 1000)
  },

  // 模拟微信登录
  handleWechatLogin() {
    wx.showLoading({ title: '登录中...' })
    setTimeout(() => {
      app.globalData.isLogin = true
      app.globalData.userInfo = { nickName: '微信用户', avatarUrl: '' }
      wx.hideLoading()
      wx.showToast({ title: '登录成功' })
      setTimeout(() => {
        wx.reLaunch({ url: this.data.redirectUrl })
      }, 1000)
    }, 1000)
  },

  // 模拟手机号登录
  handlePhoneLogin() {
    if (!this.data.phone || !this.data.code) {
      wx.showToast({ title: '请完善登录信息', icon: 'none' })
      return
    }
    wx.showLoading({ title: '登录中...' })
    setTimeout(() => {
      app.globalData.isLogin = true
      app.globalData.userInfo = { nickName: '手机用户', avatarUrl: '' }
      wx.hideLoading()
      wx.showToast({ title: '登录成功' })
      setTimeout(() => {
        wx.reLaunch({ url: this.data.redirectUrl })
      }, 1000)
    }, 1000)
  },

  goBack() {
    wx.navigateBack()
  }
})