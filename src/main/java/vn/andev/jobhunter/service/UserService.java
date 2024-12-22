package vn.andev.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.andev.jobhunter.domain.User;
import vn.andev.jobhunter.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handleCreateUser(User user) {
        return this.userRepository.save(user);
    }

    public void handleDeleteUser(long id) {
        this.userRepository.deleteById(id);
    }

    public User getOneUser(long id) {
        Optional<User> userOption = this.userRepository.findById(id);
        if (!userOption.isPresent()) {
            return null;
        }
        return userOption.get();
    }

    public List<User> getAllUser() {
        return this.userRepository.findAll();
    }

    public User updateUser(long id, User reqBody) {
        User user = this.getOneUser(id);
        if (user != null) {
            user.setEmail(reqBody.getEmail());
            user.setName(reqBody.getName());
            user.setPassword(reqBody.getPassword());
            this.userRepository.save(user);
        }

        return user;
    }

    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }
}