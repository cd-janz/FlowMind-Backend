package com.linkedreams.flowmind.infrastructure.R2DBC;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("roles")
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleEntity {
    @Id
    private Integer id;
    private String name;
    private String code;
    private String description;
}
