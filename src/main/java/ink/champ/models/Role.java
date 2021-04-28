package ink.champ.models;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    GUEST, USER, ADMIN;

    @Override public String getAuthority() { return name(); }
}
