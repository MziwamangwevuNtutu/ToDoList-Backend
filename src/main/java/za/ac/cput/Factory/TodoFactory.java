package za.ac.cput.Factory;

import za.ac.cput.Domain.Todo;
import za.ac.cput.Domain.User;
import java.time.LocalDateTime;

public class TodoFactory {

    public static Todo createTodo(String title, String description, User user) {
        return new Todo.TodoBuilder()
                .setTitle(title)
                .setDescription(description)
                .setCompleted(false)
                .setUser(user)
                .build();
    }

    public static Todo createTodoWithStatus(String title, String description, Boolean completed, User user) {
        return new Todo.TodoBuilder()
                .setTitle(title)
                .setDescription(description)
                .setCompleted(completed)
                .setUser(user)
                .build();
    }

    public static Todo createTodoWithId(Long id, String title, String description, Boolean completed, User user) {
        return new Todo.TodoBuilder()
                .setId(id)
                .setTitle(title)
                .setDescription(description)
                .setCompleted(completed)
                .setUser(user)
                .build();
    }

    public static Todo createTodoWithTimestamps(String title, String description, Boolean completed, LocalDateTime createdAt, LocalDateTime updatedAt, User user) {
        return new Todo.TodoBuilder()
                .setTitle(title)
                .setDescription(description)
                .setCompleted(completed)
                .setCreatedAt(createdAt)
                .setUpdatedAt(updatedAt)
                .setUser(user)
                .build();
    }

    public static Todo createCompleteTodo(Long id, String title, String description, Boolean completed, LocalDateTime createdAt, LocalDateTime updatedAt, User user) {
        return new Todo.TodoBuilder()
                .setId(id)
                .setTitle(title)
                .setDescription(description)
                .setCompleted(completed)
                .setCreatedAt(createdAt)
                .setUpdatedAt(updatedAt)
                .setUser(user)
                .build();
    }

    public static Todo copyTodo(Todo todo) {
        return new Todo.TodoBuilder().copy(todo).build();
    }

    public static Todo createSimpleTodo(String title, User user) {
        return new Todo.TodoBuilder()
                .setTitle(title)
                .setCompleted(false)
                .setUser(user)
                .build();
    }
}