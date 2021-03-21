package com.spring.learnspring;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class BackPressureTest {

    @Test
    public void testBackPressureStepVerifier() {
        Flux<Integer> integerFlux = Flux.range(1,10);
        StepVerifier.create(integerFlux.log())
                .expectSubscription()
                .thenRequest(1)
                .expectNext(1)
                .thenRequest(1)
                .expectNext(2)
                .thenCancel()
                .verify();
    }

    @Test
    public void testBackPressure() {
        Flux<Integer> integerFlux = Flux.range(1,10).log();
        integerFlux.subscribe(s -> System.out.println(s),
                error -> System.err.println(error),
                () -> System.out.println("done"),
                subscription -> subscription.request(2));
    }

    @Test
    public void testCustomizedBackPressure() {
        Flux<Integer> integerFlux = Flux.range(1,10).log();
        integerFlux.subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnNext(Integer value) {
                request(1);
                System.out.println(value);
                if (value == 9) {
                    cancel();
                }
            }
        });
    }

    @Test
    public void testCustomizedBackPressure1() {
        Flux<Integer> integerFlux = Flux.range(1,10).log();
        integerFlux.subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                subscription.request(2);
                //subscription.cancel();
            }
        });
    }
}
