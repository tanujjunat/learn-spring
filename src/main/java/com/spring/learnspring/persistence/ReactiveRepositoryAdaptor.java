package com.spring.learnspring.persistence;

import com.spring.learnspring.entity.Item;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@Qualifier
public interface ReactiveRepositoryAdaptor extends ReactiveMongoRepository<Item, String> {

    public Mono<Item> findByName(String name);
}
