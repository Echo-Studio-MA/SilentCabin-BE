package io.echo.silentcabin.user.domain;

public enum Role {
    USER, ADMIN;

    public static Role of(String value) {
        for (Role role : Role.values()) {
            if (role.name().equalsIgnoreCase(value)) {
                return role;
            }
        }
        return null;
    }
}
