package com.linkedreams.flowmind.application.ports.driving;

import com.linkedreams.flowmind.infrastructure.dto.CreateUserDTO;
import reactor.core.publisher.Mono;

public interface UserServices {
    public Mono<Void> createUser(CreateUserDTO user);
}
