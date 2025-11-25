package com.linkedreams.flowmind.infrastructure.mappers;

import com.linkedreams.flowmind.infrastructure.R2DBC.projections.UserMiddleProjection;
import com.linkedreams.flowmind.infrastructure.dto.BasicUserResponse;
import com.linkedreams.flowmind.infrastructure.dto.CreateUserDTO;
import com.linkedreams.flowmind.infrastructure.R2DBC.UserEntity;
import com.linkedreams.flowmind.infrastructure.redis.User;
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
                user.getPhoneNumber(),
                "", "", "7"
        );
    }
    public User toRedis(UserMiddleProjection user){
        return new User(
                user.id().toString(),
                user.first_name(),
                user.last_name(),
                user.email(),
                user.password(),
                user.username(),
                user.phone_number(),
                user.role_name(),
                user.role_code(),
                user.role_value().toString()
        );
    }
    public UserEntity toEntity(CreateUserDTO user, String encryptedPassword, Integer roleId){
        return new UserEntity(
                null,
                user.firstName(),
                user.lastName(),
                user.email(),
                encryptedPassword,
                user.username(),
                user.phoneNumber(),
                null,
                roleId,
                null,
                null
        );
    }
    public BasicUserResponse toBasicResponse(User user){
        return new BasicUserResponse(
                user.id(), user.firstName(), user.lastName(),
                user.email(), user.username(), user.phoneNumber()
        );
    }
}
