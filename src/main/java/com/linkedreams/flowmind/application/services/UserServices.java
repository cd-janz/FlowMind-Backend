package com.linkedreams.flowmind.application.services;

import com.linkedreams.flowmind.infrastructure.dto.CreateUserDTO;
import com.linkedreams.flowmind.infrastructure.dto.LoginUserDTO;
import com.linkedreams.flowmind.infrastructure.dto.WithTokenResponse;
import reactor.core.publisher.Mono;

public interface UserServices {
    public Mono<Void> createUser(CreateUserDTO user);
    public Mono<WithTokenResponse> login(LoginUserDTO user);
}
