package com.linkedreams.flowmind.infrastructure.controller;

import com.linkedreams.flowmind.application.ports.driving.UserServices;
import com.linkedreams.flowmind.infrastructure.dto.CreateUserDTO;
import com.linkedreams.flowmind.infrastructure.models.Response;
import com.linkedreams.flowmind.infrastructure.repositories.UserRepository;
import com.linkedreams.flowmind.infrastructure.utils.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/public")
public class Public {
    private final UserServices userServices;
    private UserRepository userRepository;

    public Public(UserServices userServices){
        this.userServices = userServices;
    }

    @PostMapping("/create-user")
    public Mono<ResponseEntity<Response<Void>>> createUser(@Valid @RequestBody CreateUserDTO user){
        return userServices.createUser(user).then(ResponseUtil.created());
    }
}
