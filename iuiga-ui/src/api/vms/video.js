import request from '@/utils/request'

// 查询参数列表
export function listVideo(query) {
  return request({
    url: '/vms/video/list',
    method: 'get',
    params: query
  })
}

// 查询参数详细
export function getVideo(videoId) {
  return request({
    url: '/vms/video/' + videoId,
    method: 'get'
  })
}

// 新增参数配置
export function addVideo(data) {
  return request({
    url: '/vms/video',
    method: 'post',
    data: data
  })
}

// 修改参数配置
export function updateVideo(data) {
  return request({
    url: '/vms/video',
    method: 'put',
    data: data
  })
}

// 删除参数配置
export function delVideo(videoId) {
  return request({
    url: '/vms/video/' + videoId,
    method: 'delete'
  })
}
