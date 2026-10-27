package za.ac.cput.Controller;

import org.springframework.http.HttpStatus;
import za.ac.cput.Domain.Todo;
import za.ac.cput.Domain.User;
import za.ac.cput.Service.TodoService;
import za.ac.cput.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "http://localhost:3000")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<Todo>> getUserTodos(@AuthenticationPrincipal UserDetails userDetails) {
        Optional<User> user = userService.findByUsername(userDetails.getUsername());
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<Todo> todos = todoService.findByUserId(user.get().getId());
        return ResponseEntity.ok(todos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Todo> todoOptional = todoService.read(id);
        Optional<User> userOptional = userService.findByUsername(userDetails.getUsername());

        if (todoOptional.isEmpty() || userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Todo todo = todoOptional.get();
        User user = userOptional.get();

        if (!todo.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(todo);
    }

    @PostMapping
    public ResponseEntity<Todo> createTodo(@RequestBody Map<String, String> todoData, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<User> userOptional = userService.findByUsername(userDetails.getUsername());
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOptional.get();
        Todo todo = new Todo.TodoBuilder()
                .setTitle(todoData.get("title"))
                .setDescription(todoData.get("description"))
                .setUser(user)
                .setCompleted(false)
                .build();

        Todo savedTodo = todoService.create(todo);
        return ResponseEntity.ok(savedTodo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo updatedTodo, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<User> userOptional = userService.findByUsername(userDetails.getUsername());
        Optional<Todo> existingTodoOptional = todoService.read(id);

        if (userOptional.isEmpty() || existingTodoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOptional.get();
        Todo existingTodo = existingTodoOptional.get();

        if (!existingTodo.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // SAFE UPDATE: Only update fields if they are provided, otherwise keep existing values
        Todo todoToUpdate = new Todo.TodoBuilder()
                .copy(existingTodo)
                .setTitle(updatedTodo.getTitle() != null ? updatedTodo.getTitle() : existingTodo.getTitle())
                .setDescription(updatedTodo.getDescription() != null ? updatedTodo.getDescription() : existingTodo.getDescription())
                .setCompleted(updatedTodo.getCompleted() != null ? updatedTodo.getCompleted() : existingTodo.getCompleted())
                .build();

        Todo savedTodo = todoService.update(id, todoToUpdate);
        return ResponseEntity.ok(savedTodo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<User> userOptional = userService.findByUsername(userDetails.getUsername());
        Optional<Todo> todoOptional = todoService.read(id);

        if (userOptional.isEmpty() || todoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOptional.get();
        Todo todo = todoOptional.get();

        if (!todo.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        todoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}