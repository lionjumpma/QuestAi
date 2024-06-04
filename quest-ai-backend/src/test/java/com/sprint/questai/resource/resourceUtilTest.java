/**
 * @Author: chenzehao
 * @Date: 2024-05-29
 *
 */
package com.sprint.questai.resource;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.resource.ResourceUtil;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class resourceUtilTest {
    @Test
    public void testResourceUtil() {
        URL resource = ResourceUtil.getResource("legal_data/wenshu/qisu/qisu1.txt");
System.out.println(resource);
        FileReader fileReader = new FileReader(FileUtil.file(resource));

        List<String> strings = fileReader.readLines();
        // 使用 Stream API 过滤掉空行
        List<String> filteredLines = strings.stream()
                .filter(line -> !line.trim().isEmpty()) // 过滤掉空白行
                .collect(Collectors.toList());
        FileUtil.writeLines(filteredLines, FileUtil.file(resource), "UTF-8");
        String result = fileReader.readString();
        System.out.println(result);

    }
}
