import request from '@/utils/request'

export function getAllAd() {
  return request({
    url: '/ad/getAllAd',
    method: 'get'
  })
}
export function updateAd(ad) {
  return request({
    url: '/ad/updateAd',
    method: 'post',
    data: ad
  })
}