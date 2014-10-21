package dao.impl;

import dao.impl.JdbcRoleDao;
import dao.impl.JdbcUserDao;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;

public class JdbcDaoTestUtils {

	private String propertiesFile = "h2Config.properties";
	private JdbcUserDao userDao = new JdbcUserDao();
	private JdbcRoleDao roleDao = new JdbcRoleDao();

	JdbcDaoTestUtils() throws Exception {
		Properties properties = new Properties();
		try {
			InputStream inputStream = getClass().getClassLoader()
					.getResourceAsStream(propertiesFile);
			properties.load(inputStream);
		} catch (IOException e) {
			throw new Exception("Something wrong with "
					+ "properties to DB connect.", e);
		}

		String driver = properties.getProperty("h2_driver");
		String url = properties.getProperty("h2_url");
		String login = properties.getProperty("h2_user");
		String password = properties.getProperty("h2_password");

		System.setProperty(
				PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, driver);
		System.setProperty(
				PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, url);
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME,
				login);
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD,
				password);
	}

	public JdbcUserDao getUserDao() {

		return userDao;
	}

	public JdbcRoleDao getRoleDao() {

		return roleDao;
	}

	public void createDB(Connection connection) throws Exception {
		final String CREATE_TABLES = "DROP TABLE IF EXISTS USER; "
				+ "DROP TABLE IF EXISTS ROLE; "
				+ "CREATE TABLE IF NOT EXISTS ROLE" + "( "
				+ "    ID BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL, "
				+ "    NAME VARCHAR(50) NOT NULL UNIQUE " + "); "
				+ "CREATE TABLE IF NOT EXISTS USER " + "( "
				+ "    ID BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL, "
				+ "    ROLE_ID BIGINT NOT NULL, "
				+ "    LOGIN VARCHAR_IGNORECASE(50) NOT NULL UNIQUE , "
				+ "    PASSWORD VARCHAR(50) NOT NULL, "
				+ "    EMAIL VARCHAR(50) NOT NULL UNIQUE , "
				+ "    FIRST_NAME VARCHAR(50), "
				+ "    LAST_NAME VARCHAR(50), " + "    BIRTHDAY DATE, "
				+ "    FOREIGN KEY (ROLE_ID) REFERENCES ROLE (ID) " + ");";

		Statement st = connection.createStatement();
		st.execute(CREATE_TABLES);
	}

	public void deleteDB(Connection connection) throws Exception {
		Statement st = connection.createStatement();
		st.execute("DELETE FROM user; " + "DELETE FROM role;");
	}
}
