package com.linkedreams.flowmind.application.services.impl;

import com.linkedreams.flowmind.application.services.UserServices;
import com.linkedreams.flowmind.application.utils.EncryptionUtils;
import com.linkedreams.flowmind.application.utils.GenerationUtils;
import com.linkedreams.flowmind.application.utils.ValidationUtils;
import com.linkedreams.flowmind.infrastructure.R2DBC.RoleEntity;
import com.linkedreams.flowmind.infrastructure.configuration.JwtSupport;
import com.linkedreams.flowmind.infrastructure.dto.BasicUserResponse;
import com.linkedreams.flowmind.infrastructure.dto.CreateUserDTO;
import com.linkedreams.flowmind.infrastructure.dto.LoginUserDTO;
import com.linkedreams.flowmind.infrastructure.dto.WithTokenResponse;
import com.linkedreams.flowmind.infrastructure.models.BearerToken;
import com.linkedreams.flowmind.infrastructure.redis.User;
import com.linkedreams.flowmind.infrastructure.mappers.UserMapper;
import com.linkedreams.flowmind.infrastructure.repositories.RoleRepository;
import com.linkedreams.flowmind.infrastructure.repositories.UserRepository;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class UserServicesImpl implements UserServices {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ReactiveRedisOperations<String, User> userOps;
    private final ReactiveRedisOperations<String, String> indexOps;
    private final UserMapper userMapper;
    private final JwtSupport jwt;

    public UserServicesImpl(
            UserRepository userRepository, ReactiveRedisOperations<String, User> userOps, UserMapper userMapper,
            ReactiveRedisOperations<String, String> indexOps, RoleRepository roleRepository,
            JwtSupport jwt
    ) {
        this.userRepository = userRepository;
        this.userOps = userOps;
        this.userMapper = userMapper;
        this.indexOps = indexOps;
        this.roleRepository = roleRepository;
        this.jwt = jwt;
    }

    @Override
    public Mono<Void> createUser(CreateUserDTO user) {
        ValidationUtils.ValidateEmail(user.email());
        Mono<String> passwordMono = Mono.fromCallable(() -> EncryptionUtils.encryptPassword(user.password()))
                .subscribeOn(Schedulers.boundedElastic());
        Mono<RoleEntity> roleMono = roleRepository.findRoleEntityByCode("CL01").switchIfEmpty(Mono
                .error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Default role configuration error")));
        return Mono.zip(roleMono, passwordMono)
                .map(tuple -> {
                    RoleEntity role = tuple.getT1();
                    String encryptedPass = tuple.getT2();
                    String username = "";
                    if(user.username() == null || user.username().isEmpty()){
                        username = user.firstName().split(" ")[0] + GenerationUtils.generateCode(8);
                    }
                    return userMapper.toEntity(user, encryptedPass, role.getId(), username);
                })
                .flatMap(userRepository::save)
                .flatMap(saved -> {
                    User userRedis = userMapper.toRedis(saved);
                    Mono<Boolean> emailIndex = indexOps.opsForValue()
                            .set("email:" + saved.getEmail(), saved.getId().toString());
                    Mono<Boolean> userSave = userOps.opsForValue()
                            .set("user:" + saved.getId(), userRedis);
                    return Mono.when(emailIndex, userSave);
                })
                .then();
    }

    @Override
    public Mono<WithTokenResponse> login(LoginUserDTO user) {
        ValidationUtils.ValidateEmail(user.email());
        return indexOps.opsForValue().get("email:" + user.email())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("User not found or Email wrong")))
                .flatMap(userid -> userOps.opsForValue().get("user:" + userid))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Email found, but an error has occurred")))
                .doOnNext(res -> {
                    if(!EncryptionUtils.checkPassword(user.password(), res.password()))
                        throw new IllegalArgumentException("Wrong password!");
                })
                .map(u -> {
                    BasicUserResponse newUser = userMapper.toBasicResponse(u);
                    BearerToken token = jwt.generate(newUser.email());
                    return new WithTokenResponse(token.getValue(), newUser);
                });
    }
}
