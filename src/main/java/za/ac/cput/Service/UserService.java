package za.ac.cput.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.Domain.User;
import za.ac.cput.Repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(User user) {
        return userRepository.save(user);
    }

    public Optional<User> read(Long id) {
        return userRepository.findById(id);
    }

    public User update(Long userId, User userDetails) {
        return userRepository.findById(userId).map(existingUser -> {
            User updatedUser = new User.UserBuilder()
                    .copy(existingUser)
                    .setUsername(userDetails.getUsername())
                    .setPassword(userDetails.getPassword() != null ? userDetails.getPassword() : existingUser.getPassword())
                    .setEmail(userDetails.getEmail())
                    .build();
            return userRepository.save(updatedUser);
        }).orElse(null);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public List<User> findUsersByTodoCount(int todoCount) {
        return userRepository.findUsersByTodoCount(todoCount);
    }
}