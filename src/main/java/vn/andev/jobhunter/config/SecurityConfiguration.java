package vn.andev.jobhunter.config;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

import vn.andev.jobhunter.util.SecurityUtil;

// Mục tiêu tại đây: Sử dụng annotation @Bean và @Configuration đè lại cấu hình mặc định của spring
// Theo Cơ chế Spring Security, request sẽ chạy vào filterChain trước, sau đó mới chạy qua @RestControllerAdvice

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

    @Value("${andev.jwt.base64-secret}")
    private String jwtKey;

    // Khởi tạo BCryptPasswordEncoder và đưa vào IoC Container quản lý
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomAuthenticationEntryPoint customAuthEntryPoint)
            throws Exception {
        http
                // Cơ chế bảo vệ, cần phải truyền token
                .csrf(c -> c.disable())
                // Cấu hình CORS và cần override Bean CorsConfigurationSource
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(
                        (authz) -> authz
                                .requestMatchers("/", "/login").permitAll()
                                .anyRequest().authenticated())
                .oauth2ResourceServer(
                        oauth2 -> oauth2.jwt(Customizer.withDefaults())
                                // Sử dụng file cấu hình custom ghi đè lại cấu hình mặc định (JWT exception)
                                .authenticationEntryPoint(customAuthEntryPoint))
                // // Xử lý Exception JWT 401 và 403
                // .exceptionHandling(
                // exceptions -> exceptions
                // .authenticationEntryPoint(customAuthEntryPoint) // 401
                // .accessDeniedHandler(new BearerTokenAccessDeniedHandler())) // 403

                .formLogin(f -> f.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(getSecretKey())
                .macAlgorithm(SecurityUtil.JWT_ALGORITHM).build();

        return token -> {
            try {
                return jwtDecoder.decode(token);
            } catch (Exception e) {
                System.out.println(">>> JWT Decoder error: " + e.getMessage());
                throw e;
            }
        };
    }

    // Mã hóa JWT
    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey()));
    }

    // base64 decode to byte[]
    private SecretKey getSecretKey() {
        byte[] keyBytes = com.nimbusds.jose.util.Base64.from(jwtKey).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, SecurityUtil.JWT_ALGORITHM.getName());
    }

    // Convert data chứa trong token và lưu vào Spring context để tái sử dụng
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("andev");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}
