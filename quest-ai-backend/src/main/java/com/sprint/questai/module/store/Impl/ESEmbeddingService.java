package com.sprint.questai.module.store.Impl;

import cn.hutool.core.util.ObjectUtil;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.sprint.questai.factory.ModelFactory;
import com.sprint.questai.model.entity.fakao.FakaoInfo;
import com.sprint.questai.model.entity.wenshu.Case;
import com.sprint.questai.model.enums.EmbeddingModelEnums;

import com.sprint.questai.model.enums.NameEnums;
import com.sprint.questai.model.store.CaseRecordEmbed;
import com.sprint.questai.util.FakaoUtil;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.document.splitter.DocumentByCharacterSplitter;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.Tokenizer;
import dev.langchain4j.model.dashscope.QwenModelName;
import dev.langchain4j.model.dashscope.QwenTokenizer;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.spi.model.embedding.EmbeddingModelFactory;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.elasticsearch.ElasticsearchEmbeddingStore;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
@Service
@Data
public class ESEmbeddingService {
    @Autowired
    ModelFactory factory;
    @Resource
    private ElasticsearchClient client;
    public EmbeddingModel embeddingModel;

    String api_key=System.getenv("DASHSCOPE_API_KEY");
    Tokenizer tokenizer = new QwenTokenizer(api_key, QwenModelName.QWEN_PLUS);

    DocumentSplitter spiltter = new DocumentByCharacterSplitter(200,15);
    EmbeddingStoreIngestor caseRecordIngestor ;
    EmbeddingStoreIngestor judgeAccusationIngestor;
    EmbeddingStoreIngestor judgeReasonIngestor ;
    EmbeddingStoreIngestor judgeResultIngestor;
    EmbeddingStoreIngestor fakaoIngestor;
    public EmbeddingStore<TextSegment> buildEmbeddingStore(String indexName){
        return ElasticsearchEmbeddingStore.builder()
                .serverUrl("http://localhost:9200")
                .dimension(1024)
                .indexName(indexName)
                .build();
    }
    public EmbeddingStoreIngestor getEmbeddingStoreIngestor(String field){
        return factory.createEmbeddingStoreIngestor(
                field,
                NameEnums.BGE_SMALL_ZH.toString(),
                NameEnums.ELASTIC_SEARCH_STORE.toString(),
                NameEnums.DEFAULT_DOCUMENT_SPLITTER.toString()
        );
    }
    @PostConstruct
    public void init(){
        embeddingModel = factory.createEmbeddingModel(
                EmbeddingModelEnums.BGE_SMALL_ZH.getText(),
                api_key
        );

    }
    public EmbeddingStoreIngestor getCaserecordIngestor(){
        if(ObjectUtil.isNotEmpty(caseRecordIngestor)){
            return caseRecordIngestor;
        }
        return getEmbeddingStoreIngestor(NameEnums.CASE_RECORD_BGE_INDEX.getText());
    }
    public EmbeddingStoreIngestor getJudgeAccusationIngestor(){
        if(ObjectUtil.isNotEmpty(judgeAccusationIngestor)){
            return judgeAccusationIngestor;
        }
        return getEmbeddingStoreIngestor(NameEnums.JUDGE_ACCUSATION_BGE_INDEX.getText());
    }
    public EmbeddingStoreIngestor getJudgeReasonIngestor(){
        if(ObjectUtil.isNotEmpty(judgeReasonIngestor)){
            return judgeReasonIngestor;
        }
        return getEmbeddingStoreIngestor(NameEnums.JUDGE_REASON_BGE_INDEX.getText());
    }
    public EmbeddingStoreIngestor getJudgeResultIngestor(){
        if(ObjectUtil.isNotEmpty(judgeResultIngestor)){
            return judgeResultIngestor;
        }
        return getEmbeddingStoreIngestor(NameEnums.JUDGE_RESULT_BGE_INDEX.getText());
    }
    public EmbeddingStoreIngestor getFakaoIngestor(){
        if(ObjectUtil.isNotEmpty(fakaoIngestor)){
            return fakaoIngestor;
        }
        return getEmbeddingStoreIngestor(NameEnums.FAKAO_NAME.getText());
    }
    public void partialSaveCasesEmbedding(List<Case> cases,NameEnums fieldName,NameEnums eModel,NameEnums eStore,NameEnums splitter) throws IOException {
        String indexName=NameEnums.generateEmbeddingIndexName(fieldName.toString(),eModel.toString());
        EmbeddingStoreIngestor ingestor = factory.createEmbeddingStoreIngestor(
                fieldName.toString(),
                eModel.toString(),
                eStore.toString(),
               splitter.toString());
        for (Case acase : cases){
            String caseId = acase.getCaseId();
            Metadata metadata = generateMeta(acase);
            String text = acase.getPropertyByNameEnums(fieldName);
            if(ObjectUtil.isEmpty(text)){
                continue;
            }
            Document document = Document.from(text, metadata);
            if (searchCaseByCaseId(caseId, indexName).hits().hits().isEmpty()) {
                ingestor.ingest(document);
                System.out.println("caseId:" + caseId + indexName +"save success");
            } else {
                System.out.println("caseId:" + caseId + indexName+"already exists");
            }
        }
    }

