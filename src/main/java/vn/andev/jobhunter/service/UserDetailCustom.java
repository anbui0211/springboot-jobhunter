package vn.andev.jobhunter.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component("userDetailsService")
public class UserDetailCustom implements UserDetailsService {

    private final UserService userService;

    public UserDetailCustom(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        vn.andev.jobhunter.domain.User user = this.userService.handleGetUserByUsername(username);

        // If user does not exist
        if (user == null) {
            throw new UsernameNotFoundException("username/password không hợp lệ");
        }

        /**
         * Thay vì return trực tiếp UserDetails thì chúng ra return về User của
         * JavaSpring Security
         * Chỉ cần return thèn con (User), nó sẽ tự convert sang thèn cha (UserDetails)
         * - Tính đa hình trong Java
         */
        return new User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
