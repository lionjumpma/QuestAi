package com.sprint.questai.model.extraModel.inference;

import com.sprint.questai.xinference.XClient;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.scoring.ScoringModel;
import lombok.Builder;

import java.util.List;

public class XScoreModel implements ScoringModel{
    private final XClient client;
    @Builder
    public XScoreModel(String baseUrl, String modelUid){
        this.client = new XClient(baseUrl, modelUid);
    }
    @Override
    public Response<Double> score(String text, String query) {
        return ScoringModel.super.score(text, query);
    }

    @Override
    public Response<Double> score(TextSegment segment, String query) {
        return ScoringModel.super.score(segment, query);
    }

    @Override
    public Response<List<Double>> scoreAll(List<TextSegment> list, String s) {


        return null;
    }
}
