package ink.champ.configs;

import ink.champ.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Класс конфигурации SpringSecurity
 * @author Maxim
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired private AppService service;

    /**
     * Метод для настройки конфигурации SpringSecurity http
     * @param http Объект HttpSecurity
     * @throws Exception Исключение
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/", "/index", "/champs", "/teams", "/players", "/about", "/error",
                            "/restore", "/registration", "/login", "/post/auth/*",
                            "/css/**", "/img/**", "/fontawesome/**"
                    ).permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .failureUrl("/login?error")
                    .defaultSuccessUrl("/login/success", true)
                    .permitAll()
                    .and()
                .csrf().disable()
                .logout()
                    .permitAll();
    }

    /**
     * Метод для настройки конфигурации SpringSecurity авторизации
     * @param auth Объект AuthenticationManagerBuilder
     * @throws Exception Исключение
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(service).passwordEncoder(new BCryptPasswordEncoder());
    }

}
