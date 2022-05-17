import request from '@/utils/request'

// 查询公告列表
export function listSubject(query) {
  return request({
    url: '/qa/subject/list',
    method: 'get',
    params: query
  })
}

// 查询公告详细
export function getSubject(subjectId) {
  return request({
    url: '/qa/subject/' + subjectId,
    method: 'get'
  })
}

// 新增公告
export function addSubject(data) {
  return request({
    url: '/qa/subject',
    method: 'post',
    data: data
  })
}

// 修改公告
export function updateSubject(data) {
  return request({
    url: '/qa/subject',
    method: 'put',
    data: data
  })
}

// 删除公告
export function delSubject(subjectId) {
  return request({
    url: '/qa/subject/' + subjectId,
    method: 'delete'
  })
}

// 新增公告
export function addSubjectData(data) {
  return request({
    url: '/qa/subject/data',
    method: 'post',
    data: data
  })
}

// 修改公告
export function updateSubjectData(data) {
  return request({
    url: '/qa/subject/data',
    method: 'put',
    data: data
  })
}

// 删除公告
export function delSubjectData(subjectId) {
  return request({
    url: '/qa/subject/data/' + subjectId,
    method: 'delete'
  })
}