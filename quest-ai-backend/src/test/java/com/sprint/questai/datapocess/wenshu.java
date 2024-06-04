/**
 * @Author: chenzehao
 * @Date: 2024-05-02
 *
 */
package com.sprint.questai.datapocess;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.io.resource.Resource;
import cn.hutool.core.io.resource.ResourceUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.questai.model.entity.wenshu.Case;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class wenshu {

    //get wenshu from resources
    @Test
    public void getWenshu() {
        String path = "legal_data/wenshu/14501.json";
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(ResourceUtil.getStream(path));
            JsonNode ctxsNode = jsonNode.get("ctxs");
            if (!ctxsNode.isMissingNode()) {
                ctxsNode.fieldNames().forEachRemaining(key -> {
                    JsonNode caseNode = ctxsNode.get(key);
                    try {
                        Case aCase = mapper.treeToValue(caseNode, Case.class);
                        System.out.println(aCase);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }

                });


            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
