/**
 * @Author: chenzehao
 * @Date: 2024-05-29
 *
 */
package com.sprint.questai.rerank;

import com.sprint.questai.xinference.XHandler;
import com.sprint.questai.xinference.XClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class RerankTest {
    XClient client = new XClient();
    @Test
    void getModelTest() {
        XHandler handler = client.getModel("bge-reranker-base");
    }
    @Test
    void rerankModelTest() {
        String query = "我是一名软件工程师，最近因为公司裁员被迫解除劳动合同，但我认为解雇过程中没有遵守相关法律规定。我想了解如何启动劳动仲裁程序，以及在劳动仲裁过程中需要准备哪些证据和文件？";
        List<String> docs = Arrays.asList(
                "劳动合同终止的法律规定包括合同期满、违反合同条款或经济性裁员，每种情况下的法律后果和员工权利都有所不同。",
                "解除劳动合同需要符合法律规定的条件，包括严重违纪和经济性裁员。员工可以对不公正解雇提起诉讼，并要求赔偿。",
                "劳动争议的调解程序通常包括提交申请、调解会议和达成调解协议。调解失败后，可以进一步申请劳动仲裁或法院诉讼。",
                "公司处理员工不当行为的措施包括警告、罚款或解除合同。每项措施都必须严格按照劳动法规定执行。",
                "劳动合同纠纷的常见类型包括工资支付争议、工作时间和休息休假争议。这些争议常常需要通过法律途径解决。",
                "申请劳动仲裁需要提交仲裁申请书和相关证据材料，如合同副本、工资单、解雇通知等，以证明解雇的不合理性。",
                "员工权益保护的法律依据主要来自劳动法和相关法律规定。了解这些法律对员工在争议中的立场至关重要。",
                "在一起实际案例中，一名工程师因公司重组被解雇，后成功通过劳动仲裁获得了补偿。此案例涉及多项证据，包括不公正解雇的邮件通知和公司内部政策。",
                "软件开发项目管理的最佳实践包括敏捷开发和持续集成。虽然这与劳动法无关，但了解这些可以帮助软件工程师在职业生涯中取得成功。",
                "如何提高编程技能：参加在线课程、阅读最新的技术书籍。这些资源可以帮助个人在技术领域保持竞争力。",
                "劳动法规定加班应支付不低于工资的150%的加班费。了解这些规定对于争取自己的权益非常重要。"
        );
        XHandler handler= client.getModel("bge-reranker-base");
        Map<String, Object> rerank = handler.rerankAll(query, docs);
        System.out.println(rerank);

    }
}
