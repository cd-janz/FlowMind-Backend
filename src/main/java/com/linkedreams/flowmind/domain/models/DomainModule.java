package com.linkedreams.flowmind.domain.models;

public class DomainModule {
    private final Integer id;
    private final String name;
    private final String code;
    private final String description;
    private final Integer permissionValue;
    private DomainModule(Integer id, String name, String code, String description, Integer permissionValue) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("module name ain't provided");
        if (code == null || code.isBlank()) throw new IllegalArgumentException("module code ain't provided");
        if (permissionValue == null) throw new IllegalArgumentException("user don't have permissions on this module");
        this.id = id;
        this.name = name;
        this.code = code;
        this.description = description;
        this.permissionValue = permissionValue;
    }
    public static DomainModule createMinimal(String name, String code, Integer permissionValue) {
        return new DomainModule(null, name, code, null, permissionValue);
    }
    public static DomainModule create(String name, String code, String description, Integer permissionValue) {
        return new DomainModule(null, name, code, description, permissionValue);
    }
    public static DomainModule createFull(Integer id, String name, String code, String description, Integer permissionValue) {
        return new DomainModule(id, name, code, description, permissionValue);
    }
    public static DomainModule rehydrate(Integer id, String name, String code, String description, Integer permissionValue) {
        return new DomainModule(id, name, code, description, permissionValue);
    }
    public Integer getId() {
        return id;
    }
    public String getDescription() {
        return description;
    }
    public String getName() {
        return name;
    }
    public String getCode() {
        return code;
    }
    public Integer getPermissionValue() {
        return permissionValue;
    }
}

