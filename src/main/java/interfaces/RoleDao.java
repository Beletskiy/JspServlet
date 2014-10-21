package interfaces;

import entity.Role;

public interface RoleDao {
    void create(Role role) throws Exception;
    void update(Role role) throws Exception;
    void remove(Role role) throws Exception;
    Role findByName(String name) throws Exception;
}
