package com.linkedreams.flowmind.infrastructure.repositories;

import com.linkedreams.flowmind.infrastructure.R2DBC.RoleEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface RoleRepository extends ReactiveCrudRepository<RoleEntity, Integer> {
    @Query("select r.id from roles r where r.code = :role")
    Mono<RoleEntity> findRoleEntityByCode(String code);
}
