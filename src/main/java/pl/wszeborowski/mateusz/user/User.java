package pl.wszeborowski.mateusz.user;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NamedQuery(name = User.Queries.FIND_ALL, query = "select user from User user")
@NamedQuery(name = User.Queries.FIND_BY_LOGIN, query = "select user from User user where user" +
        ".login = :login")
public class User implements Serializable {

    public static class Roles {
        public static final String ADMIN = "ADMIN";

        public static final String USER = "USER";

        public static final List<String> ROLES = Arrays.asList(ADMIN, USER);
    }

    public User(User user) {
        this.id = user.id;
        this.login = user.login;
        this.password = user.password;
        this.roles = new ArrayList<>(user.roles);
    }

    public static class Queries {
        static final String FIND_ALL = "User.findAll";
        static final String FIND_BY_LOGIN = "User.findByLogin";
    }

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Getter
    @Setter
    @Column(nullable = false, unique = true)
    @NotBlank
    private String login;

    @Getter
    @Setter
    @NotBlank
    private String password;

    @Getter
    @Setter
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "users_roles", joinColumns = @JoinColumn(name = "user"))
    @Column(name = "role")
    @Singular
    private List<String> roles;
}
