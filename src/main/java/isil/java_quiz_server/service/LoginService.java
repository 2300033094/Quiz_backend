package isil.java_quiz_server.service;

import isil.java_quiz_server.modal.User;
import isil.java_quiz_server.repository.UserRepository;
import isil.java_quiz_server.requests.LoginRequest;
import isil.java_quiz_server.response.LoginResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    @Autowired
    private UserRepository userRepository;

    public LoginResponse authenticateUser(LoginRequest loginRequest) {
        logger.debug("Authenticating user: username={}", loginRequest.getUsername());
        if (loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
            logger.warn("Invalid login request: username={}, password={}", 
                loginRequest.getUsername(), 
                loginRequest.getPassword() != null ? "[provided]" : "null");
            return new LoginResponse(null, false);
        }
        User user = userRepository.findByUsernameAndPassword(
            loginRequest.getUsername().trim(), 
            loginRequest.getPassword().trim()
        );
        boolean isAuthenticated = user != null;
        if (isAuthenticated) {
            logger.info("Authentication successful for username: {}", loginRequest.getUsername());
        } else {
            logger.warn("Authentication failed for username: {}", loginRequest.getUsername());
            User existingUser = userRepository.findByUsername(loginRequest.getUsername().trim());
            if (existingUser == null) {
                logger.warn("No user found with username: {}", loginRequest.getUsername());
            } else {
                logger.warn("Password mismatch for username: {}", loginRequest.getUsername());
            }
        }
        return new LoginResponse(user, isAuthenticated);
    }
}