App({
  onLaunch() {},
  globalData: {
    isLogin: false, // 登录状态
    userInfo: null,
    apiBaseUrl: 'http://localhost:8080/com/back/api'
  },
  
  // 校验登录并跳转
  checkLogin(targetUrl) {
    if (this.globalData.isLogin) {
      wx.navigateTo({ url: targetUrl })
    } else {
      wx.navigateTo({ url: '/pages/login/login?redirect=' + encodeURIComponent(targetUrl) })
    }
  }
})
