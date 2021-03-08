package com.vino.webclient;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

public class WebClientTest {

    public void get() {
        WebClient client = WebClient.create("http://localhost:8080");
        Mono<String> result = client.get()
                .uri("/get")
                .retrieve()
                .bodyToMono(String.class);
        result.subscribe(System.err::println);
    }

    public void getRequestParam() {
        WebClient client = WebClient.create("http://localhost:8080");

        //定义query参数
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("name", "kl");
        params.add("age", "19");
        //定义url参数
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("id", 200);

        String uri = UriComponentsBuilder.fromUriString("/getParam/{id}")
                .queryParams(params)
                .uriVariables(uriVariables)
                .toUriString();

        Mono<String> result = client.get()
                .uri("/get")
                .retrieve()
                .bodyToMono(String.class);
        result.subscribe(System.err::println);
    }
}
