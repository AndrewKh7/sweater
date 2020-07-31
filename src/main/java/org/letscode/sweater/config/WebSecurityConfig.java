package org.letscode.sweater.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource; // нуже чтобы менеджер мог ходить в БД и искать там пользователей и их роли

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/", "/registration").permitAll()
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .permitAll()
                .and()
                    .logout()
                    .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.jdbcAuthentication()
                .dataSource(this.dataSource)
                .passwordEncoder(NoOpPasswordEncoder.getInstance()) // шифрование паролей.
                // NoOpPasswordEncoder.getInstance() жутко устарел и используется только при тустированиее
                // Далее запрос для поиска пользователя по имени (порядок в запросе очень важен)
                .usersByUsernameQuery("select username, password, active from usr where username=?")
                // список пользователей с их ролями
                /**Пояснение запроса
                 * Мз таблицы user присоедененной в ней таблице, соедененные через поля user_id и u.id
                 * выбираем поля u.username и ur.roles
                 */
                .authoritiesByUsernameQuery("select u.username, ur.roles from usr u inner join user_role ur on u.id = ur.user_id where u.username=?") ;
    }
}
