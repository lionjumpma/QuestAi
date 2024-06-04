/**
 * @Author: chenzehao
 * @Date: 2024-05-02
 *
 */
package com.sprint.questai.store;

import cn.hutool.core.io.resource.ResourceUtil;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;

import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import com.sprint.questai.factory.ModelFactory;
import com.sprint.questai.model.entity.fakao.FakaoInfo;
import com.sprint.questai.model.entity.wenshu.Case;
import com.sprint.questai.model.enums.NameEnums;
import com.sprint.questai.module.store.Impl.ESEmbeddingService;
import com.sprint.questai.util.FakaoUtil;
import com.sprint.questai.util.WenShuUtil;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentBySentenceSplitter;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.BgeSmallZhQuantizedEmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootTest
public class EsTest {
    @Resource
    private FakaoUtil fakaoUtil;
    @Resource
    private WenShuUtil wenShuUtil;
    @Resource
    private ElasticsearchClient client;
    @Resource
    private ESEmbeddingService embeddingService;
    @Resource
    private ModelFactory factory;
    //通用的embeddingModel
    
    @Test
    public void addCaseTest() throws IOException {
        Case aCase = new Case();
        aCase.setCaseId("123");
        IndexResponse cases = client.index(i -> i.index("cases").id(aCase.getCaseId()).document(aCase));
        System.out.println(cases);
    }

    @Test
    public void saveCasesToEsTest() {
        wenShuUtil.saveCasesToEs(wenShuUtil.parseCases("14501"));
    }

    @Test
    public void getFileNamesFromResource() {
        URL resource = ResourceUtil.getResource("legal_data/wenshu/dev");
        //从文件夹下获取所有文件名
        System.out.println(wenShuUtil.getWenShuFileNameByPath(resource.getPath()));
    }

    @Test
    public void addCaseEmbeddingTest() throws IOException {
        URL resource = ResourceUtil.getResource("legal_data/wenshu/dev");

        List<String> names = wenShuUtil.getWenShuFileNameByPath(resource.getPath());
        names = names.subList(1, 20);
        for (String s : names) {
            List<Case> cases = wenShuUtil.parseCases(s);
            embeddingService.saveCasesEmbedding(cases);
        }
//        //获取resource目录下legal_data/wenshu/dev下的所有case
//        List<Case> cases = wenShuUtil.parseCases("14501.json");
//        //获取10个case
//        //cases = cases.subList(0, 10);
//        embeddingService.saveCasesEmbedding(cases);
    }

    @Test
    public void addFakaoEmbeddingTest() {
        //获取resource目录下legal_data/fakao/fakao_gpt4.json下的所有法考信息
        List<FakaoInfo> fakaoInfos = fakaoUtil.parseFakaoInfo();
        //获取10个法考信息
        embeddingService.saveFakaoEmbedding(fakaoInfos);
    }
    @Test
    public void getCategory() {
        List<String> names = wenShuUtil.getWenShuFileNameByPath("D:\\Study\\Spring3\\chuangxin\\QuestAi\\quest-ai-backend\\src\\main\\resources\\legal_data\\wenshu\\dev");
        //names = names.subList(1, 20);
        Set<String> categories = new HashSet<>();
        for (String s : names) {
            List<Case> cases = wenShuUtil.parseCases(s);
            for (Case aCase : cases) {
                categories.add(aCase.getCategory().getCat1());
                categories.add(aCase.getCategory().getCat2());
                //System.out.println(aCase.getCategory());
            }
        }
        System.out.println(categories);

        System.out.println(categories.size());
    }

    @Test
    public void partialSaveCasesTest() throws IOException {
        URL resource = ResourceUtil.getResource("legal_data/wenshu/dev");
        String path = resource.getPath();
        List<String> names = wenShuUtil.getWenShuFileNameByPath(path);
        //names = names.subList(1, 20);
        for (String s : names) {
            List<Case> cases = wenShuUtil.parseCases(s);
            embeddingService.partialSaveCasesEmbedding(
                    cases,
                    NameEnums.JUDGE_ACCUSATION_NAME,
                    NameEnums.DEFAULT_EMBEDDING_MODEL,
                    NameEnums.ELASTIC_SEARCH_STORE);
        }
    }

