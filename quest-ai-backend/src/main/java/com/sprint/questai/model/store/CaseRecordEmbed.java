package com.sprint.questai.model.store;

import dev.langchain4j.data.document.Metadata;
import lombok.Data;

import java.util.HashMap;

//      {
//        "_index": "case_record",
//        "_id": "c4067ab8-1531-4f39-b782-4afa11fa7cf0",
//        "_score": 1,
//        "_source": {
//          "vector": "[]",
//          "text": "原告窦某与被告周某抚养纠纷一案，本院于2021年8月23日立案后，依法适用简易程序，公开开庭进行了审理，原告窦某，被告周某到庭参加诉讼，本案现已审理终结",
//          "metadata": {
//            "category2": "孩子抚养",
//            "keywords": "[婚生子女, 婚姻, 抚养费]",
//            "caseId": "9214edcf86e34265bba6adf200e5a827",
//            "category1": "婚姻家庭"
//          }
//        }
//      },

@Data
public class CaseRecordEmbed {

    private double[] vector;
    private String text;
    private HashMap<String,String> metadata;


}
