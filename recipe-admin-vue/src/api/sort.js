import request from '@/utils/request'

// -------FirstSort----start---------

export function getFirstSortList(page) {
  return request({
    url: '/first-sort/getFirstSortList',
    method: 'post',
    data: page
  })
}

export function getFirstSortNameList() {
  return request({
    url: '/first-sort/getFirstSortNameList',
    method: 'get'
  })
}

export function updateFirstSort(firstsort) {
  return request({
    url: '/first-sort/updateFirstSort',
    method: 'post',
    data: firstsort
  })
}

export function addFirstSort(firstsort) {
  return request({
    url: '/first-sort/addFirstSort',
    method: 'post',
    data: firstsort
  })
}

export function deleteFirstSort(sortId) {
  return request({
    url: '/first-sort/deleteFirstSortById',
    method: 'post',
    params: { "id": sortId }
  })
}

//---------FirstSort--end-------


// -------SecondSort----start---------

export function getWholeSortByPage(page) {
  return request({
    url: '/second-sort/getWholeSortByPage',
    method: 'post',
    data: page
  })
}
export function addSecondSort(secondsort) {
  return request({
    url: '/second-sort/addSecondSort',
    method: 'post',
    data: secondsort
  })
}
export function updateSecondSort(secondsort) {
  return request({
    url: '/second-sort/updateSecondSort',
    method: 'post',
    data: secondsort
  })
}
export function deleteSecondSort(sortId) {
  return request({
    url: '/second-sort/deleteSecondSort',
    method: 'post',
    params: { "id": sortId }
  })
}
//---------SecondSort--end-------