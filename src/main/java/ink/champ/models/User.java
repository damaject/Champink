package ink.champ.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

/**
 * Класс-модель для представления сущности пользователя
 * @author Maxim
 */
@Entity(name="users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true) private String email;
    private boolean active;

    @OneToMany(targetEntity = Champ.class, mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Champ> champs;

    @OneToMany(mappedBy = "user")
    private Set<Team> teams;

    @OneToMany(targetEntity = Player.class, mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Player> players;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    /**
     * Конструктор пользователя
     */
    public User() { }

    /**
     * Конструктор пользователя
     * @param username Логин
     * @param password Пароль
     * @param name Имя
     * @param email Адрес электронной почты
     */
    public User(String username, String password, String name, String email) {
        this.username = username;
        this.password = new BCryptPasswordEncoder().encode(password);
        this.name = name;
        this.email = email;
        this.active = true;
    }

    /**
     * Метод для установки идентификатора
     * @param id Идентификатор
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Метод для установки имени пользователя
     * @param username Имя пользователя
     */
    public void setUsername(String username) { this.username = username; }

    /**
     * Метод для установки пароля
     * @param password Пароль
     */
    public void setPassword(String password) { this.password = password; }

    /**
     * Метод для установки имени
     * @param name Имя
     */
    public void setName(String name) { this.name = name; }

    /**
     * Метод для установки активности
     * @param active Активность
     */
    public void setActive(boolean active) { this.active = active; }

    /**
     * Метод для установки адреса электронной почты
     * @param email Адрес электронной почты
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Метод для установки ролей
     * @param roles Роли
     */
    public void setRoles(Set<Role> roles) { this.roles = roles; }

    /**
     * Метод для установки чемпионатов
     * @param champs Чемпионаты
     */
    public void setChamps(Set<Champ> champs) { this.champs = champs; }

    /**
     * Метод для установки команд
     * @param teams Команды
     */
    public void setTeams(Set<Team> teams) { this.teams = teams; }

    /**
     * Метод для установки игроков
     * @param players Игроки
     */
    public void setPlayers(Set<Player> players) { this.players = players; }

    /**
     * Метод для получения идентификатора
     * @return Идентификатор
     */
    public Long getId() { return id; }

    /**
     * Метод для получения имени пользователя
     * @return Имя пользователя
     */
    public String getUsername() { return username; }

    /**
     * Метод для получения пароля
     * @return Пароль
     */
    public String getPassword() { return password; }

    /**
     * Метод для получения имени
     * @return Имя
     */
    public String getName() { return name; }

    /**
     * Метод для получения адреса электронной почты
     * @return Адрес электронной почты
     */
    public String getEmail() { return email; }

    /**
     * Метод для получения активности
     * @return Активность
     */
    public boolean isActive() { return active; }

    /**
     * Метод для получения ролей
     * @return Роли
     */
    public Set<Role> getRoles() { return roles; }

    /**
     * Метод для получения чемпионатов
     * @return Чемпионаты
     */
    public Set<Champ> getChamps() { return champs; }

    /**
     * Метод для получения команд
     * @return Команды
     */
    public Set<Team> getTeams() { return teams; }

    /**
     * Метод для получения игроков
     * @return Игроки
     */
    public Set<Player> getPlayers() { return players; }

    /**
     * Метод для получения является ли пользователь администратором
     * @return Администратор
     */
    public boolean isAdmin() { return getRoles().contains(Role.ADMIN); }

    /**
     * Метод для получения, что срок действия аккаунта не истек
     * @return Срок действия аккаунта
     */
    @Override
    public boolean isAccountNonExpired() { return true; }

    /**
     * Метод для получения, что аккаунт не заблокирован
     * @return Статус блокировки
     */
    @Override
    public boolean isAccountNonLocked() { return true; }

    /**
     * Метод для получения, что срок действия учетных данных не истек
     * @return Срок действия учетных данных
     */
    @Override
    public boolean isCredentialsNonExpired() { return true; }

    /**
     * Метод для получения доступности
     * @return Доступность
     */
    @Override
    public boolean isEnabled() { return isActive(); }

    /**
     * Метод для получения ролей авторизации
     * @return Роли авторизации
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return getRoles(); }

}
