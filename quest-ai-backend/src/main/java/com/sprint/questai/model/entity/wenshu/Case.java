package com.sprint.questai.model.entity.wenshu;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.questai.model.enums.NameEnums;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;

/**
 * "0": {
 *             "Case": "陈某与毛某、刘某婚约财产纠纷一审民事判决书",
 *             "CaseId": "0e068c4223e249269c39aa2900b6470c",
 *             "CaseProc": "民事一审",
 *             "CaseRecord": "原告陈某诉被告毛某、刘某婚约财产纠纷一案，本院于2018年10月23日立案后，依法适用简易程序，公开开庭进行了审理。原告陈某及其委托代理人杨利、崔克伟、李静雅，被告毛某及其委托代理人王雪华到庭参加诉讼。被告刘某经本院合法传唤无正当理由拒不到庭。本案现已审理终结",
 *             "CaseType": "民事案件",
 *             "JudgeAccusation": "原告陈某向本院提出诉讼请求：1、要求二被告返还彩礼60000元；2诉讼费由被告承担。事实和理由：原、被告经人介绍认识，2018年农历正月初六按农村风俗订婚，订婚时原告给被告礼金46054元。同年农历正月十三抄年命时原告家人又给被告彩礼金5620元。二人订婚后，原告就外出找工，被告在家打工，被告经常以买衣服、给其儿子买玩具、母亲过生日为由向原告索要财物共计6420元。××××年××月××日，被告提出分手，称不愿与原告结婚。2018年8月21日，被告再次向原告提出分手，此后双方就彩礼事宜协商，但被告拒不返还。现诉至法院。\n被告毛某辩称，原告所述不属实，被告从来没有向原告索要过钱，原告诉称原、被告之间有矛盾是交往中正常存在的现象。诉称中关于订婚彩礼40000元属实，其他有关给付的钱系原告自愿给被告或家人的，被告一直都同意和原告结婚，是因为结婚前被告不送彩礼才没能结成婚，所以不愿意退还彩礼。\n被告刘某未向本院提交答辩状亦未进行口头答辩。\n本院经审理认定事实如下：原告陈某与被告毛某经人介绍认识，2018年农历正月初六双方按农村习俗举行了订婚仪式，原告方送给被告毛某彩礼礼金40000元，该款由被告毛某存放。后原、被告因琐事发生矛盾双方确定分手，因彩礼返还问题协商未果，诉至法院",
 *             "JudgeReason": "本院认为：双方未办理结婚登记手续的，当事人请求返还按照习俗给付的彩礼的，人民法院应当予以支持。本案原、被告虽按农村习俗举行了订婚仪式，但未办理结婚登记，被告按习俗接收了原告的彩礼40000元，原告要求被告退还，被告应予适当退还，故对原告的诉讼请求，本院予以部分支持，彩礼返还酌定30000元为宜。原告的其他诉讼请求未向本院提供相应证据予以证明，本院不予支持。被告辩称不应返还彩礼与法无据，本院不予采信。依据最高人民法院关于适用《中华人民共和国婚姻法》若干问题的解释（二）第十条、《中华人民共和国民事诉讼法》第六十四条、第一百四十四条之规定，判决如下",
 *             "JudgeResult": "一、被告毛某于判决书生效后五日内返还原告陈某彩礼款30000元；\n二、驳回原告陈某的其它诉讼请求。\n案件受理费650元，由原告陈某负担350元，被告毛某负担300元。\n如果未按判决指定的期间履行给付金钱义务，应当按照《中华人民共和国民事诉讼法》第二百五十三条的规定，加倍支付迟延履行期间的债务利息。\n如不服本判决，可在判决书送达之日起十五日内向本院递交上诉状，并按对方当事人的人数提出副本，上诉于河南省许昌市中级人民法院",
 *             "Keywords": [
 *                 "给付",
 *                 "彩礼",
 *                 "返还"
 *             ],
 *             "Parties": [
 *                 {
 *                     "NameText": "陈某",
 *                     "Name": "陈某",
 *                     "LegalEntity": "Person",
 *                     "Prop": "原告"
 *                 },
 *                 {
 *                     "NameText": "毛某",
 *                     "Name": "毛某",
 *                     "LegalEntity": "Person",
 *                     "Prop": "被告"
 *                 },
 *                 {
 *                     "NameText": "刘某",
 *                     "Name": "刘某",
 *                     "LegalEntity": "Person",
 *                     "Prop": "被告"
 *                 }
 *             ],
 *             "Category": {
 *                 "cat_1": "婚姻家庭",
 *                 "cat_2": "财产分割"
 *             }
 *         },
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Case {
    @JsonProperty("Case")
    private String caseName;

    @JsonProperty("CaseId")
    private String caseId;

    @JsonProperty("CaseProc")
    private String caseProc;

    @JsonProperty("CaseRecord")
    private String caseRecord;

    @JsonProperty("CaseType")
    private String caseType;

    @JsonProperty("JudgeAccusation")
    private String judgeAccusation;

    @JsonProperty("JudgeReason")
    private String judgeReason;

    @JsonProperty("JudgeResult")
    private String judgeResult;

    @JsonProperty("Keywords")
    private List<String> keywords;

    @JsonProperty("Parties")
    private List<Party> parties;

    @JsonProperty("Category")
    private Category category;
    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // 使用ObjectMapper将当前对象转换成格式化的JSON字符串
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "Error converting to JSON: " + e.getMessage();
        }
    }
    public HashMap<String,String> generateHashMap(){
        HashMap<String,String> map = new HashMap<>();
        map.put("caseName",this.caseName);
        map.put("caseId",this.caseId);
        map.put("caseProc",this.caseProc);
        map.put("caseRecord",this.caseRecord);
        map.put("caseType",this.caseType);
        map.put("judgeAccusation",this.judgeAccusation);
        map.put("judgeReason",this.judgeReason);
        map.put("judgeResult",this.judgeResult);
        map.put("keywords",this.keywords.toString());
        map.put("parties",this.parties.toString());
        map.put("category",this.category.toString());
        return map;
    }
    public String getPropertyByNameEnums(NameEnums name){
        switch (name){
            case JUDGE_REASON_NAME:
                return this.judgeReason;
            case JUDGE_RESULT_NAME:
                return this.judgeResult;
            case JUDGE_ACCUSATION_NAME:
                return this.judgeAccusation;
            case CASE_RECORD_NAME:
                return this.caseRecord;
            default:
                return null;
        }
    }
}
