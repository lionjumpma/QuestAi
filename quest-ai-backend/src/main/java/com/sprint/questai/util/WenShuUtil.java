package com.sprint.questai.util;

import cn.hutool.core.io.resource.ResourceUtil;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.questai.model.entity.wenshu.Case;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Component
public class WenShuUtil {
    @Resource
    private ElasticsearchClient client;
    public  List<Case> parseCases(String wenshuid){
        String basicPath = "legal_data/wenshu/dev/";
        String path = basicPath + wenshuid;
        List<Case> cases = new ArrayList<>() ;
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(ResourceUtil.getStream(path));
            JsonNode ctxsNode = jsonNode.get("ctxs");
            if (!ctxsNode.isMissingNode()) {
                ctxsNode.fieldNames().forEachRemaining(key -> {
                    JsonNode caseNode = ctxsNode.get(key);
                    try {
                        Case aCase = mapper.treeToValue(caseNode, Case.class);

                        //System.out.println(aCase);
                        cases.add(aCase);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }

                });


            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return cases;
    }
    public void saveCasesToEs(List<Case> cases){
        for (Case aCase : cases) {
            try {
                client.index(i -> i.index("cases").id(aCase.getCaseId()).document(aCase));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    //获取resource目录下path下的所有case

    public List<String> getWenShuFileNameByPath(String path){
        List<String> fileNames = new ArrayList<>();
// 创建File对象
        File folder = new File(path);

        // 获取文件夹下所有的文件和文件夹
        File[] files = folder.listFiles();
        // 检查files数组是否为空（即文件夹是否存在或非空）
        if (files != null) {
            for (File file : files) {
                // 如果只需要文件名，可以使用file.getName()
                // 如果需要完整路径，可以使用file.getAbsolutePath()
                fileNames.add(file.getName());
            }
        }

        // 打印出文件名列表
        //fileNames.forEach(System.out::println);
        return fileNames;
    }


}
