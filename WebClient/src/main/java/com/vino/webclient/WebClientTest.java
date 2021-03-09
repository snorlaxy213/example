package com.vino.webclient;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component("WebClient")
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

        // Define query parameters
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("name", "kl");
        params.add("age", "19");
        // Define url parameters
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("id", 200);

        String uri = UriComponentsBuilder.fromUriString("/getParam/{id}")
                .queryParams(params)
                .uriVariables(uriVariables)
                .toUriString();

        Mono<String> result = client.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class);
        result.subscribe(System.err::println);
    }

    public void postFile() {
        WebClient client = WebClient.create("http://localhost:8080");

        client.post()
                .uri("/images")
                .retrieve()
                .bodyToMono(Resource.class)
                .subscribe(resource -> {
                    try {
                        File file = new File("D://pikachu.jpg");
                        FileCopyUtils.copy(StreamUtils.copyToByteArray(resource.getInputStream()), file);
                    } catch (IOException ex) {
                    }
                });
    }
}
