package com.linkedreams.flowmind.infrastructure.repositories;

import com.linkedreams.flowmind.infrastructure.R2DBC.UserEntity;
import com.linkedreams.flowmind.infrastructure.R2DBC.projections.UserMiddleProjection;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface UserRepository extends ReactiveCrudRepository<UserEntity, UUID> {
    @Query("select * from users u where u.email = :email")
    Mono<UserEntity> findUserEntityByEmail(String email);
    @Query("""
        SELECT u.id,
           u.first_name,
           u.last_name,
           u.username,
           u.email,
           u.password,
           u.phone_number,
           u.profile_photo,
           u.created_at,
           u.updated_at,
           r.id AS role_id,
           r.name AS role_name,
           r.code AS role_code,
           r.description AS role_description,
           r.value AS role_value
        FROM USERS u
        JOIN ROLES r ON u.role_id = r.id;
    """)
    Flux<UserMiddleProjection> findUsersWithRole();
}
