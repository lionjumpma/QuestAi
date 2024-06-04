/**
 * @Author: mayixiang
 * @Date: 2024-05-29
 *
 */

package com.sprint.questai.xinference;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.questai.model.enums.NameEnums;
import dev.langchain4j.data.segment.TextSegment;

import java.util.*;

public abstract class XHandler {
    String baseUrl = "http://localhost:9997";
    Map<String, String> headers= new HashMap<String,String>();
    String modelUid= NameEnums.BGE_RERANKER_BASE_NAME.toString();
    public Map<String, Object> rerank(String query, String docs) {
        throw new RuntimeException("no rerank methods");
    }
    public Map<String, Object> rerankAll(String queries, List<String> docs) {
        throw new RuntimeException("no rerank methods");
    }
}
class RerankHandler extends XHandler {
    public Map<String, Object> rerank(String query, String docs) {
        return rerank(query, docs, 10, 10, false);
    }

    public Map<String, Object> rerank(String query, String docs, int topN, int maxChunk, boolean returnDoc) {
        List<String> docList = new ArrayList<>();
        docList.add(docs);
        List<Map<String, Object>> results = rerankAll(query, docList, topN, maxChunk, returnDoc);
        return results.isEmpty() ? null : results.get(0);
    }

    public Map<String, Object> rerankAll(String queries, List<String> docs) {
        return rerankAll(queries, docs, 10, 10, false).get(0);
    }
    public List<Map<String, Object>> rerankAll(String query, List<String> docs, int topN, int maxChunk, boolean returnDoc) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Object>> allResults = new ArrayList<>();

                String url = baseUrl + "/v1/rerank";
                Map<String, Object> body = new HashMap<>();
                body.put("model", modelUid);
                body.put("query", query);
                body.put("documents", docs);
                String bodyStr = mapper.writeValueAsString(body);

                // 发送 HTTP POST 请求
                HttpResponse response = HttpRequest.post(url)
                        .header("Authorization", headers.get("Authorization"))
                        .body(bodyStr)
                        .execute();

                if (response.getStatus() != 200) {
                    throw new RuntimeException("rerank error: " + response.body());
                }

                String jsonResponse = response.body();
                Map<String, Object> responseMap = mapper.readValue(jsonResponse, new TypeReference<Map<String, Object>>() {});

                // 修改每个结果项的 "document" 字段
                List<Map<String, Object>> results = (List<Map<String, Object>>) responseMap.get("results");
                for (Map<String, Object> result : results) {
                    Integer index = (Integer) result.get("index");  // 获取 index
                    if (index != null && index < docs.size()) {
                        result.put("document", docs.get(index));  // 设置 "document" 字段
                    }
                }
                allResults.add(responseMap);


            return allResults;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}
