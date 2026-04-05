Page({
  data: {
    selectedDate: 0,
    doctor: {
      name: '徐再春',
      title: '主任医师',
      dept: '内科',
      avatarText: '徐',
      fans: 244,
      reputation: 69,
      specialty: '长期从事中医内科临床工作，擅长诊治各种疑难、复杂病症：如难治性肾病、肾功能减退；致残性红斑狼疮、类风湿关节炎等风湿病；顽固性咳嗽、气管炎；活动性肝炎、肝硬化；致死性糖尿病、冠心病...',
      location: '杭州萧山分院',
      address: '萧山区市心南路128号二轻大厦（萧山宾馆对面）'
    },
    dateList: [
      { day: '04-05', week: '周日', remain: 30 },
      { day: '04-12', week: '周日', remain: 26 },
      { day: '04-19', week: '周日', remain: 30 },
      { day: '04-26', week: '周日', remain: 28 }
    ],
    timeSlots: [
      { time: '08:30-09:00', remain: 6, price: 100 },
      { time: '09:00-09:30', remain: 8, price: 100 }
    ],
    reviews: [
      {
        id: 1,
        userName: '张**',
        userInitials: '张',
        rating: 5,
        date: '2024-03-25',
        content: '徐医生态度非常好，讲解非常细致，开的药方吃了一周效果很明显，非常感谢！',
        tags: ['医术高超', '态度和蔼']
      },
      {
        id: 2,
        userName: '王**',
        userInitials: '王',
        rating: 5,
        date: '2024-03-20',
        content: '名不虚传，老中医看病就是稳，对症下药，解决了困扰我很久的咳嗽问题。',
        tags: ['对症下药', '老牌专家']
      }
    ]
  },

  onLoad(options) {
    // 实际开发中根据 options.id 获取医生详情
  },

  onDateTap(e) {
    this.setData({ selectedDate: e.currentTarget.dataset.index })
  },

  onAppoint() {
    wx.showToast({ title: '挂号功能开发中', icon: 'none' })
  },

  noop() {}
})