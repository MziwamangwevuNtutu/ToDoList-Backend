/**
 * @Author: [Your Name]
 * @Student Number: [Your Student Number]
 **/

package za.ac.cput.Service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import za.ac.cput.Domain.User;
import za.ac.cput.Factory.UserFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class UserServiceTest {

    @Autowired
    private UserService service;

    private User testUser;
    private static Long savedUserId; // Make it static to persist across tests

    @BeforeEach
    void setUp() {
        // Create a base user using the Factory
        this.testUser = UserFactory.createUser("johndoe", "SecurePass123!", "john@example.com");
    }

    @Test
    @Order(1)
    void create() {
        User createdUser = this.service.create(this.testUser);

        assertNotNull(createdUser, "Created user should not be null");
        assertNotNull(createdUser.getId(), "Created user should have a generated ID");

        savedUserId = createdUser.getId(); // Save to static variable
        System.out.println("✅ Created User with ID: " + savedUserId);
        System.out.println("✅ Created User: " + createdUser);
    }

    @Test
    @Order(2)
    void read() {
        assertNotNull(savedUserId, "savedUserId should not be null - create() test must run first");

        Optional<User> foundUser = this.service.read(savedUserId);

        assertTrue(foundUser.isPresent(), "User should be found by ID");
        assertEquals("johndoe", foundUser.get().getUsername());
        System.out.println("✅ Read User: " + foundUser.get());
    }

    @Test
    @Order(3)
    void update() {
        assertNotNull(savedUserId, "savedUserId should not be null");

        Optional<User> existingUserOpt = this.service.read(savedUserId);
        assertTrue(existingUserOpt.isPresent(), "User must exist before updating");

        User existingUser = existingUserOpt.get();

        User updateDetails = new User.UserBuilder()
                .copy(existingUser)
                .setEmail("newemail@example.com")
                .build();

        User updatedUser = this.service.update(savedUserId, updateDetails);

        assertNotNull(updatedUser, "Updated user should not be null");
        assertEquals("newemail@example.com", updatedUser.getEmail());
        System.out.println("✅ Updated User: " + updatedUser);
    }

    @Test
    @Order(4)
    void getAll() {
        List<User> users = this.service.getAll();
        assertNotNull(users);
        assertTrue(users.size() >= 1, "Should have at least one user");
        System.out.println("✅ All Users Count: " + users.size());
        System.out.println("✅ Users: " + users);
    }

    @Test
    @Order(5)
    void findByUsername() {
        Optional<User> foundUser = this.service.findByUsername("johndoe");
        assertTrue(foundUser.isPresent(), "User should be found by username");
        assertEquals("newemail@example.com", foundUser.get().getEmail());
        System.out.println("✅ Found by Username: " + foundUser.get());
    }

    @Test
    @Order(6)
    void findByEmail() {
        Optional<User> foundUser = this.service.findByEmail("newemail@example.com");
        assertTrue(foundUser.isPresent(), "User should be found by email");
        assertEquals("johndoe", foundUser.get().getUsername());
        System.out.println("✅ Found by Email: " + foundUser.get());
    }

    @Test
    @Order(7)
    void existsByUsername() {
        assertTrue(this.service.existsByUsername("johndoe"), "Username should exist");
        assertFalse(this.service.existsByUsername("nonexistent_user"), "Non-existent username should not exist");
        System.out.println("✅ Exists by Username check passed");
    }

    @Test
    @Order(8)
    void existsByEmail() {
        assertTrue(this.service.existsByEmail("newemail@example.com"), "Email should exist");
        assertFalse(this.service.existsByEmail("fake@example.com"), "Non-existent email should not exist");
        System.out.println("✅ Exists by Email check passed");
    }

    @Test
    @Order(9)
    void findUsersByTodoCount() {
        User todoUser = UserFactory.createUserWithTodos("todoguy", "password", "todo@example.com", new ArrayList<>());
        this.service.create(todoUser);

        List<User> users = this.service.findUsersByTodoCount(0);
        assertNotNull(users);
        assertTrue(users.stream().anyMatch(u -> u.getUsername().equals("todoguy")),
                "Should find user with 0 todos");
        System.out.println("✅ Users with 0 todos: " + users);
    }

    @Test
    @Order(10)
    void delete() {
        assertNotNull(savedUserId, "savedUserId should not be null before delete");

        this.service.delete(savedUserId);

        Optional<User> deletedCheck = this.service.read(savedUserId);
        assertTrue(deletedCheck.isEmpty(), "User should no longer exist after deletion");
        System.out.println("✅ User deleted successfully");
    }
}