    public void partialSaveCasesEmbedding(List<Case> cases,NameEnums fieldName,NameEnums eModel,NameEnums eStore) throws IOException {
        partialSaveCasesEmbedding(cases,fieldName,eModel,eStore,NameEnums.DEFAULT_DOCUMENT_SPLITTER);
    }

    public void saveCasesEmbedding(List<Case> cases) throws IOException {
        for (Case acase : cases){
            String caseId = acase.getCaseId();

            Metadata metadata = generateMeta(acase);
            //TextSegment textSegment = new TextSegment(acase.getCaseRecord(), metadata);

            Document document ;

            if(ObjectUtil.isNotEmpty(acase.getCaseRecord()))
            {
                document = Document.from(acase.getCaseRecord(), metadata);
                if (searchCaseByCaseId(caseId, NameEnums.CASE_RECORD_BGE_INDEX.getText()).hits().hits().isEmpty()) {
                    getCaserecordIngestor().ingest(document);
                    System.out.println("caseId:" + caseId + " case_record save success");
                } else {
                    System.out.println("caseId:" + caseId + " case_record already exists");
                }
            }

            if (ObjectUtil.isNotEmpty(acase.getJudgeAccusation()))
            {
                document = Document.from(acase.getJudgeAccusation(), metadata);
                if (searchCaseByCaseId(caseId, NameEnums.JUDGE_ACCUSATION_BGE_INDEX.getText()).hits().hits().isEmpty()) {
                    getJudgeAccusationIngestor().ingest(document);
                    System.out.println("caseId:" + caseId + " judge_accusation save success");
                } else {
                    System.out.println("caseId:" + caseId + " judge_accusation already exists");
                }
            }

            if (ObjectUtil.isNotEmpty(acase.getJudgeReason()))
            {
                document = Document.from(acase.getJudgeReason(), metadata);
                if (searchCaseByCaseId(caseId, NameEnums.JUDGE_REASON_BGE_INDEX.getText()).hits().hits().isEmpty()) {
                    getJudgeReasonIngestor().ingest(document);
                    System.out.println("caseId:" + caseId + " judge_reason save success");
                } else {
                    System.out.println("caseId:" + caseId + " judge_reason already exists");
                }
            }
            if(ObjectUtil.isNotEmpty(acase.getJudgeResult()))
            {
                document = Document.from(acase.getJudgeResult(), metadata);

                if (searchCaseByCaseId(caseId, NameEnums.JUDGE_RESULT_BGE_INDEX.getText()).hits().hits().isEmpty()) {
                    getJudgeResultIngestor().ingest(document);
                    System.out.println("caseId:" + caseId + " judge_result save success");
                } else {
                    System.out.println("caseId:" + caseId + " judge_result already exists");
                }
            }
            System.out.println("save case embedding success");
        }

    }
    //生成meta
    public Metadata generateMeta(Case acase){
        Metadata metadata = new Metadata();
        metadata.put("caseId", acase.getCaseId());
        metadata.put("category1", acase.getCategory().getCat1());
        metadata.put("category2", acase.getCategory().getCat2());
        metadata.put("keywords", acase.getKeywords().toString());
        return metadata;
    }
    public Response<Embedding> embed(String text){
        return embeddingModel.embed(text);
        //System.out.println(embedding);
    }
    public Response<List<Embedding>> embedAll(List<TextSegment> textSegments){
        return embeddingModel.embedAll(textSegments);
    }
//判断是否已经存在该case

    public SearchResponse<CaseRecordEmbed> searchCaseByCaseId(String caseId,String indexName) throws IOException {

        // 创建一个 TermQuery 来匹配 caseId 字段
        TermQuery termQuery = TermQuery.of(t -> t.field("metadata.caseId").value(caseId));

        // 使用 ElasticsearchClient 的 search 方法来执行搜索请求
        return client.search(s -> s
                        .index(indexName)  // 指定索引名
                        .query(q -> q.term(termQuery)),  // 设置查询条件
                CaseRecordEmbed.class);  // 指定返回的文档类型
       // System.out.println(response);
        // 打印搜索结果
//        response.hits().hits().forEach(hit -> {
//            CaseRecordEmbed caseRecord = hit.source();
//            System.out.println(caseRecord);
//        });
    }

    public void searchCaseByText(String fieldName,String emodel,String text,int maxResult){
        EmbeddingStore store= factory.createEmbeddingStore(fieldName,emodel,NameEnums.ELASTIC_SEARCH_STORE.toString());
        Embedding embedding = embeddingModel.embed(text).content();
        List<EmbeddingMatch<TextSegment>> matches = store.findRelevant(embedding, maxResult, 0);
        for (EmbeddingMatch<TextSegment> match : matches) {
            System.out.println(match.embedded()+"\n"+match.score());
        }
    }

    public void saveFakaoEmbedding(List<FakaoInfo> list){
        for (FakaoInfo fakaoInfo : list){
            String input = fakaoInfo.getInput();
            String output = fakaoInfo.getOutput();
            String type = fakaoInfo.getType();
            Metadata metadata = new Metadata();
            metadata.put("type",type);
            metadata.put("output",output);
            Document document = Document.from(input,metadata);
            getFakaoIngestor().ingest(document);
        }
    }
    public void searchFakaoByText(String text,int maxResult){

    }
}
