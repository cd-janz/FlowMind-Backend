package com.linkedreams.flowmind.infrastructure.repositories;

import com.linkedreams.flowmind.infrastructure.entities.R2DBC.UserEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends ReactiveCrudRepository<UserEntity, UUID> {
}