    @Test
    public void searchCaseEmbeddingTest() {
        //case_record test
        Response<Embedding> response = embeddingService.embed("我设计的产品被其他人侵权使用");
        String indexName = NameEnums.generateEmbeddingIndexName(NameEnums.CASE_RECORD_NAME.toString(), NameEnums.DEFAULT_EMBEDDING_MODEL.toString());
        EmbeddingStore<TextSegment> store = factory.createEmbeddingStore(NameEnums.CASE_RECORD_NAME.toString(), NameEnums.DEFAULT_EMBEDDING_MODEL.toString(), NameEnums.ELASTIC_SEARCH_STORE.toString());
        List<EmbeddingMatch<TextSegment>> relevant = store.findRelevant(response.content(), 8, 0);

        for (EmbeddingMatch<TextSegment> emb : relevant) {
            System.out.println(emb);
        }
//        store = embeddingService.buildEmbeddingStore("judge_accusation");
//        relevant = store.findRelevant(response.content(), 5, 0);
//        for (EmbeddingMatch<TextSegment> emb : relevant) {
//            System.out.println(emb);
//        }
//
//        store = embeddingService.buildEmbeddingStore("judge_reason");
//        relevant = store.findRelevant(response.content(), 5, 0);
//        for (EmbeddingMatch<TextSegment> emb : relevant) {
//            System.out.println(emb);
//        }

    }
    @Test
    public void embeddingEnhenceTest(){
        //测试embedding增强
        String text = "我设计的产品被其他人侵权使用";
        ChatLanguageModel model = factory.createChatLanguageModel(NameEnums.Chat_GLM_3_NAME.toString());
    }

    @Test
    public void searchCaseByMetaTest() {
        //case_record test
        Response<Embedding> response = embeddingService.embed("权益纠纷");
        EmbeddingStore<TextSegment> store = embeddingService.buildEmbeddingStore("case_record");
        List<EmbeddingMatch<TextSegment>> relevant = store.findRelevant(response.content(), 5, 0);

        for (EmbeddingMatch<TextSegment> emb : relevant) {
            System.out.println(emb);
        }

//        store = embeddingService.buildEmbeddingStore("judge_accusation");
//        relevant = store.findRelevant(response.content(), 5, 0);
//        for (EmbeddingMatch<TextSegment> emb : relevant) {
//            System.out.println(emb);
//        }
//
//        store = embeddingService.buildEmbeddingStore("judge_reason");
//        relevant = store.findRelevant(response.content(), 5, 0);
//        for (EmbeddingMatch<TextSegment> emb : relevant) {
//            System.out.println(emb);
//        }

    }

    @Test
    public void searchCaseByCaseIdTest() throws IOException {
        embeddingService.searchCaseByCaseId("9214edcf86e34265bba6adf200e5a827", "case_record");
        embeddingService.searchCaseByCaseId("9214edcf86e34265bba6adf200e5a827", "judge_accusation");
    }

    @Test
    public void getFileNamesTest() {
        System.out.println(wenShuUtil.getWenShuFileNameByPath("D:\\Study\\Spring3\\chuangxin\\QuestAi\\quest-ai-backend\\src\\main\\resources\\legal_data\\wenshu\\dev"));
    }

    @Test
    public void searchEmbeddingTest() {
        embeddingService.searchCaseByText(
                NameEnums.JUDGE_ACCUSATION_NAME.toString()
                , NameEnums.BGE_SMALL_ZH.toString(),
                "我未经博主小a允许私自使用了他花的画进行牟利",
                5);
    }

    @Test
    public void dashTest() {
        EmbeddingModel embeddingModel = new BgeSmallZhQuantizedEmbeddingModel();
        String api_key = System.getenv("DASHSCOPE_API_KEY");
        //System.out.println(api_key);
        DocumentSplitter spiltter = new DocumentBySentenceSplitter(150, 15);
        //DocumentSplitter spiltter= DocumentSplitters.recursive(300, 15, tokenizer);
        Document document = Document.from("原告陈某向本院提出诉讼请求：1、要求二被告返还彩礼60000元；2诉讼费由被告承担。事实和理由：原、被告经人介绍认识，2018年农历正月初六按农村风俗订婚，订婚时原告给被告礼金46054元。同年农历正月十三抄年命时原告家人又给被告彩礼金5620元。二人订婚后，原告就外出找工，被告在家打工，被告经常以买衣服、给其儿子买玩具、母亲过生日为由向原告索要财物共计6420元。××××年××月××日，被告提出分手，称不愿与原告结婚。2018年8月21日，被告再次向原告提出分手，此后双方就彩礼事宜协商，但被告拒不返还。现诉至法院。\n" +
                "被告毛某辩称，原告所述不属实，被告从来没有向原告索要过钱，原告诉称原、被告之间有矛盾是交往中正常存在的现象。诉称中关于订婚彩礼40000元属实，其他有关给付的钱系原告自愿给被告或家人的，被告一直都同意和原告结婚，是因为结婚前被告不送彩礼才没能结成婚，所以不愿意退还彩礼。\n" +
                "被告刘某未向本院提交答辩状亦未进行口头答辩。\n" +
                "本院经审理认定事实如下：原告陈某与被告毛某经人介绍认识，2018年农历正月初六双方按农村习俗举行了订婚仪式，原告方送给被告毛某彩礼礼金40000元，该款由被告毛某存放。后原、被告因琐事发生矛盾双方确定分手，因彩礼返还问题协商未果，诉至法院");
        List<TextSegment> split = spiltter.split(document);
        for (TextSegment textSegment : split) {
            embeddingModel.embed(textSegment.text());
            System.out.println(textSegment);
        }

    }

