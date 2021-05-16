package ink.champ.models;

import org.springframework.security.core.GrantedAuthority;

/**
 * Перечисление пользовательских ролей
 * @author Maxim
 */
public enum Role implements GrantedAuthority {
    GUEST, USER, ADMIN;

    @Override public String getAuthority() { return name(); }
}
