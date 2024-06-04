
import request from "@/config/axiosConfig.js"

//http://localhost:8080/chat/normal?model=GLM3&content=hello
export const aiChat = (model:String,content:String,conversationId:String) => {
  return request({
    url: '/chat/nokey',
    method: 'get',
    timeout: 20000,
    params: {
      model: model,
      content: content,
        conversationId: conversationId
    }
  })
}
