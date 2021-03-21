package com.spring.learnspring;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;

public class FilterTest {

    @Test
    public void testFilterFlux() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("adam", "eve", "amar", "tina"))
                .filter(s -> s.startsWith("a"));
        StepVerifier.create(stringFlux.log())
                .expectNext("adam", "amar")
                .verifyComplete();
    }
}
