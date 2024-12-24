package vn.andev.jobhunter.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.andev.jobhunter.domain.User;
import vn.andev.jobhunter.service.UserService;
import vn.andev.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("")
    public ResponseEntity<User> createNewUser(@RequestBody User userReq) {
        String hashPassword = this.passwordEncoder.encode(userReq.getPassword());
        userReq.setPassword(hashPassword);
        User user = this.userService.handleCreateUser(userReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") long id) throws IdInvalidException {
        if (id > 100)
            throw new IdInvalidException("Id is greater than 100");
        this.userService.handleDeleteUser(id);
        return ResponseEntity.ok("delete user successfully");
    }

    // Get user by id
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") long id) {
        User user = this.userService.getOneUser(id);
        return ResponseEntity.ok(user);
    }

    // Get all users
    @GetMapping("")
    public ResponseEntity<List<User>> getAllUser() {
        List<User> users = this.userService.getAllUser();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> putMethodName(@PathVariable long id, @RequestBody User reqUser) {
        User user = this.userService.updateUser(id, reqUser);
        return ResponseEntity.ok(user);
    }
}