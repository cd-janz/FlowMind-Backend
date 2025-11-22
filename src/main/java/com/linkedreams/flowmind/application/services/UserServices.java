package com.linkedreams.flowmind.application.services;

import com.linkedreams.flowmind.infrastructure.dto.CreateUserDTO;
import com.linkedreams.flowmind.infrastructure.dto.LoginUserDTO;
import reactor.core.publisher.Mono;

public interface UserServices {
    public Mono<Void> createUser(CreateUserDTO user);
    public Mono<Void> login(LoginUserDTO user);
}
