const app = getApp()

Page({
  data: {
    userInfo: {
      avatarUrl: '',
      nickName: '未登录',
      userId: ''
    },
    menuList: [
      { id: 'order', name: '我的订单', icon: '📋' },
      { id: 'record', name: '我的就诊记录', icon: '📜' },
      { id: 'report', name: '我的报告', icon: '📑' },
      { id: 'doctor', name: '我的医生', icon: '👨‍⚕️' }
    ]
  },

  onShow() {
    if (app.globalData.isLogin && app.globalData.userInfo) {
      this.setData({
        userInfo: {
          ...app.globalData.userInfo,
          userId: 'ID: 88886666'
        }
      })
    }
  },

  onMenuTap(e) {
    const id = e.currentTarget.dataset.id
    wx.showToast({
      title: '功能开发中',
      icon: 'none'
    })
  },

  handleLogout() {
    wx.showModal({
      title: '提示',
      content: '确定要退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          app.globalData.isLogin = false
          app.globalData.userInfo = null
          wx.reLaunch({ url: '/pages/index/index' })
        }
      }
    })
  },

  goToHome() {
    wx.reLaunch({ url: '/pages/index/index' })
  }
})