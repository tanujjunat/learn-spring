package com.spring.learnspring;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class SampleTest {
	
	@Test
	public void testFlux() {
		
		Flux<String> stringFlux = Flux.just("Spring", "Spring Reactive", "Spring Webflux");
		StepVerifier.create(stringFlux.log())
				.expectNext("Spring", "Spring Reactive", "Spring Webflux")
				.verifyComplete();
	}

	@Test
	public void testFluxFromIterable() {

		List<String> listData = Arrays.asList("Spring", "Spring Reactive", "Spring Webflux");
		Flux<String> stringFlux = Flux.fromIterable(listData)
				.log();
		StepVerifier.create(stringFlux)
				.expectNext("Spring", "Spring Reactive", "Spring Webflux")
				.verifyComplete();
	}

	@Test
	public void testFluxFromArray() {

		String [] stringArray = new String[] {"Spring", "Spring Reactive", "Spring Webflux"};
		Flux<String> stringFlux = Flux.fromArray(stringArray)
				.log();
		StepVerifier.create(stringFlux)
				.expectNext("Spring", "Spring Reactive", "Spring Webflux")
				.verifyComplete();
	}

	@Test
	public void testFluxFromStream() {

		String [] stringArray = new String[] {"Spring", "Spring Reactive", "Spring Webflux"};
		Flux<String> stringFlux = Flux.fromStream(Arrays.stream(stringArray))
				.log();
		StepVerifier.create(stringFlux)
				.expectNext("Spring", "Spring Reactive", "Spring Webflux")
				.verifyComplete();
	}

	@Test
	public void testMono() {

		Mono<String> stringMono = Mono.justOrEmpty(null);
		StepVerifier.create(stringMono.log())
				.verifyComplete();
	}

	@Test
	public void testMonoFromSupplier() {
		Supplier<String> stringSupplier = () -> "hello";
		Mono<String> stringMono = Mono.fromSupplier(stringSupplier);
		StepVerifier.create(stringMono.log())
				.expectNext("hello")
				.verifyComplete();
	}

	@Test
	public void testFluxFromRange() {
		Flux<Integer> intergerFlux = Flux.range(1,5);
		StepVerifier.create(intergerFlux.log())
				.expectNext(1,2,3,4,5)
				.verifyComplete();
	}

}
