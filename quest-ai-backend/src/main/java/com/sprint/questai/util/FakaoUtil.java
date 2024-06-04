package com.sprint.questai.util;

import cn.hutool.core.io.resource.ResourceUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.questai.model.entity.fakao.FakaoInfo;
import org.elasticsearch.client.license.LicensesStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: mayixiang
 * @Date: 2024/5/30 18:31
 * @Description: 法考工具类，用于从resource中获取法考信息并加入向量数据库
 */

/**
 * [
 *  {
 *   "input": "甲男与乙女于某日中午公开在某公园内发生性关系，引起游客的极大愤慨，造成恶劣的社会影响。对甲、乙的行为应如何认定?",
 *   "output": "在分析甲男与乙女在公园内公开发生性关系的行为是否构成犯罪之前，我们需要明确相关法律规定。在中国法律体系中，可能涉及的罪名主要有“寻衅滋事罪”、“公然侮辱罪”和“猥亵罪”。\n\n首先，考虑是否构成“寻衅滋事罪”。根据《中华人民共和国刑法》第二百九十三条的规定，寻衅滋事罪是指寻衅滋事，扰乱公共秩序的行为。然而，甲男与乙女的行为虽然在公共场合发生，但其主要行为是两人之间的私人行为，没有直接目的去扰乱公共秩序或者寻找与他人的冲突，因此难以构成寻衅滋事罪。\n\n其次，考虑是否构成“公然侮辱罪”。根据《中华人民共和国刑法》第二百四十六条，公然侮辱他人，情节严重的，构成公然侮辱罪。甲男与乙女的行为虽然在公共场所进行，但其行为的对象并非特定的他人，而是两人之间的私人行为，因此不构成对他人的“公然侮辱”。\n\n再次，考虑是否构成“猥亵罪”。根据《中华人民共和国刑法》第二百三十七条，以暴力、胁迫手段或者其他方法强制猥亵他人或者侮辱妇女的，构成猥亵罪。甲男与乙女的性关系是公开且双方自愿的，不存在使用暴力、胁迫或其他方法的情形，因此不构成猥亵罪。\n\n最后，虽然甲男与乙女的行为在道德上可能受到公众的谴责，并且可能违反了公共场所的管理规定，但在刑法层面上，他们的行为不构成上述任何一种犯罪。因此，从法律的角度来看，甲男与乙女的行为应当被认定为“无罪”。\n\n综上所述，甲男与乙女在公园内公开发生性关系的行为，虽然可能违反了社会公德和公共场所的管理规定，但根据现行的《中华人民共和国刑法》的相关规定，他们的行为不构成任何犯罪，因此应当被认定为“无罪”。",
 *   "type": "qingjing"
 *  },
 *  {
 *   "input": "Question: 根据修订的刑诉法的规定，侦查机关询问证人不可在什么地点进行?\nA. 现场\nB. 证人所在单位或住处\nC. 证人本人提出的地点\nD. 侦查机关指定的地点",
 *   "output": "分析：侦查机关在询问证人时，选择的地点应当保障证人的合法权益，同时也要考虑到调查的有效性和便利性。选项A“现场”是侦查机关进行询问的常见地点之一，因为现场可以帮助证人回忆事件的情况，有助于获取更准确的证词。选项B“证人所在单位或住处”考虑到了证人的便利和舒适度，有时在证人的生活或工作环境中进行询问，可以减少证人的紧张情绪，有助于获取真实的证词。选项C“证人本人提出的地点”体现了对证人意愿的尊重，只要该地点不影响询问的公正性和有效性，侦查机关通常会考虑证人的请求。\n\n选项D“侦查机关指定的地点”在实际操作中也是常见的，但根据修订的刑事诉讼法的规定，侦查机关应当尊重和保障证人的合法权益，避免对证人造成不必要的压力或影响其正常生活和工作。如果侦查机关无理由地限定特定的询问地点，可能会被视为对证人权益的不当限制，尤其是如果该地点给证人带来不便或压力。因此，虽然侦查机关有权指定询问地点，但这种指定应当基于案件的具体情况和证人的实际情况，不能随意指定。\n\n根据《中华人民共和国刑事诉讼法》的相关规定，侦查机关在询问证人时应当采取有利于保护证人合法权益的措施，避免对证人造成不必要的困扰。因此，选项D“侦查机关指定的地点”是不可随意进行的，需要考虑到证人的权益和案件的实际需要。因此，正确答案是D。",
 *   "type": "gainian"
 *  },
 *  ]
 */
@Component
public class FakaoUtil {
    public List<FakaoInfo> parseFakaoInfo(){
        //read fakao info from resource
    String basicPath = "legal_data/fakao/fakao_gpt4.json";
    List<FakaoInfo> fakaoInfos = new ArrayList<>() ;
    ObjectMapper mapper = new ObjectMapper();
    try {
        JsonNode jsonNode = mapper.readTree(ResourceUtil.getStream(basicPath));
        for (JsonNode node : jsonNode) {
            FakaoInfo fakaoInfo = mapper.treeToValue(node, FakaoInfo.class);
            fakaoInfos.add(fakaoInfo);
        }

    } catch (IOException e) {
        throw new RuntimeException(e);
    }
    return fakaoInfos;

    }

    public static void main(String[] args) {
        FakaoUtil fakaoUtil = new FakaoUtil();
        List<FakaoInfo> fakaoInfos = fakaoUtil.parseFakaoInfo();
        for (FakaoInfo fakaoInfo : fakaoInfos) {
            System.out.println(fakaoInfo);
        }
    }
}
