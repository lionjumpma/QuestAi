package com.sprint.questai.model.enums;

import cn.hutool.core.lang.hash.Hash;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public enum ChatToolEnums {

    TYPE_STRING("String",new HashMap<String, Object>() {{
        put("type", "string");
    }} );

    private final String text;

    private final HashMap<String,Object> value;

    ChatToolEnums(String text, HashMap<String,Object> value) {
        this.text = text;
        this.value = value;
    }

}
