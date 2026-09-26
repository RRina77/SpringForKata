package web.dao;

import web.model.User;

import java.util.List;

public interface UserDao {
    List<User> getAllUsers();
    User getUserById(int id);
    void saveUser(User user);
    void updateUser(int id, User updatedUser);
    void deleteUser(int id);

}
