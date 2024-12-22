package vn.andev.jobhunter.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Component
public class CorsConfig {
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Chỉ cho phép requests từ frontend chạy ở localhost:3000
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        // Cho phép các HTTP methods này
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Cho phép các headers này
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        // Cho phép gửi credentials (như cookies, authorization headers)
        configuration.setAllowCredentials(true);
        // Cache CORS config trong 3600 giây (1 giờ)
        configuration.setMaxAge(3600L);

        // Tạo source để lưu cấu hình CORS dựa trên URL pattern
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // "/**" => Áp dụng cho tất cả các request
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
