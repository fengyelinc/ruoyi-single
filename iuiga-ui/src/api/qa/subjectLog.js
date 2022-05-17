import request from '@/utils/request'

// 查询公告列表
export function listSubjectLog(query) {
  return request({
    url: '/qa/subjectLog/list',
    method: 'get',
    params: query
  })
}

// 查询公告详细
export function getSubjectLog(subjectLogId) {
  return request({
    url: '/qa/subjectLog/' + subjectLogId,
    method: 'get'
  })
}