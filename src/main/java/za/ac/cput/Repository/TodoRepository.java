package za.ac.cput.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.ac.cput.Domain.Todo;
import za.ac.cput.Domain.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
 List<Todo> findByUserId(Long userId);
 List<Todo> findByUser(User user); // Explicitly declared
 List<Todo> findByUserIdAndCompleted(Long userId, boolean completed);
 long countByCompleted(boolean completed);
 long countByUser(User user);
 List<Todo> findByUpdatedAtAfter(LocalDateTime dateTime);
 List<Todo> findByCreatedAtAfter(LocalDateTime dateTime);
 List<Todo> findByCompletedAndUser(boolean completed, User user);
 List<Todo> findByTitleAndUser(String title, User user);
 List<Todo> findByCompleted(boolean completed);
 List<Todo> findByTitle(String title);
}