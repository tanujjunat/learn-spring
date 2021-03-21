package com.spring.learnspring.persistence;

import com.spring.learnspring.entity.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@DataMongoTest
@RunWith(SpringRunner.class)
public class ReactiveRepositoryAdaptorTest {

    @Autowired
    ReactiveRepositoryAdaptor reactiveRepositoryAdaptor;

    @BeforeEach
    public void setup() {
        List<Item> listItems = Arrays.asList(new Item(null, "Samsung TV", 400.0),
                new Item(null, "LG TV", 430.0),
                new Item(null, "Apple Watch", 180.0),
                new Item("1", "Amazon Echo", 200.0));
        reactiveRepositoryAdaptor
                .deleteAll()
                .thenMany(Flux.fromIterable(listItems))
                .flatMap(reactiveRepositoryAdaptor::save)
                .doOnNext(item -> System.out.println("Item Inserted : " + item))
                .blockLast();


    }

    @Test
    public void testGetAllItems() {
        StepVerifier.create(reactiveRepositoryAdaptor.findAll().log())
                .expectSubscription()
                .expectNextCount(4)
                .verifyComplete();
    }

    @Test
    public void testGetItemById() {
        StepVerifier.create(reactiveRepositoryAdaptor.findById("1").log())
                .expectSubscription()
                .expectNextMatches(item -> item.getName().equalsIgnoreCase("Amazon Echo"))
                .verifyComplete();
    }

    @Test
    public void testGetItemByDescription() {
        StepVerifier.create(reactiveRepositoryAdaptor.findByName("Amazon Echo").log())
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void testInsertItem() {

        Mono<Item> insertedItem = reactiveRepositoryAdaptor.save(new Item(null, "Google Home", 80.0));
        StepVerifier.create(insertedItem.log())
                .expectSubscription()
                .expectNextMatches(item -> item.getName().equalsIgnoreCase("Google Home"))
                .verifyComplete();
    }

    @Test
    public void testUpdateItem() {

        Mono<Item> updatedItem = reactiveRepositoryAdaptor
                .findById("1")
                .map(item -> {
                    item.setPrice(220.0);
                    return item;
                })
                .flatMap(reactiveRepositoryAdaptor::save);
        StepVerifier.create(updatedItem.log())
                .expectSubscription()
                .expectNextMatches(item -> item.getPrice().equals(220.0))
                .verifyComplete();
    }

    @Test
    public void testDeleteItemById() {
        Mono<Void> deletedItem = reactiveRepositoryAdaptor
                .findById("1")
                .flatMap(reactiveRepositoryAdaptor::delete);
        StepVerifier.create(deletedItem.log())
                .expectSubscription()
                .verifyComplete();
        StepVerifier.create(reactiveRepositoryAdaptor.findAll().log())
                .expectSubscription()
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    public void testDeleteItemByName() {
        Mono<Void> deletedItem = reactiveRepositoryAdaptor
                .findByName("Samsung TV")
                .flatMap(reactiveRepositoryAdaptor::delete);
        StepVerifier.create(deletedItem.log())
                .expectSubscription()
                .verifyComplete();
        StepVerifier.create(reactiveRepositoryAdaptor.findAll().log())
                .expectSubscription()
                .expectNextCount(3)
                .verifyComplete();
    }
}
