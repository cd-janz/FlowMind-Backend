package com.linkedreams.flowmind.infrastructure.entities.R2DBC;

import com.linkedreams.flowmind.infrastructure.utils.GenerationUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("users")
@Getter
@Setter
public class UserEntity {
    @Id
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String username;
    private String phoneNumber;
    public UserEntity(String firstName, String lastName, String email, String password, String phoneNumber) {
        this.id = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.username = firstName.split(" ")[0] + GenerationUtil.generateCode(8);
        this.phoneNumber = phoneNumber;
    }
    public UserEntity(String firstName, String lastName, String email, String password) {
        this.id = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.username = firstName.split(" ")[0] + GenerationUtil.generateCode(8);
    }
}
