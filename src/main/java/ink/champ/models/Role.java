package ink.champ.models;

import org.springframework.security.core.GrantedAuthority;

/**
 * Перечисление пользовательских ролей
 * @author Maxim
 */
public enum Role implements GrantedAuthority {

    GUEST, USER, ADMIN;

    /**
     * Метод для получения названия роли
     * @return Название роли
     */
    @Override
    public String getAuthority() { return name(); }

}
