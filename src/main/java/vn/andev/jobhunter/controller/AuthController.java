package vn.andev.jobhunter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.andev.jobhunter.domain.dto.LoginDTO;
import vn.andev.jobhunter.domain.dto.ResLoginDTO;
import vn.andev.jobhunter.util.SecurityUtil;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
    }

    /*
     * @Valid: Annotation validate reqbody, validate trực tiếp trong model
     */
    @PostMapping("/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody LoginDTO loginDto) {
        // Nạp input username/password vào security
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(), loginDto.getPassword());

        // Mặc định với cơ chế của Spring Security, nó sẽ lưu người dùng vào Memory
        // Xác thực người dùng => cần viết hàm loadUserByUsername (in service)
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authToken);

        // Create token
        String access_token = this.securityUtil.createToken(authentication);

        // Set data into Spring context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Response
        ResLoginDTO res = new ResLoginDTO();
        res.setAccessToken(access_token);
        return ResponseEntity.ok().body(res);
    }
}
