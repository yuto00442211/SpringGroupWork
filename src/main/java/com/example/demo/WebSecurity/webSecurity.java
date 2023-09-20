package com.example.demo.WebSecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class webSecurity {

    // パスワードの暗号化に使用するエンコーダーを設定
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // セキュリティフィルターチェインを設定
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // CSRF保護を有効化し、トークンリポジトリを設定
            .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
            
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    // 指定のURLパターンへのアクセスをすべて許可
                    .requestMatchers(
                        "/sell",
                        "/sell/login",
                        "/sell/sub",
                        "/sell/product",
                        "/sell/genreItemList",
                        "/sell/itemList",
                        "/sell/success"
                    ).permitAll()
                    
                    // その他のリクエストは認証が必要
                    .anyRequest().authenticated()
            )
            
            .formLogin(formLogin ->
                formLogin
                    // ログインページを指定し、認証が必要でないように設定
                    .loginPage("/sell/login").permitAll()
                    // ログイン成功後のデフォルトのリダイレクト先を指定
                    .defaultSuccessUrl("/afterLogin/sell", true)
            )
            
            .logout(logout ->
                logout
                    // ログアウトのリクエストをマッチさせるパスを指定
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    // ログアウト成功後のリダイレクト先を指定
                    .logoutSuccessUrl("/sell")
                    // クッキーを削除し、HTTPセッションを無効にする
                    .deleteCookies("JSESSIONID")
                    .invalidateHttpSession(true)
            );

        // セキュリティフィルターチェインを構築して返す
        return http.build();
    }
}
