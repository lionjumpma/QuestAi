/**
 * @Author: mayixiang
 * @Date: 2024-05-29
 */

package com.sprint.questai.xinference;


import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.sprint.questai.model.enums.NameEnums;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XClient {
    String baseUrl = "http://localhost:9997";
    Map<String, String> headers= new HashMap<String,String>();
    String modelUid= NameEnums.BGE_RERANKER_BASE_NAME.toString();

    public XClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    public XClient(String baseUrl, String model) {
        this.baseUrl = baseUrl;
        this.modelUid = model;
    }
    public XClient() {
    }
    public XHandler getModel(String model){
        String url=baseUrl+"/v1/models/"+model;
        HttpResponse execute = HttpRequest.get(url)
                .header("Authorization", headers.get("Authorization"))
                .execute();
        if(execute.getStatus()!=200){
            throw new RuntimeException("get model error:"+execute.body());
        }
        String body = execute.body();
        ObjectMapper mapper = new ObjectMapper();
        try{
            JsonNode jsonNode = mapper.readTree(body);
            String type = jsonNode.get("model_type").asText();
            if(type.equals("rerank")){
                return new RerankHandler();
            }
        } catch (Exception e) {
            throw new RuntimeException("get model error:"+body);
        }
        throw new RuntimeException("get model error:"+body);
    }
    public XHandler getModel(){
        return getModel(modelUid);
    }


}
