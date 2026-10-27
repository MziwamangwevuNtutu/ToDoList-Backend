package za.ac.cput.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.ac.cput.Domain.Todo;
import za.ac.cput.Domain.User;
import za.ac.cput.Repository.TodoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    @Autowired
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public Todo create(Todo todo) {
        return todoRepository.save(todo);
    }

    public Optional<Todo> read(Long id) {
        return todoRepository.findById(id);
    }

    public Todo update(Long todoId, Todo todoDetails) {
        Optional<Todo> existingTodoOptional = todoRepository.findById(todoId);
        if (existingTodoOptional.isPresent()) {
            Todo existingTodo = existingTodoOptional.get();
            Todo updatedTodo = new Todo.TodoBuilder()
                    .copy(existingTodo)
                    .setTitle(todoDetails.getTitle() != null ? todoDetails.getTitle() : existingTodo.getTitle())
                    .setDescription(todoDetails.getDescription() != null ? todoDetails.getDescription() : existingTodo.getDescription())
                    .setCompleted(todoDetails.getCompleted() != null ? todoDetails.getCompleted() : existingTodo.getCompleted())
                    .setUser(existingTodo.getUser())
                    .build();
            return todoRepository.save(updatedTodo);
        }
        return null;
    }

    public List<Todo> getAll() {
        return todoRepository.findAll();
    }

    public List<Todo> findByUserId(Long userId) {
        return todoRepository.findByUserId(userId);
    }

    public List<Todo> findByUser(User user) {
        return todoRepository.findByUser(user);
    }

    public List<Todo> findByTitle(String title) {
        return todoRepository.findByTitle(title);
    }

    public List<Todo> findByCompleted(boolean completed) {
        return todoRepository.findByCompleted(completed);
    }

    public List<Todo> findByTitleAndUser(String title, User user) {
        return todoRepository.findByTitleAndUser(title, user);
    }

    public List<Todo> findByCompletedAndUser(boolean completed, User user) {
        return todoRepository.findByCompletedAndUser(completed, user);
    }

    public void delete(Long id) {
        todoRepository.deleteById(id);
    }

    public List<Todo> findByCreatedAtAfter(LocalDateTime dateTime) {
        return todoRepository.findByCreatedAtAfter(dateTime);
    }

    public List<Todo> findByUpdatedAtAfter(LocalDateTime dateTime) {
        return todoRepository.findByUpdatedAtAfter(dateTime);
    }

    public long countByUser(User user) {
        return todoRepository.countByUser(user);
    }

    public long countByCompleted(boolean completed) {
        return todoRepository.countByCompleted(completed);
    }

    public Todo toggleCompletion(Long todoId) {
        Optional<Todo> todoOptional = todoRepository.findById(todoId);
        if (todoOptional.isPresent()) {
            Todo todo = todoOptional.get();
            Todo updatedTodo = new Todo.TodoBuilder()
                    .copy(todo)
                    .setCompleted(!todo.isCompleted())
                    .build();
            return todoRepository.save(updatedTodo);
        }
        return null;
    }
}