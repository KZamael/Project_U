package app.web.application;

import java.util.List;
import java.util.stream.Collectors;
import app.business.entities.User;

import com.google.common.collect.ImmutableList;

public class UserDao {
    private final List<User> users = ImmutableList.of(
            //        Username    Salt for hash                    Hashed password (the password is "password" for all users)
            new User("perwendel", "$2a$10$h.dl5J86rGH7I8bD9bZeZe", "$2a$10$h.dl5J86rGH7I8bD9bZeZeci0pDt0.VwFTGujlnEaZXPf/q7vM5wO", "Steve", "Builder"),
            new User("davidase", "$2a$10$e0MYzXyjpJS7Pd0RVvHwHe", "$2a$10$e0MYzXyjpJS7Pd0RVvHwHe1HlCS4bZJ18JuywdEMLT83E1KDmUhCy","Sonic", "Hedgehog"),
            new User("federico", "$2a$10$E3DgchtVry3qlYlzJCsyxe", "$2a$10$E3DgchtVry3qlYlzJCsyxeSK0fftK4v0ynetVCuDdxGVl1obL.ln2", "Ash", "Ketchum")
    );

    public User getUserByUsername(String username) {
        return users.stream().filter(b -> b.userName.equals(username)).findFirst().orElse(null);
    }

    public Iterable<String> getAllUserNames() {
        return users.stream().map(user -> user.userName).collect(Collectors.toList());
    }
}
