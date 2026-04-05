Page({
  data: {
    selectedDept: '全部',
    deptFilters: [
      '全部', '内科', '脾胃科', '耳鼻喉科',
      '心理睡眠科', '皮肤科', '肿瘤科', '男科',
      '骨伤科'
    ],
    subDeptMap: {
      '内科': ['呼吸内科', '消化内科', '神经内科', '心内科', '肾内科'],
      '脾胃科': ['胃病中心', '肠病中心', '内镜中心'],
      '耳鼻喉科': ['耳科', '鼻科', '咽喉科'],
      '心理睡眠科': ['失眠门诊', '心理咨询', '青少年心理'],
      '皮肤科': ['皮炎湿疹', '痤疮美容', '中医皮肤'],
      '肿瘤科': ['肺癌', '胃癌', '中医调理'],
      '男科': ['前列腺', '生殖健康'],
      '骨伤科': ['颈椎病', '腰椎病', '关节病']
    },
    expandedDept: '', // 当前展开的科室
    selectedSubDept: '', // 选中的二级科室
    famousDoctors: [
      {
        id: 1,
        name: '张国医',
        title: '国医大师',
        dept: '中医内科 · 疑难病科',
        avatarText: '张',
        level: '名医',
        desc: '1、全国老中医药专家学术经验继承工作指导老师...',
        tags: ['学术权威', '擅治疑难', '口碑极佳'],
        fans: 5210,
        reputation: 5
      },
      {
        id: 2,
        name: '李教授',
        title: '省级名中医',
        dept: '中医妇科 · 调理科',
        avatarText: '李',
        level: '名医',
        desc: '1、从事中医临床、教学、科研工作40余年，擅长妇科...',
        tags: ['临床经验丰富', '医术精湛'],
        fans: 3280,
        reputation: 4
      },
      {
        id: 3,
        name: '王主任',
        title: '主任医师',
        dept: '中医骨科 · 针灸科',
        avatarText: '王',
        level: '名医',
        desc: '1、擅长针药并举治疗颈椎病、腰椎病、骨性关节炎...',
        tags: ['针灸权威', '医德高尚'],
        fans: 2150,
        reputation: 4
      }
    ]
  },

  onLoad() {
    // 可以在这里获取后端数据
  },

  onDeptSelect(e) {
    const dept = e.currentTarget.dataset.dept
    if (dept === '全部') {
      this.setData({ selectedDept: '全部', expandedDept: '', selectedSubDept: '' })
      wx.showToast({ title: '已显示全部名医', icon: 'none' })
      return
    }
    
    // 如果点击的是当前已展开的，则收起
    if (this.data.expandedDept === dept) {
      this.setData({ expandedDept: '' })
    } else {
      this.setData({ expandedDept: dept })
    }
    
    this.setData({ selectedDept: dept })
  },

  onSubDeptSelect(e) {
    const subDept = e.currentTarget.dataset.sub
    this.setData({ selectedSubDept: subDept })
    wx.showToast({ title: '正在筛选 ' + subDept, icon: 'none' })
    // 这里可以执行具体的筛选逻辑，收起面板
    this.setData({ expandedDept: '' })
  },

  onDoctorTap(e) {
    const id = e.currentTarget.dataset.id
    wx.navigateTo({
      url: `/pages/doctor-home/doctor-home?id=${id}`
    })
  },

  noop() {}
})