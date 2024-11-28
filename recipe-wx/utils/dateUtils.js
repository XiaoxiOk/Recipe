
// 时间戳转为正常时间的公共方法
function filterTime(time) {
    const date = new Date(time)
    const Y = date.getFullYear()
    const M = date.getMonth() + 1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1 
    const D = date.getDate()
    return `${Y}年${M}月${D}日`
  }

  module.exports = {
    filterTime: filterTime,
  }