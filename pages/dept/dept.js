const app = getApp()

Page({
  data: {
    city: '杭州',
    keyword: '',
    mainTab: 'dept', // dept | disease
    leftIndex: 0,
    rightList: [],
    deptLeft: ['不限科室'],
    diseaseDeptLeft: [],
    diseaseDeptIds: [],
    baseUrl: '',
    deptItems: [], // 后端返回的科室列表
    deptParents: [], // 顶级科室
    deptChildrenMap: {}, // parentId -> children array
    currentDeptId: '', // 当前选中的顶级科室ID（疾病模式下用于刷新右侧二级科室）
    currentSubDeptId: '', // 疾病模式下选中的二级科室ID
    diseaseList: [], // 当前二级科室下的疾病列表
    /** 首页带入：online=在线问诊 offline=预约挂号，跳转医生列表时透传 */
    visitMode: ''
  },

  getApiBase() {
    if (this._apiBase) return this._apiBase
    const g = getApp().globalData && getApp().globalData.apiBaseUrl
    return g || 'http://localhost:8080/com/back/api'
  },

  onLoad(options) {
    const baseUrl = app.globalData.apiBaseUrl || 'http://localhost:8080/com/back/api'
    this._apiBase = baseUrl
    const visitMode = (options && options.visitMode) || ''
    this.setData({ baseUrl, visitMode })
    this.loadDepartments()
  },

  // 加载科室数据
  loadDepartments() {
    wx.request({
      url: `${this.getApiBase()}/department/list`,
      method: 'GET',
      success: (res) => {
        const list = Array.isArray(res.data) ? res.data : []
        if (!list.length) return
        // 规范字段名
        const items = list.map(it => ({
          id: it.departmentId || it.department_id || it.id || '',
          name: it.departmentName || it.department_name || it.name || '',
          parentId: it.departmentParentId || it.department_parent_id || it.parentId || ''
        }))
        // 顶级科室：parentId 为空或 '0'
        const parents = items.filter(it => !it.parentId || it.parentId === '0')
        const children = items.filter(it => it.parentId && it.parentId !== '0')
        // 疾病页左侧使用二级科室
        const diseaseDepts = children.filter(it => it.id && it.name)
        const diseaseDeptLeft = diseaseDepts.map(d => d.name)
        const diseaseDeptIds = diseaseDepts.map(d => d.id)
        // children map
        const childrenMap = {}
        items.forEach(it => {
          const p = it.parentId || '0'
          if (!childrenMap[p]) childrenMap[p] = []
          childrenMap[p].push(it)
        })
        // 科室 Tab 左侧（顶级 + 不限）
        const deptLeft = ['不限科室', ...parents.map(p => p.name)]
        // 默认选中第一个顶级（索引1），右侧显示其二级科室
        const defaultParentId = parents[0] ? parents[0].id : '0'
        const rightList = (childrenMap[defaultParentId] || []).map(c => ({ id: c.id, name: c.name }))
        this.setData({
          deptItems: items,
          deptParents: parents,
          deptChildrenMap: childrenMap,
          deptLeft,
          diseaseDeptLeft,
          diseaseDeptIds,
          leftIndex: 1,
          rightList
        })
      },
      fail: () => {
        wx.showToast({ title: '科室数据加载失败', icon: 'none' })
      }
    })
  },

  onMainTab(e) {
    const tab = e.currentTarget.dataset.tab
    if (tab === this.data.mainTab) return
    let leftIndex = 0
    let rightList = []
    let currentDeptId = ''
    let currentSubDeptId = ''
    let diseaseList = []

    if (tab === 'dept') {
      leftIndex = this.data.deptParents.length ? 1 : 0
      if (this.data.deptParents.length) {
        const parent = this.data.deptParents[0]
        rightList = (this.data.deptChildrenMap[parent.id] || []).map(c => ({ id: c.id, name: c.name }))
      } else {
        rightList = [{ id: '', name: '暂无数据' }]
      }
    } else {
      // 疾病 Tab：左侧二级科室，右侧疾病列表（由 illness 表接口返回）
      leftIndex = 0
      if (this.data.diseaseDeptIds.length) {
        currentSubDeptId = this.data.diseaseDeptIds[0]
      }
      rightList = []
    }

    this.setData({ mainTab: tab, leftIndex, rightList, currentDeptId, currentSubDeptId, diseaseList })
    if (tab === 'disease' && currentSubDeptId) {
      this.loadDiseases(currentSubDeptId)
    }
  },

  onLeftTap(e) {
    const idx = Number(e.currentTarget.dataset.index)
    const { mainTab } = this.data
    if (mainTab === 'dept') {
      if (idx === 0) {
        this.setData({ leftIndex: idx, rightList: [{ id: '', name: '全部' }], currentDeptId: '' })
        return
      }
      if (this.data.deptParents.length) {
        const parent = this.data.deptParents[idx - 1]
        const children = this.data.deptChildrenMap[parent.id] || []
        const names = children.map(c => ({ id: c.id, name: c.name }))
        this.setData({ leftIndex: idx, rightList: names, currentDeptId: '' })
      } else {
        this.setData({ leftIndex: idx, rightList: [], currentDeptId: '' })
      }
    } else {
      // 疾病模式：左侧直接是二级科室，点击后加载该科室下疾病
      const subId = this.data.diseaseDeptIds[idx] || ''
      this.setData({ leftIndex: idx, rightList: [], currentDeptId: '', currentSubDeptId: subId })
      if (subId) this.loadDiseases(subId); else this.setData({ diseaseList: [] })
    }
  },

  onSearchInput(e) {
    this.setData({ keyword: e.detail.value })
  },

  // 加载疾病（可按科室过滤）
  loadDiseases(departmentId) {
    wx.request({
      url: `${this.getApiBase()}/illness/list${departmentId ? ('?departmentId=' + encodeURIComponent(departmentId)) : ''}`,
      method: 'GET',
      success: (res) => {
        const list = Array.isArray(res.data) ? res.data : []
        const diseases = list.map(it => ({
          id: it.illnessId || it.illness_id || '',
          name: it.illnessName || it.illness_name || it.name || ''
        }))
        this.setData({ diseaseList: diseases })
      }
    })
  },

  onSubTap(e) {
    const deptIdFromData = e.currentTarget.dataset.deptId || ''
    const deptNameFromData = e.currentTarget.dataset.deptName || ''
    if (this.data.mainTab === 'disease') {
      // 疾病模式右侧不再展示二级科室，此处无需处理
      return
    }
    // 尝试匹配选中的子科室到部门ID
    let deptId = deptIdFromData
    let deptName = deptNameFromData
    if (!deptId && this.data.deptParents.length) {
      const allChildren = Object.values(this.data.deptChildrenMap).flat()
      const found = allChildren.find(c => c.name === deptName)
      if (found) {
        deptId = found.id
        deptName = found.name
      }
    }
    const vm = this.data.visitMode ? `&visitMode=${encodeURIComponent(this.data.visitMode)}` : ''
    wx.navigateTo({
      url: `/pages/doctor-list/doctor-list?dept=${encodeURIComponent(deptName || '')}&deptId=${encodeURIComponent(deptId)}${vm}`
    })
  },

  // 点击疾病项才跳转医生列表
  onDiseaseTap(e) {
    const illnessId = e.currentTarget.dataset.illnessId || ''
    const diseaseName = e.currentTarget.dataset.illnessName || ''
    const deptId = this.data.currentSubDeptId || ''
    if (!deptId) {
      wx.showToast({ title: '请先选择二级科室', icon: 'none' })
      return
    }
    const deptName = this.buildDeptDisplayName(deptId)
    const vm = this.data.visitMode ? `&visitMode=${encodeURIComponent(this.data.visitMode)}` : ''
    wx.navigateTo({
      url: `/pages/doctor-list/doctor-list?dept=${encodeURIComponent(deptName)}&deptId=${encodeURIComponent(deptId)}&disease=${encodeURIComponent(diseaseName)}&illnessId=${encodeURIComponent(illnessId)}${vm}`,
      fail: () => wx.showToast({ title: '跳转失败', icon: 'none' })
    })
  },

  buildDeptDisplayName(subDeptId) {
    if (!subDeptId) return '科室'
    const sub = this.data.deptItems.find(d => d.id === subDeptId)
    if (!sub) return '科室'
    const parent = this.data.deptItems.find(d => d.id === sub.parentId)
    return parent && parent.name ? `${parent.name} · ${sub.name}` : sub.name
  },

  noop() {}
})
