package com.linkedreams.flowmind.infrastructure.redis.loader;

import com.linkedreams.flowmind.infrastructure.redis.User;
import com.linkedreams.flowmind.infrastructure.mappers.UserMapper;
import com.linkedreams.flowmind.infrastructure.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UserRedisLoader {
    private final ReactiveRedisConnectionFactory factory;
    private final ReactiveRedisOperations<String, User> ops;
    private final ReactiveRedisOperations<String, String> indexOps;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserRedisLoader(
            ReactiveRedisConnectionFactory factory,
            ReactiveRedisOperations<String, User> userOps,
            ReactiveRedisOperations<String, String> indexOps,
            UserRepository userRepository,
            UserMapper userMapper
    ) {
        this.factory = factory;
        this.ops = userOps;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.indexOps = indexOps;
    }

    @PostConstruct
    public void loadData() {
        ReactiveValueOperations<String, User> userValues = ops.opsForValue();
        ReactiveValueOperations<String, String> indexValues = indexOps.opsForValue();

        factory.getReactiveConnection()
                .serverCommands()
                .flushAll()
                .thenMany(userRepository.findUsersWithRole())
                .flatMap(user -> {
                    System.out.println(user.toString());
                    String userId = user.id().toString();
                    User redisUser = userMapper.toRedis(user);

                    Mono<Boolean> saveUser = userValues.set("user:" + userId, redisUser);
                    Mono<Boolean> saveIndex = indexValues.set("email:" + user.email(), userId);

                    return Mono.when(saveUser, saveIndex);
                })
                .thenMany(ops.keys("user:*"))
                .flatMap(userValues::get)
                .subscribe(System.out::println);
    }

}