    @Test
    void bgeEmbedTest() {
        EmbeddingModel embeddingModel = new BgeSmallZhQuantizedEmbeddingModel();
        Response<Embedding> response = embeddingModel.embed("原告陈某向本院提出诉讼请求：1、要求二被告返还彩礼60000元；2诉讼费由被告承担。事实和理由：原、被告经人介绍认识，2018年农历正月初六按农村风俗订婚，订婚时原告给被告礼金46054元。同年农历正月十三抄年命时原告家人又给被告彩礼金5620元。二人订婚后，原告就外出找工，被告在家打工，被告经常以买衣服、给其儿子买玩具、母亲过生日为由向原告索要财物共计6420元。××××年××月××日，被告提出分手，称不愿与原告结婚。2018年8月21日，被告再次向原告提出分手，此后双方就彩礼事宜协商，但被告拒不返还。现诉至法院。\n" +
                "被告毛某辩称，原告所述不属实，被告从来没有向原告索要过钱，原告诉称原、被告之间有矛盾是交往中正常存在的现象。诉称中关于订婚彩礼40000元属实，其他有关给付的钱系原告自愿给被告或家人的，被告一直都同意和原告结婚，是因为结婚前被告不送彩礼才没能结成婚，所以不愿意退还彩礼。\n" +
                "被告刘某未向本院提交答辩状亦未进行口头答辩。\n" +
                "本院经审理认定事实如下：原告陈某与被告毛某经人介绍认识，2018年农历正月初六双方按农村习俗举行了订婚仪式，原告方送给被告毛某彩礼礼金40000元，该款由被告毛某存放。后原、被告因琐事发生矛盾双方确定分手，因彩礼返还问题协商未果，诉至法院");
        System.out.println(response.content().dimension());
    }

    @Test
    void ingestorTest() {
        EmbeddingStoreIngestor ingestor =factory.createEmbeddingStoreIngestor(
                NameEnums.JUDGE_ACCUSATION_NAME.toString(),
                NameEnums.DEFAULT_EMBEDDING_MODEL.toString(),
                NameEnums.ELASTIC_SEARCH_STORE.toString(),
                NameEnums.DEFAULT_DOCUMENT_SPLITTER.toString()
        );
        Document document = Document.from("原告陈某向本院提出诉讼请求：1、要求二被告返还彩礼60000元；2诉讼费由被告承担。事实和理由：原、被告经人介绍认识，2018年农历正月初六按农村风俗订婚，订婚时原告给被告礼金46054元。同年农历正月十三抄年命时原告家人又给被告彩礼金5620元。二人订婚后，原告就外出找工，被告在家打工，被告经常以买衣服、给其儿子买玩具、母亲过生日为由向原告索要财物共计6420元。××××年××月××日，被告提出分手，称不愿与原告结婚。2018年8月21日，被告再次向原告提出分手，此后双方就彩礼事宜协商，但被告拒不返还。现诉至法院。\n" +
                "被告毛某辩称，原告所述不属实，被告从来没有向原告索要过钱，原告诉称原、被告之间有矛盾是交往中正常存在的现象。诉称中关于订婚彩礼40000元属实，其他有关给付的钱系原告自愿给被告或家人的，被告一直都同意和原告结婚，是因为结婚前被告不送彩礼才没能结成婚，所以不愿意退还彩礼。\n" +
                "被告刘某未向本院提交答辩状亦未进行口头答辩。\n" +
                "本院经审理认定事实如下：原告陈某与被告毛某经人介绍认识，2018年农历正月初六双方按农村习俗举行了订婚仪式，原告方送给被告毛某彩礼礼金40000元，该款由被告毛某存放。后原、被告因琐事发生矛盾双方确定分手，因彩礼返还问题协商未果，诉至法院");
        ingestor.ingest(document);
    }

    @Test
    void clearIndex() throws IOException {
        //delete index
        client.indices().delete(DeleteIndexRequest.of(d -> d.index(NameEnums.JUDGE_ACCUSATION_BGE_INDEX.getText())));
    }

    public static void main(String[] args) {

    }
}