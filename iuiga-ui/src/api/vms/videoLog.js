import request from '@/utils/request'

// 查询参数列表
export function listVideoLog(query) {
  return request({
    url: '/vms/videoLog/list',
    method: 'get',
    params: query
  })
}

// 查询参数详细
export function getVideoLog(videoId) {
  return request({
    url: '/vms/videoLog/' + videoId,
    method: 'get'
  })
}

// 删除参数配置
export function delVideoLog(videoId) {
  return request({
    url: '/vms/videoLog/' + videoId,
    method: 'delete'
  })
}
