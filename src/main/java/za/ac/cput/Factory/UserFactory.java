package za.ac.cput.Factory;

import za.ac.cput.Domain.User;
import za.ac.cput.Domain.Todo;
import java.util.List;

public class UserFactory {

    public static User createUser(String username, String password, String email) {
        return new User.UserBuilder()
                .setUsername(username)
                .setPassword(password)
                .setEmail(email)
                .build();
    }

    public static User createUserWithId(Long id, String username, String password, String email) {
        return new User.UserBuilder()
                .setId(id)
                .setUsername(username)
                .setPassword(password)
                .setEmail(email)
                .build();
    }

    public static User createUserWithTodos(String username, String password, String email, List<Todo> todos) {
        return new User.UserBuilder()
                .setUsername(username)
                .setPassword(password)
                .setEmail(email)
                .setTodos(todos)
                .build();
    }

    public static User createCompleteUser(Long id, String username, String password, String email, List<Todo> todos) {
        return new User.UserBuilder()
                .setId(id)
                .setUsername(username)
                .setPassword(password)
                .setEmail(email)
                .setTodos(todos)
                .build();
    }

    public static User copyUser(User user) {
        return new User.UserBuilder()
                .copy(user)
                .build();
    }
}