import request from "@/utils/request";

// 查询消息列表
export function listMessage(query) {
  return request({
    url: "/system/message/list",
    method: "get",
    params: query,
  });
}

// 查询消息详情
export function getMessage(id) {
  return request({
    url: "/system/message/info/" + id,
    method: "get",
  });
}

// 新增消息
export function addMessage(data) {
  return request({
    url: "/system/message",
    method: "post",
    data: data,
  });
}

// 编辑消息
export function updateMessage(data) {
  return request({
    url: "/system/message",
    method: "put",
    data: data,
  });
}

// 删除消息
export function deleteMessagee(id) {
  return request({
    url: "/system/message/" + id,
    method: "delete",
  });
}

// 刷新缓存
export function refreshMessage() {
  return request({
    url: "/system/message/refreshCache",
    method: "post",
  });
}