package interfaces;

import java.util.List;
import entity.User;

public interface UserDao {
    void create(User user) throws Exception;
    void update(User user) throws Exception;
    void remove(User user) throws Exception;
    List<User> findAll() throws Exception;
    User findByLogin (String login) throws Exception;
    User findByEmail (String email) throws Exception;
}
