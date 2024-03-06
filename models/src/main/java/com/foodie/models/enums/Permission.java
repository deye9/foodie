package com.foodie.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),

    MANAGER_READ("management:read"),
    MANAGER_UPDATE("management:update"),
    MANAGER_CREATE("management:create"),
    MANAGER_DELETE("management:delete"),

    USER_READ("user:read"),
    USER_UPDATE("user:update"),
    USER_CREATE("user:create"),
    USER_DELETE("user:delete"),

    RIDER_READ("rider:read"),
    RIDER_UPDATE("rider:update"),
    RIDER_CREATE("rider:create"),
    RIDER_DELETE("rider:delete"),


    OWNER_READ("owner:read"),
    OWNER_UPDATE("owner:update"),
    OWNER_CREATE("owner:create"),
    OWNER_DELETE("owner:delete");

    @Getter
    private final String permission;
}
