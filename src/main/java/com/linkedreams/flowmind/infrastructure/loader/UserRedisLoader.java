package com.linkedreams.flowmind.infrastructure.loader;

import com.linkedreams.flowmind.infrastructure.entities.redis.User;
import jakarta.annotation.PostConstruct;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

@Component
public class UserRedisLoader {
    private final ReactiveRedisConnectionFactory factory;
    private final ReactiveRedisOperations<String, User> ops;

    public UserRedisLoader(ReactiveRedisConnectionFactory factory, ReactiveRedisOperations<String, User> userOps) {
        this.factory = factory;
        this.ops = userOps;
    }

    @PostConstruct
    public void loadData() {
        ReactiveValueOperations<String, User> values = ops.opsForValue();
        List<User> users = List.of(
                new User(UUID.randomUUID().toString(), "Jet", "Black", null, null, null, null),
                new User(UUID.randomUUID().toString(), "Darth", "Redis", null, null, null, null),
                new User(UUID.randomUUID().toString(), "Black", "Alert", null, null, null, null)
        );

        factory.getReactiveConnection()
                .serverCommands()
                .flushAll()
                .thenMany(Flux.fromIterable(users)
                        .flatMap(user -> values.set(user.id(), user)))
                .thenMany(ops.keys("*")
                        .flatMap(values::get))
                .subscribe(System.out::println);
    }
}
