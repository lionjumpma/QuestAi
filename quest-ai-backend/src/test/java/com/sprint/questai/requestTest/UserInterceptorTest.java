/**
 * @Author: chenzehao
 * @Date: 2024-05-29
 *
 */
package com.sprint.questai.requestTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;

@SpringBootTest
@AutoConfigureMockMvc
public class UserInterceptorTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testUserIdInSession() throws Exception {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("model", Arrays.asList("glm3"));
        map.put("content", Arrays.asList("你好"));
        mockMvc.perform(MockMvcRequestBuilders.get("/chat/nokey").params(
                        map
                ))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(request -> {
                    assert request.getRequest().getSession().getAttribute("userId").equals("12345");
                });
    }
}

