package com.sprint.questai.model.entity.wenshu;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Category {
    @JsonProperty("cat_1")
    private String cat1;
    @JsonProperty("cat_2")
    private String cat2;

    public String toString() {
        return "Category:"+cat1 + " " + cat2+"\n";
    }
}
