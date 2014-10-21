package dao.impl;

import entity.Role;
import entity.User;
import interfaces.UserDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcUserDao extends AbstractJdbcDao implements UserDao {

    private static final String ALL_FIELDS =
            "user.id AS user_id, role_id, role.name AS role_name, login, " +
                    "password, email, first_name, last_name, birthday ";

    private static final String SQL_INSERT_USER =
            "INSERT INTO user(role_id, login, password, email, " +
                    "first_name, last_name, birthday ) " +
            "VALUES(?, ?, ?, ?, ?, ?, ?);";

    private static final String SQL_SELECT_ALL_USERS =
            "SELECT " + ALL_FIELDS +
            "FROM user JOIN role ON user.role_id = role.id;";

    private static final String SQL_SELECT_USER_BY_LOGIN =
            "SELECT " + ALL_FIELDS +
            "FROM user JOIN role ON user.role_id = role.id " +
            "WHERE login = ?;";

    private static final String SQL_SELECT_USER_BY_EMAIL =
            "SELECT " + ALL_FIELDS +
            "FROM user JOIN role ON user.role_id = role.id " +
            "WHERE email = ?;";

    private static final String SQL_DELETE_USER_BY_LOGIN =
            "DELETE FROM user WHERE login = ?;";

    private static final String SQL_UPDATE_USER =
            "UPDATE user " +
            "SET role_id = ?, password = ?, email = ?, " +
                    "first_name = ?, last_name = ?, birthday = ? " +
            "WHERE login = ?;";
    
    private Log logger = LogFactory.getLog(JdbcUserDao.class.getName());

    public JdbcUserDao() {
    	logger.trace("Create user. ");
    }
    @Override
    public void create(User user) throws Exception {
        if (user == null) {
            throw new Exception("User can't be null.");
        }
        Connection connection = createConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            if (findByLogin(user.getLogin()) != null) {
                logger.trace("DB already has a user with login " + user.getLogin());
                throw new Exception("DB already has a user with such login: " +
                        "with such login: " + user.getLogin());
            }
            if (findByEmail(user.getEmail()) != null) {
                logger.trace("DB already has a user with email " + user.getEmail());
                throw new Exception("DB already has a user " +
                        "with such email: " + user.getEmail());
            }
            preparedStatement = connection.prepareStatement(SQL_INSERT_USER);
            preparedStatement.setLong(1, user.getRole().getId());
            preparedStatement.setString(2, user.getLogin());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getFirstName());
            preparedStatement.setString(6, user.getLastName());
            preparedStatement.setDate(7,
                                    new Date(user.getBirthday().getTime()));
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            rollbackQuietly(connection);
            String message = "Can't execute request = '" + SQL_INSERT_USER +
                    "' in create(User) method.";
            logger.error(message, e);
            throw new Exception(message, e);
        } finally {
            closeQuietly(resultSet);
            closeQuietly(preparedStatement);
            closeQuietly(connection);
        }
    }
    @Override
    public void  update(User user) throws Exception {
        if (user == null) {
            throw new Exception("User can't be null.");
        }
        Connection connection = createConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
      
            User userWithSuchEmail = findByEmail(user.getEmail());
            if (userWithSuchEmail != null &&
                    !(userWithSuchEmail.getLogin().equals(user.getLogin()))) {
                logger.trace("DB already has a user with email " + user.getEmail());
                throw new Exception("DB already has a user " +
                        "with such email: " + user.getEmail());
            }
            preparedStatement = connection.prepareStatement(SQL_UPDATE_USER);
            preparedStatement.setLong(1, user.getRole().getId());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getFirstName());
            preparedStatement.setString(5, user.getLastName());
            preparedStatement.setDate(6,
                    new Date(user.getBirthday().getTime()));
            preparedStatement.setString(7, user.getLogin());
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            rollbackQuietly(connection);
            String message = "Can't execute request = '" + SQL_UPDATE_USER +
                    "' in update(User) method.";
            logger.error(message, e);
            throw new Exception(message, e);
        } finally {
            closeQuietly(resultSet);
            closeQuietly(preparedStatement);
            closeQuietly(connection);
        }
    }
    @Override
    public void remove(User user) throws Exception {
        if (user == null) {
            throw new Exception("User can't be null.");
        }
        Connection connection = createConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement
                    (SQL_DELETE_USER_BY_LOGIN);
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            rollbackQuietly(connection);
            String message = "Can't execute request = '" + SQL_DELETE_USER_BY_LOGIN +
                    "' in remove(User) method.";
            logger.error(message, e);
            throw new Exception(message, e);
        } finally {
            closeQuietly(resultSet);
            closeQuietly(preparedStatement);
            closeQuietly(connection);
        }
    }
    @Override
    public List<User> findAll() throws Exception {
        Connection connection = createConnection();
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(SQL_SELECT_ALL_USERS);
            List<User> result = new ArrayList<>();
            while (resultSet.next()) {
               // long userId = resultSet.getLong("user_id");
                long roleId = resultSet.getLong("role_id");
                String roleName = resultSet.getString("role_name");
                String login = resultSet.getString("login");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                Date birthday = resultSet.getDate("birthday");

                result.add(new User(new Role(roleId, roleName), login,
                        password, email, firstName, lastName, birthday));
            }
            connection.commit();
            return result;
        } catch (SQLException e) {
            rollbackQuietly(connection);
            String message = "Can't execute request = '" + SQL_SELECT_ALL_USERS +
                    "' in findAll() method.";
            logger.error(message, e);
            throw new Exception(message, e);
        } finally {
            closeQuietly(resultSet);
            closeQuietly(statement);
            closeQuietly(connection);
        }
    }

    @Override
    public User findByLogin(String login) throws Exception {
        return getUserByField(login, SQL_SELECT_USER_BY_LOGIN);
    }

    @Override
    public User findByEmail(String email) throws Exception {
        return getUserByField(email, SQL_SELECT_USER_BY_EMAIL);
    }

    private User getUserByField(String field, String sqlRequest) throws Exception {
        Connection connection = createConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement =  connection.prepareStatement(sqlRequest);
            preparedStatement.setString(1, field);
            resultSet = preparedStatement.executeQuery();
            User user = null;
            if (resultSet.next()) {
              //  long userId = resultSet.getLong("user_id");
                long roleId = resultSet.getLong("role_id");
                String login = resultSet.getString("login");
                String roleName = resultSet.getString("role_name");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                Date birthday = resultSet.getDate("birthday");

                user = new User(new Role(roleId, roleName), login,
                        password, email, firstName, lastName, birthday);
            }
            connection.commit();
            return user;
        } catch (SQLException e) {
            rollbackQuietly(connection);
            String message = "Can't execute request = '" + sqlRequest + "'.";
            logger.error(message, e);
            throw new Exception(message, e);
        } finally {
            closeQuietly(resultSet);
            closeQuietly(preparedStatement);
            closeQuietly(connection);
        }
    }
}
