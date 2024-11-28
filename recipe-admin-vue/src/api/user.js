import request from '@/utils/request'

export function doLogin(admName, admPwd) {
  return request({
    url: '/admin/doLogin',
    method: 'post',
    params: { "admName": admName, "admPwd": admPwd }
  })
};
export function getUserList(page, type) {
  return request({
    url: '/admin/getUserList',
    method: 'post',
    params: { "type": type },
    data: page
  });
};
export function getUserInfoByName(page, userName) {
  return request({
    url: '/admin/getUserInfoByName',
    method: 'post',
    params: { "userName": userName },
    data: page
  });
};
export function freezeUser(userId, isFrozen) {
  return request({
    url: '/admin/freezeUser',
    method: 'post',
    params: { "userId": userId, "isFrozen": isFrozen }
  })
};


export function updateAdminInfo(oldName, oldPwd, newName, newPwd) {
  return request({
    url: '/admin/updateAdminInfo',
    method: 'post',
    params: { "oldName": oldName, "oldPwd": oldPwd, "newName": newName, "newPwd": newPwd }
  })
};


