package com.linkedreams.flowmind.application.services;

import com.linkedreams.flowmind.application.ports.driving.UserServices;
import com.linkedreams.flowmind.application.utils.EncryptionUtils;
import com.linkedreams.flowmind.application.utils.ValidationUtils;
import com.linkedreams.flowmind.infrastructure.dto.CreateUserDTO;
import com.linkedreams.flowmind.infrastructure.entities.R2DBC.UserEntity;
import com.linkedreams.flowmind.infrastructure.entities.redis.User;
import com.linkedreams.flowmind.infrastructure.mappers.UserMapper;
import com.linkedreams.flowmind.infrastructure.repositories.UserRepository;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserServicesImpl implements UserServices {
    private final UserRepository userRepository;
    private final ReactiveRedisOperations<String, User> userOps;
    private final UserMapper userMapper;

    public UserServicesImpl(UserRepository userRepository, ReactiveRedisOperations<String, User> userOps, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userOps = userOps;
        this.userMapper = userMapper;
    }

    @Override
    public Mono<Void> createUser(CreateUserDTO user) {
        ValidationUtils.ValidateEmail(user.email());
        String password = EncryptionUtils.encryptPassword(user.password());
        UserEntity newUser = userMapper.toEntity(user, password);
        return userRepository
                .save(newUser)
                .flatMap(saved -> {
                    User userRedis = userMapper.toRedis(saved);
                    return userOps.opsForValue().set(userRedis.id(), userRedis);
                })
                .then();
    }
}
