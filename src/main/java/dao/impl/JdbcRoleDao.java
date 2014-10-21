package dao.impl;

import entity.Role;
import interfaces.RoleDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcRoleDao extends AbstractJdbcDao implements RoleDao {

	private static final String SQL_INSERT_ROLE = "INSERT INTO role(id, name) "
			+ "VALUES(?, ?);";

	private static final String SQL_UPDATE_ROLE = "UPDATE role "
			+ "SET name = ? " + "WHERE id = ?;";

	private static final String SQL_DELETE_ROLE_BY_ID = "DELETE "
			+ "FROM role " + "WHERE id = ?;";

	private static final String SQL_SELECT_ROLE_BY_NAME = "SELECT id, name "
			+ "FROM role " + "WHERE name = ?;";

	private static final String SQL_CHECK_USERS_BY_ROLE = "SELECT * "
			+ "FROM user " + "WHERE role_id = ?;";

	private Log logger = LogFactory.getLog(JdbcRoleDao.class.getName());

	public JdbcRoleDao() {
	}
	@Override
	public void create(Role role) throws Exception {
		if (role == null) {
			throw new Exception("Role can't be null.");
		}
		Connection connection = createConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			if (findByName(role.getName()) != null) {
				throw new Exception("DB already has such " + "role name: "
						+ role.getName());
			}
			preparedStatement = connection.prepareStatement(SQL_INSERT_ROLE);
			preparedStatement.setLong(1, role.getId());
			preparedStatement.setString(2, role.getName());
			preparedStatement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			rollbackQuietly(connection);
			String message = "Can't execute request = '" + SQL_INSERT_ROLE
					+ "' in create(Role) method.";
			logger.error(message, e);
			throw new Exception(message, e);
		} finally {
			closeQuietly(resultSet);
			closeQuietly(preparedStatement);
			closeQuietly(connection);
		}
	}
	@Override
	public void update(Role role) throws Exception {
		if (role == null) {
			throw new Exception("Role cannot be null.");
		}
		Connection connection = createConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			if (findByName(role.getName()) != null) {
				throw new Exception("DB already has such " + "role name: "
						+ role.getName());
			}
			preparedStatement = connection.prepareStatement(SQL_UPDATE_ROLE);
			preparedStatement.setString(1, role.getName());
			preparedStatement.setLong(2, role.getId());
			preparedStatement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			rollbackQuietly(connection);
			String message = "Can't execute request = '" + SQL_UPDATE_ROLE
					+ "' in update(Role) method.";
			logger.error(message, e);
			throw new Exception(message, e);
		} finally {
			closeQuietly(resultSet);
			closeQuietly(preparedStatement);
			closeQuietly(connection);
		}
	}

	@Override
	public void remove(Role role) throws Exception {
		if (role == null) {
			throw new Exception("Role can't be null.");
		}

		Connection connection = createConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			if (!checkUsersRole(connection, role)) {
				throw new Exception("Can't remove role " + role.getName()
						+ ", because [user] table has users "
						+ "with such role.");
			}
			preparedStatement = connection
					.prepareStatement(SQL_DELETE_ROLE_BY_ID);
			preparedStatement.setLong(1, role.getId());
			preparedStatement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			rollbackQuietly(connection);
			String message = "Can't execute request = '"
					+ SQL_DELETE_ROLE_BY_ID + "' in remove(Role) method.";
			logger.error(message, e);
			throw new Exception(message, e);
		} finally {
			closeQuietly(resultSet);
			closeQuietly(preparedStatement);
			closeQuietly(connection);
		}
	}
	@Override
	public Role findByName(String name) throws Exception {
		Connection connection = createConnection();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			preparedStatement = connection
					.prepareStatement(SQL_SELECT_ROLE_BY_NAME);
			preparedStatement.setString(1, name);
			resultSet = preparedStatement.executeQuery();
			Role role = null;
			if (resultSet.next()) {
				long id = resultSet.getLong("id");
				role = new Role(id, name);
			}
			connection.commit();
			return role;
		} catch (SQLException e) {
			rollbackQuietly(connection);
			String message = "Can't execute request = '"
					+ SQL_SELECT_ROLE_BY_NAME + "' in findByName() method.";
			logger.error(message, e);
			throw new Exception(message, e);
		} finally {
			closeQuietly(resultSet);
			closeQuietly(preparedStatement);
			closeQuietly(connection);
		}
	}

	private boolean checkUsersRole(Connection connection, Role role)
			throws Exception {

		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement(SQL_CHECK_USERS_BY_ROLE);
			preparedStatement.setLong(1, role.getId());
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return false;
			}
			return true;
		} catch (SQLException e) {
			String message = "Can't execute request = '"
					+ SQL_CHECK_USERS_BY_ROLE + "' in checkUsersRole() method";
			logger.error(message, e);
			throw new Exception(message, e);
		}
	}
}