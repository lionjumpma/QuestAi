package com.sprint.questai.model.entity.wenshu;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 *  *                 {
 *  *                     "NameText": "陈某",
 *  *                     "Name": "陈某",
 *  *                     "LegalEntity": "Person",
 *  *                     "Prop": "原告"
 *  *                 },
 */
@Data
public class Party {
    @JsonProperty("NameText")
    private String NameText;
    @JsonProperty("Name")
    private String Name;
    @JsonProperty("LegalEntity")
    private String LegalEntity;
    @JsonProperty("Prop")
    private String Prop;
    public String toString(){
        return "NameText: " + NameText + ", Name: " + Name + ", LegalEntity: " + LegalEntity + ", Prop: " + Prop;
    }
}
