/**
 * @Author: chenzehao
 * @Date: 2024-05-29
 *
 */
package com.sprint.questai.config;

import com.sprint.questai.factory.ModelFactory;
import com.sprint.questai.model.enums.NameEnums;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ConfigFactoryTest {
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private ModelFactory modelFactory;

    @Test
    public void testConfig() {
        System.out.println(appConfig.getEmbeddingModel().getName());
        System.out.println(appConfig.getEmbeddingModel().getApiKey());
        System.out.println(appConfig.getDocumentSplitter());
        System.out.println(appConfig.getEmbeddingStore());
    }
    @Test
    public void testConfigFactory() {
        EmbeddingModel model = modelFactory.createEmbeddingModelByConfig();
        Response<Embedding> embedtest = model.embed("test");
        System.out.println(embedtest);
        DocumentSplitter splitter = modelFactory.createDocumentSplitterByConfig();
        List<TextSegment> split = splitter.split(Document.from("被告人雷志朋，男性，1978年**月**日出生，公民身份号码6104311978********，初中文化程度，无业，户籍所在地陕西省咸阳市武功县，住武功县**镇**村**组**号，2022年9月14日因涉嫌赌博罪被武功县公安局刑事拘留；同年10月1日被武功县公安局取保候审。\n" +
                "\n" +
                "本案由武功县公安局侦查终结，以被告人雷志朋涉嫌开设赌场罪，于2023年3月13日向本院移送起诉。本院受理后，已告知被告人有权委托辩护人和认罪认罚可能导致的法律后果，依法讯问了被告人，听取了值班律师的意见，审查了全部案件材料。被告人同意本案适用简易程序审理。\n" +
                "\n" +
                "经依法审查查明：\n" +
                "\n" +
                "2022年6月初至9月12日期间，康某甲（另案处理）伙同张某甲（未到案）分别在武功县小村镇仁康村东边的凹地、仁康村东南方向的彩钢瓦大棚、小村镇东崆峒村牛场、东崆峒村坟地、小村镇金铁寨村东面的树地等周边偏僻地点开设赌场共计130余场次。赌场分为下午场和夜场，其中下午场开设时间为17时至20时，持续2个小时左右，每场次参赌人员40余人，夜场开设时间为23时至凌晨1时，持续2个小时左右，每场次参赌人员50余人。赌场以押宝摇单双为赌博方式，收取官费和点子费进行获利。\n" +
                "\n" +
                "康某甲纠集康某乙（另案处理）、高某某（另案处理）、黄某某（另案处理）、薛某某（另案处理）等多人为赌场工作人员，安排康某乙、余某某（另案处理）、张某乙（另案处理）、康某丙（另案处理）、杨某某（另案处理）等人驾驶车辆接送参赌人员；黄某某为赌场运送所需物品并布置赌场，兼职放哨；安排高某某、薛某某、张某丙（另案处理）、李某某（另案处理）、雷志朋、范某甲（另案处理）、方某某（另案处理）、康某丁（另案处理）在赌场周边放哨；董某某（另案处理）、成某某（另案处理）、刘某某（另案处理）、谷某某（另案处理）、袁某某（另案处理）、康某戊（另案处理）在赌场赔注；王某某（另案处理）、田某某（另案处理）、计某某（另案处理）、李某某（另案处理）在赌场内放账；范某乙（另案处理）提供牛场为赌博场地。\n" +
                "\n" +
                "被告人雷志朋于2022年8月初至9月12日期间在赌场从事放哨工作，参与赌场工作60场次，获利1万元。\n" +
                "\n" +
                "认定上述事实的证据如下：\n" +
                "\n" +
                "书证；证人证言；被告人的供述与辩解。\n" +
                "\n" +
                "上述证据收集程序合法，内容客观真实，足以认定指控事实。被告人雷志朋对指控的犯罪事实和证据没有异议，并自愿认罪认罚。\n" +
                "\n" +
                "本院认为，被告人雷志朋明知他人开设赌场，仍在赌场多次从事放哨工作并领取报酬，其行为触犯了《中华人民共和国刑法》第三百零三条第二款之规定，犯罪事实清楚，证据确实、充分，应当以开设赌场罪追究其刑事责任。被告人雷志朋在共同犯罪中起次要作用，是从犯，依据《中华人民共和国刑法》第二十七条的规定，应当从轻、减轻处罚或者免除处罚。被告人雷志朋自愿认罪认罚，依据《中华人民共和国刑事诉讼法》第十五条的规定，可以从宽处理。根据《中华人民共和国刑事诉讼法》第一百七十六条的规定，提起公诉，请依法判处。"));
        for (TextSegment textSegment : split) {
            System.out.println(textSegment);
            Response<Embedding> embed = model.embed(textSegment);
            System.out.println(embed);
        }
    }
    @Test
    void factoryTest(){
        EmbeddingModel model = modelFactory.createEmbeddingModel(NameEnums.BGE_SMALL_ZH.toString(), "123");
        Response<Embedding> embed = model.embed("test");
        System.out.println(embed);
    }

    @Test
    void embeddingStoreFactoryTest(){
        EmbeddingModel model = modelFactory.createEmbeddingModel(NameEnums.BGE_SMALL_ZH.toString(), "123");
        EmbeddingStore store = modelFactory.createEmbeddingStore("test", NameEnums.BGE_SMALL_ZH.toString(), NameEnums.ELASTIC_SEARCH_STORE.toString());
        store.add( model.embed("hello").content());
    }
    @Test
    void chatModelFactoryTest(){
        ChatLanguageModel languageModel = modelFactory.createChatLanguageModel(NameEnums.GLM3.toString());
        String response = languageModel.generate("hello");
        System.out.println(response);
    }

}
