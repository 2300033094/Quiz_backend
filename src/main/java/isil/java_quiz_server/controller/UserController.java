package isil.java_quiz_server.controller;

import isil.java_quiz_server.modal.User;
import isil.java_quiz_server.repository.UserRepository;
import isil.java_quiz_server.requests.LoginRequest;
import isil.java_quiz_server.response.LoginResponse;
import isil.java_quiz_server.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LoginService loginService;

    @PostMapping("/register")
    public ResponseEntity<User> newUser(@RequestBody User newUser) {
        logger.debug("Registering user: {}", newUser.getUsername());
        if (newUser.getUsername() == null || newUser.getPassword() == null) {
            logger.warn("Invalid user data: username={}, password={}", 
                newUser.getUsername(), newUser.getPassword() != null ? "[provided]" : "null");
            return ResponseEntity.badRequest().build();
        }
        User savedUser = userRepository.save(newUser);
        logger.info("User registered: id={}", savedUser.getId());
        return ResponseEntity.ok(savedUser);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        logger.debug("Fetching all users");
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        logger.debug("Attempting login for username: {}", loginRequest.getUsername());
        LoginResponse loginResponse = loginService.authenticateUser(loginRequest);
        if (loginResponse.isAuthenticated()) {
            logger.info("Login successful for username: {}", loginRequest.getUsername());
            return ResponseEntity.ok(loginResponse);
        } else {
            logger.warn("Login failed for username: {}", loginRequest.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginResponse);
        }
    }
}