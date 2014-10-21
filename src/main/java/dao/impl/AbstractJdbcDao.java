package dao.impl;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public abstract class AbstractJdbcDao {

	private static PoolingDataSource<?> dataSource;
	
	private String propertiesFile = "h2Config.properties";
	private Log logger = LogFactory
			.getLog(AbstractJdbcDao.class.getName());
	
	public Connection createConnection() throws Exception {
		if (dataSource == null) {
			initializeDataSource();
		}

		try {
			Connection connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			return connection;
		} catch (SQLException e) {
			logger.error("Can't create connection.", e);
			throw new Exception("Can't create connection." , e);
		}
	}
	public void closeQuietly(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				logger.error("Can't close ResultSet ", e);
			}
		}
	}
	public void closeQuietly(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				logger.error("Can't close Statement ", e);
			}
		}
	}

	public void closeQuietly(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				logger.error("Can't close Connection ", e);
			}
		}
	}

	public void rollbackQuietly(Connection connection) {
		if (connection != null) {
			try {
				connection.rollback();
			} catch (SQLException e) {
				logger.error("Can't rollback ", e);
			}
		}
	}

	private void initializeDataSource() throws Exception {
		
		Properties properties = getProperties(propertiesFile);
		dataSource = getDataSource(properties);
	}
	private Properties getProperties(String propertiesFile) throws Exception {
		Properties properties = new Properties();
		try {
			InputStream inputStream = getClass().getClassLoader()
					.getResourceAsStream(propertiesFile);
			properties.load(inputStream);
			return properties;
		} catch (IOException e) {
			logger.error("Something wrong with properties of connection to DB ", e);
			throw new Exception("Something wrong with properties of connection to DB", e);				
		}
	}
	
	private PoolingDataSource<?> getDataSource(Properties properties) {

		String driver = properties.getProperty("h2_driver");
		String url = properties.getProperty("h2_url");
		String login = properties.getProperty("h2_user");
		String password = properties.getProperty("h2_password");

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			logger.error("Can't set driver to DB.", e);
			throw new IllegalArgumentException("Can't set driver to DB.", e);
		}

		ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(
				url, login, password);

		PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(
				connectionFactory, null);

		ObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<>(
				poolableConnectionFactory);

		poolableConnectionFactory.setPool(connectionPool);

		PoolingDataSource<PoolableConnection> dataSource = new PoolingDataSource<>(
				connectionPool);
		logger.trace("Connection pool created. ");
		return dataSource;

	}
}
