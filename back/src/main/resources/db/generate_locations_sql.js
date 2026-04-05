/**
 * 从 pc-code.json 生成 locations 表 INSERT（与 doctor.localtion_id 对齐）
 * 运行: node generate_locations_sql.js
 */
const fs = require('fs')
const path = require('path')

const root = __dirname
const pcPath = path.join(root, 'pc-code.json')
const outPath = path.join(root, 'locations_data.sql')

const pc = JSON.parse(fs.readFileSync(pcPath, 'utf8'))

function toProvinceId(code) {
  const c = String(code).trim()
  return c.padEnd(6, '0').slice(0, 6)
}

function toCityId(code) {
  const c = String(code).trim()
  if (c.length < 6) return c.padEnd(6, '0')
  return c.slice(0, 12)
}

function esc(s) {
  return String(s).replace(/\\/g, '\\\\').replace(/'/g, "''")
}

let sortNo = 0
const rows = []

for (const prov of pc) {
  const pid = toProvinceId(prov.code)
  rows.push({ id: pid, name: prov.name, parent: null, level: 1, sort: sortNo++ })
  for (const city of prov.children || []) {
    const cid = toCityId(city.code)
    rows.push({ id: cid, name: city.name, parent: pid, level: 2, sort: sortNo++ })
  }
}

// 国家统计局常用补充（pc-code 通常不含港澳台，此处补省级）
const extraProvinces = [
  ['710000', '台湾省', null, 1],
  ['810000', '香港特别行政区', null, 1],
  ['820000', '澳门特别行政区', null, 1]
]
for (const [id, name, parent, lv] of extraProvinces) {
  if (rows.some(r => r.id === id)) continue
  rows.push({ id, name, parent, level: lv, sort: sortNo++ })
}

const lines = []
lines.push('-- 由 generate_locations_sql.js 自 pc-code.json 生成；可与 locations_schema.sql 一起执行')
lines.push('SET NAMES utf8mb4;')
lines.push('SET FOREIGN_KEY_CHECKS = 0;')
lines.push('TRUNCATE TABLE `locations`;')
lines.push('')

const batchSize = 80
for (let i = 0; i < rows.length; i += batchSize) {
  const chunk = rows.slice(i, i + batchSize)
  const vals = chunk
    .map(
      r =>
        `('${esc(r.id)}','${esc(r.name)}',${r.parent == null ? 'NULL' : `'${esc(r.parent)}'`},${r.level},${r.sort})`
    )
    .join(',\n')
  lines.push(
    `INSERT INTO \`locations\` (\`localtion_id\`, \`localtion_name\`, \`parent_localtion_id\`, \`level_type\`, \`sort_no\`) VALUES\n${vals};`
  )
  lines.push('')
}

lines.push('SET FOREIGN_KEY_CHECKS = 1;')
fs.writeFileSync(outPath, lines.join('\n'), 'utf8')
console.log('Wrote', outPath, 'rows:', rows.length)
