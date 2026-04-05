const app = getApp()

Page({
  data: {
    entries: [
      { id: 1, name: '在线问诊', desc: '图文咨询', icon: '问' },
      { id: 2, name: '预约挂号', desc: '名医排班', icon: '约' },
      { id: 3, name: '名医馆', desc: '名医汇聚', icon: '医' },
      { id: 4, name: '养生讲堂', desc: '视频文章', icon: '学' }
    ],
    articles: [
      { id: 1, title: '春季养肝：饮食与作息要点', tag: '节气养生', date: '03-28' },
      { id: 2, title: '失眠与心神：中医如何辨证', tag: '常见病', date: '03-25' },
      { id: 3, title: '艾灸入门：注意事项一览', tag: '外治法', date: '03-20' }
    ]
  },

  onEntryTap(e) {
    const id = Number(e.currentTarget.dataset.id)
    if (id === 1) {
      wx.navigateTo({ url: '/pages/dept/dept?visitMode=online' })
    } else if (id === 2) {
      wx.navigateTo({ url: '/pages/dept/dept?visitMode=offline' })
    } else if (id === 3) {
      wx.navigateTo({ url: '/pages/famous-doctor/famous-doctor' })
    }
  },

  goToUser() {
    app.checkLogin('/pages/user/user')
  }
})
