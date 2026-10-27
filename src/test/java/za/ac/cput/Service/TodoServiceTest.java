/**
 * @Author: [Your Name]
 * @Student Number: [Your Student Number]
 **/

package za.ac.cput.Service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import za.ac.cput.Domain.Todo;
import za.ac.cput.Domain.User;
import za.ac.cput.Factory.TodoFactory;
import za.ac.cput.Factory.UserFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class TodoServiceTest {

    @Autowired
    private TodoService todoService;

    @Autowired
    private UserService userService;

    private User testUser;
    private Todo testTodo;
    private Long savedTodoId;
    private static int testCounter = 0;

    @BeforeEach
    void setUp() {
        testCounter++;
        // Create unique user for each test to avoid duplicate errors
        String uniqueEmail = "test" + System.currentTimeMillis() + "_" + testCounter + "@example.com";
        String uniqueUsername = "testuser" + System.currentTimeMillis() + "_" + testCounter;

        User newUser = UserFactory.createUser(uniqueUsername, "SecurePass123!", uniqueEmail);
        this.testUser = userService.create(newUser);

        // Create a base Todo using the Factory
        this.testTodo = TodoFactory.createTodo("Buy groceries " + testCounter, "Milk and eggs", this.testUser);
    }

    @Test
    @Order(1)
    void create() {
        Todo createdTodo = this.todoService.create(this.testTodo);

        assertNotNull(createdTodo, "Created todo should not be null");
        assertNotNull(createdTodo.getId(), "Created todo should have a generated ID");
        assertEquals("Buy groceries " + testCounter, createdTodo.getTitle());
        assertFalse(createdTodo.isCompleted(), "New todo should not be completed by default");

        this.savedTodoId = createdTodo.getId();
        System.out.println("✅ Created Todo: " + createdTodo);
    }

    @Test
    @Order(2)
    void read() {
        // Create a todo specifically for this test
        Todo todo = this.todoService.create(this.testTodo);

        Optional<Todo> foundTodo = this.todoService.read(todo.getId());

        assertTrue(foundTodo.isPresent(), "Todo should be found by ID");
        assertEquals("Buy groceries " + testCounter, foundTodo.get().getTitle());
        System.out.println("✅ Read Todo: " + foundTodo.get());
    }

    @Test
    @Order(3)
    void update() {
        // Create a todo for this test
        Todo todo = this.todoService.create(this.testTodo);

        // Build updated details
        Todo updateDetails = new Todo.TodoBuilder()
                .copy(todo)
                .setTitle("Buy groceries and cook")
                .setDescription("Milk, eggs, and pasta")
                .setCompleted(true)
                .build();

        Todo updatedTodo = this.todoService.update(todo.getId(), updateDetails);

        assertNotNull(updatedTodo, "Updated todo should not be null");
        assertEquals("Buy groceries and cook", updatedTodo.getTitle());
        assertTrue(updatedTodo.isCompleted(), "Todo should be marked as completed");
        System.out.println("✅ Updated Todo: " + updatedTodo);
    }

    @Test
    @Order(4)
    void getAll() {
        // Create a todo first
        this.todoService.create(this.testTodo);

        List<Todo> todos = this.todoService.getAll();

        assertNotNull(todos);
        assertTrue(todos.size() >= 1, "Should have at least one todo");
        System.out.println("✅ All Todos Count: " + todos.size());
    }

    @Test
    @Order(5)
    void findByUserId() {
        // Create todo for this user
        this.todoService.create(this.testTodo);

        List<Todo> todos = this.todoService.findByUserId(this.testUser.getId());

        assertNotNull(todos);
        assertFalse(todos.isEmpty(), "Should find todos for the user");
        System.out.println("✅ Found Todos by User ID: " + todos.size());
    }

    @Test
    @Order(6)
    void findByUser() {
        // Create todo for this user
        this.todoService.create(this.testTodo);

        List<Todo> todos = this.todoService.findByUser(this.testUser);

        assertNotNull(todos);
        assertFalse(todos.isEmpty(), "Should find todos for the user object");
        System.out.println("✅ Found Todos by User Object: " + todos.size());
    }

    @Test
    @Order(7)
    void findByTitle() {
        // Create todo with specific title
        this.todoService.create(this.testTodo);

        List<Todo> todos = this.todoService.findByTitle("Buy groceries " + testCounter);

        assertNotNull(todos);
        assertFalse(todos.isEmpty(), "Should find todo by title");
        assertEquals("Buy groceries " + testCounter, todos.get(0).getTitle());
        System.out.println("✅ Found Todos by Title: " + todos);
    }

    @Test
    @Order(8)
    void findByCompleted() {
        // Create and mark todo as completed
        this.testTodo.setCompleted(true);
        this.todoService.create(this.testTodo);

        List<Todo> completedTodos = this.todoService.findByCompleted(true);

        assertNotNull(completedTodos);
        assertFalse(completedTodos.isEmpty(), "Should find completed todos");
        System.out.println("✅ Found Completed Todos: " + completedTodos.size());
    }

    @Test
    @Order(9)
    void findByTitleAndUser() {
        this.todoService.create(this.testTodo);

        List<Todo> todos = this.todoService.findByTitleAndUser("Buy groceries " + testCounter, this.testUser);

        assertNotNull(todos);
        assertFalse(todos.isEmpty(), "Should find todo by title and user");
        System.out.println("✅ Found Todos by Title and User: " + todos);
    }

    @Test
    @Order(10)
    void findByCompletedAndUser() {
        this.testTodo.setCompleted(false);
        this.todoService.create(this.testTodo);

        List<Todo> todos = this.todoService.findByCompletedAndUser(false, this.testUser);

        assertNotNull(todos);
        assertFalse(todos.isEmpty(), "Should find incomplete todos for user");
        System.out.println("✅ Found Incomplete Todos for User: " + todos);
    }

    @Test
    @Order(11)
    void findByCreatedAtAfter() {
        // Create a todo first
        this.todoService.create(this.testTodo);

        // Get todos created after 1 hour ago (should include our recently created todo)
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        List<Todo> todos = this.todoService.findByCreatedAtAfter(oneHourAgo);

        assertNotNull(todos);
        assertFalse(todos.isEmpty(), "Should find todos created recently");
        System.out.println("✅ Found Todos Created After: " + todos.size());
    }

    @Test
    @Order(12)
    void findByUpdatedAtAfter() {
        // Create todo first
        this.todoService.create(this.testTodo);

        LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(1);
        List<Todo> todos = this.todoService.findByUpdatedAtAfter(oneMinuteAgo);

        assertNotNull(todos);
        assertFalse(todos.isEmpty(), "Should find todos updated recently");
        System.out.println("✅ Found Todos Updated After: " + todos.size());
    }

    @Test
    @Order(13)
    void countByUser() {
        // Create a todo first
        this.todoService.create(this.testTodo);

        long count = this.todoService.countByUser(this.testUser);

        assertTrue(count >= 1, "User should have at least one todo");
        System.out.println("✅ Todo Count by User: " + count);
    }

    @Test
    @Order(14)
    void countByCompleted() {
        // Create and mark todo as completed
        this.testTodo.setCompleted(true);
        this.todoService.create(this.testTodo);

        long count = this.todoService.countByCompleted(true);

        assertTrue(count >= 1, "Should have at least one completed todo");
        System.out.println("✅ Completed Todo Count: " + count);
    }

    @Test
    @Order(15)
    void toggleCompletion() {
        // Create a completed todo first
        this.testTodo.setCompleted(true);
        Todo createdTodo = this.todoService.create(this.testTodo);

        // Toggle it (should become false)
        Todo toggledTodo = this.todoService.toggleCompletion(createdTodo.getId());

        assertNotNull(toggledTodo);
        assertFalse(toggledTodo.isCompleted(), "Todo should be toggled to incomplete");
        System.out.println("✅ Toggled Todo Completion: " + toggledTodo.isCompleted());
    }

    @Test
    @Order(16)
    void delete() {
        // Create a todo to delete
        Todo todoToDelete = this.todoService.create(this.testTodo);

        this.todoService.delete(todoToDelete.getId());

        Optional<Todo> deletedCheck = this.todoService.read(todoToDelete.getId());
        assertTrue(deletedCheck.isEmpty(), "Todo should no longer exist after deletion");
        System.out.println("✅ Todo deleted successfully");
    }
}