package za.ac.cput.Domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "todos")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private Boolean completed = false; // CHANGED TO WRAPPER CLASS FOR SAFE PARTIAL UPDATES

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    protected Todo() {}

    private Todo(TodoBuilder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.description = builder.description;
        this.completed = builder.completed != null ? builder.completed : false;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
        this.user = builder.user;
    }

    // Getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Boolean getCompleted() { return completed; }
    public boolean isCompleted() { return completed != null && completed; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public User getUser() { return user; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setCompleted(Boolean completed) { this.completed = completed; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public void setUser(User user) { this.user = user; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Todo todo = (Todo) o;
        return Objects.equals(id, todo.id) && Objects.equals(title, todo.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }

    @Override
    public String toString() {
        return "Todo{" + "id=" + id + ", title='" + title + '\'' + ", completed=" + completed + '}';
    }

    public static class TodoBuilder {
        private Long id;
        private String title;
        private String description;
        private Boolean completed;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private User user;

        public TodoBuilder() {}
        public TodoBuilder setId(Long id) { this.id = id; return this; }
        public TodoBuilder setTitle(String title) { this.title = title; return this; }
        public TodoBuilder setDescription(String description) { this.description = description; return this; }
        public TodoBuilder setCompleted(Boolean completed) { this.completed = completed; return this; }
        public TodoBuilder setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public TodoBuilder setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        public TodoBuilder setUser(User user) { this.user = user; return this; }

        public TodoBuilder copy(Todo todo) {
            this.id = todo.id;
            this.title = todo.title;
            this.description = todo.description;
            this.completed = todo.completed;
            this.createdAt = todo.createdAt;
            this.updatedAt = todo.updatedAt;
            this.user = todo.user;
            return this;
        }

        public Todo build() {
            return new Todo(this);
        }
    }
}