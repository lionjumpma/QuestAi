package com.sprint.questai.model.entity.fakao;

import lombok.Data;

/**
 * @Author: mayixiang
 * @Date: 2024/5/30 18:28
 * @Description: 法考信息数据结构-gpt4生成
 */
@Data
public class FakaoInfo {
    String input;
    String output;
    String type;
    public String toString(){
        return "input: "+input+" output: "+output+" type: "+type;
    }
}
