const app = getApp()

function safeDecode(v) {
  if (!v) return ''
  try {
    return decodeURIComponent(v)
  } catch (e) {
    return v
  }
}

function pad2(n) {
  return String(n).padStart(2, '0')
}

function buildDateList() {
  const list = [{ week: '不限时间', day: '', unlimited: true }]
  const now = new Date()
  const weekdays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']
  for (let i = 0; i < 7; i++) {
    const d = new Date(now)
    d.setDate(now.getDate() + i)
    const day = `${pad2(d.getMonth() + 1)}-${pad2(d.getDate())}`
    const week = i === 0 ? '今天' : weekdays[d.getDay()]
    list.push({ week, day, unlimited: false })
  }
  return list
}

function buildDeptLine(d, fallback) {
  const p = d.parentDepartmentName || d.parent_department_name
  const s = d.departmentName || d.department_name
  if (p && s) return `${p} · ${s}`
  if (s) return s
  return fallback || '科室'
}

Page({
  data: {
    selectedDate: 0,
    dateList: buildDateList(),
    deptFilterDisplay: '选择科室',
    deptPickerOpen: false,
    deptLeftList: [],
    deptItems: [],
    overlayDeptIndex: 0,
    illnessPanelList: [],
    selectedDept: '',
    selectedDisease: '',
    keyword: '',
    baseUrl: '',
    deptId: '',
    illnessId: '',
    locationId: '',
    regionFilterDisplay: '全国',
    regionPickerOpen: false,
    provinceListRaw: [],
    locationProvinceList: [],
    locationCityList: [],
    overlayLocationProvinceIndex: 0,
    locationTreeCached: [],
    doctorList: [],
    /** online=在线问诊 offline=预约挂号，透传 doctor/query */
    visitMode: ''
  },

  /** setData 异步，请求必须用同步保存的 base，否则 this.data.baseUrl 仍为空 */
  getApiBase() {
    if (this._apiBase) return this._apiBase
    const g = getApp().globalData && getApp().globalData.apiBaseUrl
    return g || 'http://localhost:8080/com/back/api'
  },

  onLoad(options) {
    const dept = safeDecode(options.dept)
    const deptId = options.deptId || ''
    const illnessId = options.illnessId || ''
    const selectedDisease = safeDecode(options.disease)
    const baseUrl = app.globalData.apiBaseUrl || 'http://localhost:8080/com/back/api'
    this._apiBase = baseUrl
    this._locationTreeLoaded = false
    this._locationPickerLoading = false
    this._locationPickerWaiters = []
    this._lastPickerSelection = null
    const loc = options.locationId || ''
    const visitMode = (options.visitMode || '').trim()
    this.setData({
      baseUrl,
      deptId,
      illnessId,
      selectedDept: dept || '',
      selectedDisease,
      deptFilterDisplay: deptId || illnessId ? (dept || '选择科室') : '选择科室',
      locationId: loc,
      visitMode
    })
    if (visitMode === 'offline') {
      wx.setNavigationBarTitle({ title: '预约挂号' })
    } else if (visitMode === 'online') {
      wx.setNavigationBarTitle({ title: '在线问诊' })
    }
    this.loadDepartmentTree(deptId, illnessId, selectedDisease, dept)
    if (loc) {
      this.fetchLocationPicker(() => {}, loc)
    }
    this.loadDoctors()
  },

  /**
   * 从 GET /location/picker 返回的 selection 计算顶栏文案（与 locationId 一致时由后端解析）。
   */
  computeRegionBarTextFromSelection(sel) {
    if (!sel) return ''
    const cityId = sel.cityId || sel.city_id || ''
    if (!cityId) return ''
    const p = sel.provinceName || sel.province_name || ''
    const city = sel.cityName || sel.city_name || ''
    if (!city) return ''
    return p && p !== city ? `${p} · ${city}` : city
  },

  /**
   * 地区二级联动：GET /location/picker；失败不标记已加载以便重试。
   * @param {function} done
   * @param {string} [locationIdForQuery] onLoad 时传入 options.locationId，避免 setData 竞态
   */
  fetchLocationPicker(done, locationIdForQuery) {
    if (this._locationTreeLoaded) {
      if (typeof done === 'function') done()
      return
    }
    if (this._locationPickerLoading) {
      if (typeof done === 'function') {
        this._locationPickerWaiters.push(done)
      }
      return
    }
    this._locationPickerLoading = true
    const loc =
      locationIdForQuery !== undefined && locationIdForQuery !== null && String(locationIdForQuery).trim() !== ''
        ? String(locationIdForQuery).trim()
        : (this.data.locationId || '').trim()
    const base = (this.getApiBase() || '').replace(/\/+$/, '')
    const url = loc
      ? `${base}/location/picker?locationId=${encodeURIComponent(loc)}`
      : `${base}/location/picker`
    const runWaiters = () => {
      this._locationPickerLoading = false
      const waiters = this._locationPickerWaiters.splice(0)
      if (typeof done === 'function') done()
      waiters.forEach(fn => typeof fn === 'function' && fn())
    }
    wx.request({
      url,
      method: 'GET',
      header: { Accept: 'application/json' },
      success: (res) => {
        const sc = res.statusCode
        if (sc < 200 || sc >= 300) {
          console.error('[doctor-list] location/picker HTTP', sc, url, res.data)
          wx.showToast({ title: '地区数据加载失败', icon: 'none' })
          this.setData({ locationTreeCached: [], provinceListRaw: [] }, () => runWaiters())
          return
        }
        const body = res.data && typeof res.data === 'object' ? res.data : {}
        const list = Array.isArray(body.provinces) ? body.provinces : []
        const sel = body.selection || null
        this._lastPickerSelection = sel
        const raw = list
          .map(p => ({
            id: p.provinceId || p.province_id || '',
            name: p.provinceName || p.province_name || ''
          }))
          .filter(p => p.id && p.name)
        const patch = { locationTreeCached: list, provinceListRaw: raw }
        const bar = this.computeRegionBarTextFromSelection(sel)
        if (bar) patch.regionFilterDisplay = bar
        this._locationTreeLoaded = true
        this.setData(patch, () => runWaiters())
      },
      fail: err => {
        console.error('[doctor-list] location/picker wx.request fail', url, err)
        wx.showToast({ title: '地区数据加载失败', icon: 'none' })
        this.setData({ locationTreeCached: [], provinceListRaw: [] }, () => runWaiters())
      }
    })
  },

  findPickerPosition(tree, locationId) {
    if (!locationId || !Array.isArray(tree)) return null
    for (let ti = 0; ti < tree.length; ti++) {
      const p = tree[ti]
      const cities = p.cities || []
      for (let ci = 0; ci < cities.length; ci++) {
        const c = cities[ci]
        const id = c.locationId || c.location_id
        if (id === locationId) return { group: p, city: c, treeIndex: ti }
      }
    }
    return null
  },

  buildProvinceListUI() {
    const raw = this.data.provinceListRaw || []
    return [{ id: '', name: '全国' }].concat(raw)
  },

  loadCitiesForProvince(provinceId, after) {
    const tree = this.data.locationTreeCached || []
    const applyCities = rows => {
      const lid = this.data.locationId || ''
      const list = [{ id: '', name: '不限', checked: !lid }].concat(
        rows.map(c => ({
          id: c.id,
          name: c.name,
          checked: !!lid && lid === c.id
        }))
      )
      const matched = list.some(i => i.checked)
      if (!matched && list[0] && !lid) list[0].checked = true
      this.setData({ locationCityList: list }, () => {
        if (typeof after === 'function') after()
      })
    }
    if (!provinceId) {
      applyCities([])
      return
    }
    const g = tree.find(x => (x.provinceId || x.province_id) === provinceId)
    const rawCities = (g && g.cities) || []
    const rows = rawCities
      .map(c => ({
        id: c.locationId || c.location_id || '',
        name: c.locationName || c.location_name || ''
      }))
      .filter(c => c.id && c.name)
    applyCities(rows)
  },

  openRegionPicker() {
    this.fetchLocationPicker(() => {
      const provinces = this.buildProvinceListUI()
      const { locationId } = this.data
      const tree = this.data.locationTreeCached || []
      const openUi = (pidx, pid) => {
        this.setData({
          locationProvinceList: provinces,
          overlayLocationProvinceIndex: pidx
        })
        this.loadCitiesForProvince(pid, () => {
          this.setData({ regionPickerOpen: true })
        })
      }
      if (!locationId) {
        openUi(0, '')
        return
      }
      const pos = this.findPickerPosition(tree, locationId)
      if (pos) {
        const pid = pos.group.provinceId || pos.group.province_id || ''
        openUi(pos.treeIndex + 1, pid)
        return
      }
      const sel = this._lastPickerSelection
      if (sel) {
        const pid = sel.provinceId || sel.province_id || ''
        let pidx = provinces.findIndex(p => p.id === pid)
        if (pidx < 0) pidx = 0
        const finalPid = pid || ((provinces[pidx] || {}).id || '')
        openUi(pidx, finalPid)
        return
      }
      openUi(0, '')
    })
  },

  loadDepartmentTree(currentDeptId, currentIllnessId, currentIllnessName, entryDeptLabel) {
    wx.request({
      url: `${this.getApiBase()}/department/list`,
      method: 'GET',
      success: (res) => {
        const list = Array.isArray(res.data) ? res.data : []
        const items = list.map(it => ({
          id: it.departmentId || it.department_id || it.id || '',
          name: it.departmentName || it.department_name || it.name || '',
          parentId: it.departmentParentId || it.department_parent_id || it.parentId || ''
        }))
        const secondLevel = items.filter(it => it.id && it.name && it.parentId && it.parentId !== '0')
        let overlayDeptIndex = 0
        if (currentDeptId) {
          const i = secondLevel.findIndex(d => d.id === currentDeptId)
          if (i >= 0) overlayDeptIndex = i
        }
        let deptFilterDisplay = '选择科室'
        if (currentDeptId) {
          deptFilterDisplay = this.computeFilterBarLabel(
            currentDeptId,
            secondLevel,
            currentIllnessId,
            currentIllnessName
          )
        } else if (entryDeptLabel) {
          deptFilterDisplay = entryDeptLabel
        }
        let selectedDeptName = this.data.selectedDept
        if (currentDeptId) {
          const hit = secondLevel.find(d => d.id === currentDeptId)
          if (hit) selectedDeptName = hit.name
        }
        this.setData({
          deptItems: items,
          deptLeftList: secondLevel,
          overlayDeptIndex,
          deptFilterDisplay,
          selectedDept: selectedDeptName || this.data.selectedDept
        })
      }
    })
  },

  computeFilterBarLabel(deptId, secondLevel, illnessId, illnessName) {
    if (!deptId) return '选择科室'
    const row = secondLevel.find(d => d.id === deptId)
    const deptName = row ? row.name : this.data.selectedDept
    if (!deptName) return '选择科室'
    if (illnessId && illnessName) return `${deptName} · ${illnessName}`
    return deptName
  },

  openDeptPicker() {
    if (this.data.regionPickerOpen) this.setData({ regionPickerOpen: false })
    const { deptLeftList, deptId, overlayDeptIndex } = this.data
    if (!deptLeftList.length) {
      wx.showToast({ title: '暂无科室数据', icon: 'none' })
      return
    }
    let idx = overlayDeptIndex
    if (deptId) {
      const i = deptLeftList.findIndex(d => d.id === deptId)
      if (i >= 0) idx = i
    }
    const row = deptLeftList[idx] || deptLeftList[0]
    this.setData({ deptPickerOpen: true, overlayDeptIndex: idx })
    this.refreshIllnessPanel(row.id)
  },

  refreshIllnessPanel(departmentId) {
    wx.request({
      url: `${this.getApiBase()}/illness/list?departmentId=${encodeURIComponent(departmentId)}`,
      method: 'GET',
      success: (res) => {
        const list = Array.isArray(res.data) ? res.data : []
        const rows = list.map(it => ({
          id: it.illnessId || it.illness_id || '',
          name: it.illnessName || it.illness_name || it.name || ''
        })).filter(it => it.id && it.name)
        const panel = [{ id: '', name: '不限', checked: false }]
        rows.forEach(r => panel.push({ ...r, checked: false }))
        this.applyIllnessChecked(panel, departmentId)
      },
      fail: () => {
        const panel = [{ id: '', name: '不限', checked: true }]
        this.setData({ illnessPanelList: panel })
      }
    })
  },

  applyIllnessChecked(panel, overlayDeptId) {
    const { deptId, illnessId } = this.data
    const sameDept = deptId === overlayDeptId
    const next = panel.map(it => {
      if (it.id === '') {
        return { ...it, checked: sameDept ? !illnessId : true }
      }
      return { ...it, checked: sameDept && illnessId === it.id }
    })
    const hasCheck = next.some(i => i.checked)
    if (!hasCheck && next[0]) next[0].checked = true
    this.setData({ illnessPanelList: next })
  },

  onDeptFilterBarTap() {
    if (this.data.deptPickerOpen) {
      this.setData({ deptPickerOpen: false })
      return
    }
    this.openDeptPicker()
  },

  onCascadeMaskTap() {
    this.setData({ deptPickerOpen: false, regionPickerOpen: false })
  },

  onCascadeDeptTap(e) {
    const idx = Number(e.currentTarget.dataset.index)
    const id = e.currentTarget.dataset.id || ''
    this.setData({ overlayDeptIndex: idx })
    this.refreshIllnessPanel(id)
  },

  onCascadeIllnessTap(e) {
    const ix = Number(e.currentTarget.dataset.index)
    const picked = (this.data.illnessPanelList || [])[ix]
    if (!picked) return
    const illnessId = picked.id || ''
    const name = picked.name || ''
    const row = this.data.deptLeftList[this.data.overlayDeptIndex]
    if (!row) return
    const deptId = row.id
    const secondLevel = this.data.deptLeftList
    const deptFilterDisplay = this.computeFilterBarLabel(deptId, secondLevel, illnessId, illnessId ? name : '')
    const selectedDisease = illnessId ? name : ''
    const panel = (this.data.illnessPanelList || []).map(it => ({
      ...it,
      checked: illnessId ? it.id === illnessId : it.id === ''
    }))
    this.setData({
      deptPickerOpen: false,
      deptId,
      illnessId,
      selectedDept: row.name,
      selectedDisease,
      deptFilterDisplay,
      illnessPanelList: panel
    })
    this.loadDoctors()
  },

  onRegionFilterTap() {
    if (this.data.deptPickerOpen) this.setData({ deptPickerOpen: false })
    if (this.data.regionPickerOpen) {
      this.setData({ regionPickerOpen: false })
      return
    }
    this.openRegionPicker()
  },

  onRegionMaskTap() {
    this.setData({ regionPickerOpen: false })
  },

  onLocationProvinceTap(e) {
    const lpi = Number(e.currentTarget.dataset.index)
    const pid = e.currentTarget.dataset.id || ''
    this.setData({ overlayLocationProvinceIndex: lpi })
    this.loadCitiesForProvince(pid)
  },

  onLocationCityTap(e) {
    const lci = Number(e.currentTarget.dataset.index)
    const row = (this.data.locationCityList || [])[lci]
    if (!row) return
    const provinces = this.data.locationProvinceList || []
    const pi = this.data.overlayLocationProvinceIndex
    const prov = provinces[pi] || { id: '', name: '' }
    const cityId = row.id || ''
    let regionFilterDisplay = '全国'
    if (!cityId) {
      this.setData({
        regionPickerOpen: false,
        locationId: '',
        regionFilterDisplay
      })
      this.loadDoctors()
      return
    }
    if (prov.name && row.name) {
      regionFilterDisplay = prov.name === row.name ? row.name : `${prov.name} · ${row.name}`
    } else {
      regionFilterDisplay = row.name || '全国'
    }
    const list = (this.data.locationCityList || []).map((it, i) => ({
      ...it,
      checked: i === lci
    }))
    this.setData({
      regionPickerOpen: false,
      locationId: cityId,
      regionFilterDisplay,
      locationCityList: list
    })
    this.loadDoctors()
  },

  onSortFilterTap() {
    if (this.data.deptPickerOpen) this.setData({ deptPickerOpen: false })
    if (this.data.regionPickerOpen) this.setData({ regionPickerOpen: false })
    wx.showToast({ title: '排序开发中', icon: 'none' })
  },

  /** 科室/疾病页带入的 deptId、illnessId 始终参与查询；仅在选择地区后追加 locationId */
  loadDoctors() {
    const { deptId: departmentId, illnessId, keyword, locationId, visitMode } = this.data
    const qs = []
    if (departmentId) qs.push(`departmentId=${encodeURIComponent(departmentId)}`)
    if (illnessId) qs.push(`illnessId=${encodeURIComponent(illnessId)}`)
    if (keyword) qs.push(`keyword=${encodeURIComponent(keyword)}`)
    if (locationId) qs.push(`locationId=${encodeURIComponent(locationId)}`)
    if (visitMode === 'online' || visitMode === 'offline') {
      qs.push(`visitMode=${encodeURIComponent(visitMode)}`)
    }
    const url = `${this.getApiBase()}/doctor/query${qs.length ? ('?' + qs.join('&')) : ''}`
    wx.request({
      url,
      method: 'GET',
      success: (res) => {
        const list = Array.isArray(res.data) ? res.data : []
        if (!list.length) {
          this.setData({ doctorList: [] })
          return
        }
        const doctors = list.map(d => ({
          id: d.doctorId || d.doctor_id || d.id || '',
          name: d.doctorName || d.doctor_name || d.name || '',
          title: d.doctorLevel || d.doctor_level || d.doctorLeverName || d.doctor_lever_name || '',
          dept: buildDeptLine(d, this.data.selectedDept),
          hospital: d.doctorHospitalName || d.doctor_hospital_name || '',
          avatarText: (d.doctorName || d.name || '医').substring(0, 1),
          specialty:
            d.doctorExclusiveProfessionIntrodction ||
            d.doctor_exclusive_profession_introdction ||
            d.doctorExclusiveIntroduction ||
            d.doctor_exclusive_introduction ||
            '',
          fans: 0,
          reputation: 0,
          schedules: []
        }))
        this.setData({ doctorList: doctors })
      },
      fail: () => {
        wx.showToast({ title: '医生数据加载失败', icon: 'none' })
      }
    })
  },

  onDateTap(e) {
    this.setData({ selectedDate: e.currentTarget.dataset.index })
  },

  onKeywordInput(e) {
    const keyword = (e.detail.value || '').trim()
    this.setData({ keyword })
    if (this._kwTimer) clearTimeout(this._kwTimer)
    this._kwTimer = setTimeout(() => {
      this.loadDoctors()
    }, 350)
  },

  onKeywordConfirm() {
    if (this._kwTimer) clearTimeout(this._kwTimer)
    this.loadDoctors()
  },

  onClearKeyword() {
    if (this._kwTimer) clearTimeout(this._kwTimer)
    this.setData({ keyword: '' })
    this.loadDoctors()
  },

  onAppointTap(e) {
    const { doctor, price } = e.currentTarget.dataset
    wx.showModal({
      title: '预约确认',
      content: `确定要预约 ${doctor} 医师吗？（费用：¥${price}）`,
      success: (res) => {
        if (res.confirm) {
          wx.showToast({ title: '预约功能开发中' })
        }
      }
    })
  },

  noop() {}
})
