package za.ac.cput.Domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Todo> todos;

    protected User() {}

    private User(UserBuilder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.password = builder.password;
        this.email = builder.email;
        this.todos = builder.todos;
    }

    // Getters
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public List<Todo> getTodos() { return todos; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email) { this.email = email; }
    public void setTodos(List<Todo> todos) { this.todos = todos; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email);
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", username='" + username + '\'' + ", email='" + email + '\'' + '}';
    }

    public static class UserBuilder {
        private Long id;
        private String username;
        private String password;
        private String email;
        private List<Todo> todos;

        public UserBuilder() {}
        public UserBuilder setId(Long id) { this.id = id; return this; }
        public UserBuilder setUsername(String username) { this.username = username; return this; }
        public UserBuilder setPassword(String password) { this.password = password; return this; }
        public UserBuilder setEmail(String email) { this.email = email; return this; }
        public UserBuilder setTodos(List<Todo> todos) { this.todos = todos; return this; }

        public UserBuilder copy(User user) {
            this.id = user.id;
            this.username = user.username;
            this.password = user.password;
            this.email = user.email;
            this.todos = user.todos;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}