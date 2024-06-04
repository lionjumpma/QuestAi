/**
 * @Author: mayixiang
 * @Date: 2024-05-29
 *
 */

package com.sprint.questai.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ESConfig {
    @Bean
    public ElasticsearchClient elasticsearchClient() {
        RestClient client = RestClient.builder(
                new HttpHost("localhost", 9200, "http"),
                new HttpHost("localhost", 9201, "http"))
                .setHttpClientConfigCallback(httpClientBuilder ->
                        httpClientBuilder.addInterceptorLast((HttpRequestInterceptor) (request, context) -> {
                            System.out.println("Request: " + request.getRequestLine());
                        }))
                .build();

        ElasticsearchTransport transport = new RestClientTransport(client,new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }

}
