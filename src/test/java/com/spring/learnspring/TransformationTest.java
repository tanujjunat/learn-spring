package com.spring.learnspring;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static reactor.core.scheduler.Schedulers.parallel;

public class TransformationTest {

    @Test
    public void testTransform() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("anna","eve","pompy", "pummy"))
                .filter(s -> s.startsWith("p"))
                .map(s -> s.toUpperCase());
        StepVerifier.create(stringFlux.log())
                .expectNext("POMPY","PUMMY")
                .verifyComplete();
    }

    @Test
    public void testTransformWithFlatMap() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A","B","C","D","E","F"))
                .flatMap(s -> Flux.fromIterable(callDB(s)));
        StepVerifier.create(stringFlux.log())
                .expectNextCount(12)
                .verifyComplete();
    }

    public List<String> callDB(String s) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList(s, "New");
    }

    @Test
    public void testTransformWithFlatMapParallel() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A","B","C","D","E","F"))
                .window(2)
                .flatMap(s -> s.map(this::callDB).subscribeOn(parallel()))
                .flatMap(s -> Flux.fromIterable(s));
        StepVerifier.create(stringFlux.log())
                .expectNextCount(12)
                .verifyComplete();
    }

    @Test
    public void testTransformWithFlatMapParallelInOrder1() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A","B","C","D","E","F"))
                .window(2)
                //.concatMap(s -> s.map(this::callDB).subscribeOn(parallel()))
                .flatMapSequential(s -> s.map(this::callDB).subscribeOn(parallel()))
                .flatMap(s -> Flux.fromIterable(s));
        StepVerifier.create(stringFlux.log())
                .expectNextCount(12)
                .verifyComplete();
    }

}
