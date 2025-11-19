package com.linkedreams.flowmind.infrastructure.mappers;

import com.linkedreams.flowmind.infrastructure.dto.CreateUserDTO;
import com.linkedreams.flowmind.infrastructure.entities.R2DBC.UserEntity;
import com.linkedreams.flowmind.infrastructure.entities.redis.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toRedis(UserEntity user){
        return new User(
                user.getId().toString(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                user.getUsername(),
                user.getPhoneNumber()
        );
    }
    public UserEntity toEntity(CreateUserDTO user, String encryptedPassword){
        return new UserEntity(
                null,
                user.firstName(),
                user.lastName(),
                user.email(),
                encryptedPassword,
                user.username(),
                user.phoneNumber()
        );
    }
}
