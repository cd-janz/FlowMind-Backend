package com.linkedreams.flowmind.infrastructure.loader;

import com.linkedreams.flowmind.infrastructure.entities.redis.User;
import com.linkedreams.flowmind.infrastructure.mappers.UserMapper;
import com.linkedreams.flowmind.infrastructure.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Component;

@Component
public class UserRedisLoader {
    private final ReactiveRedisConnectionFactory factory;
    private final ReactiveRedisOperations<String, User> ops;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserRedisLoader(
            ReactiveRedisConnectionFactory factory,
            ReactiveRedisOperations<String, User> userOps,
            UserRepository userRepository,
            UserMapper userMapper
    ) {
        this.factory = factory;
        this.ops = userOps;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @PostConstruct
    public void loadData() {
        ReactiveValueOperations<String, User> values = ops.opsForValue();
        factory.getReactiveConnection()
                .serverCommands()
                .flushAll()
                .thenMany(userRepository.findAll())
                .flatMap(user -> values.set(user.getId().toString(), userMapper.toRedis(user)))
                .thenMany(ops.keys("*")
                .flatMap(values::get))
                .subscribe(System.out::println);
    }
}
