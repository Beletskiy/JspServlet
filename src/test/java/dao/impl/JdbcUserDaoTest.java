package dao.impl;

import org.dbunit.Assertion;
import org.dbunit.DBTestCase;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Test;
import entity.Role;
import entity.User;
import java.sql.Connection;
import java.util.Calendar;

public class JdbcUserDaoTest extends DBTestCase {

	private JdbcDaoTestUtils testUtils;
	private Role adminRole;
	private Role userRole;
	private Role moderatorRole;
	private User user1;
	private User user2;
	private User user3;
	private User userSameLogin;
	private User userSameEmail;
	private User updatedUser;
	private User newUser;
	
	public JdbcUserDaoTest(String name) throws Exception {
		super(name);
		testUtils = new JdbcDaoTestUtils();
	}

	@Override
	protected IDataSet getDataSet() throws Exception {

		return new FlatXmlDataSetBuilder().build(getClass().getResource(
				"/UserRoleDataSet.xml"));
	}

	@Override
	public void setUp() throws Exception {

		userRole = new Role(1, "user");
		adminRole = new Role(2, "admin");
		moderatorRole = new Role(3, "moderator");

		Calendar calendar = Calendar.getInstance();
		calendar.set(1980, Calendar.JANUARY, 1);
		user1 = new User(userRole, "user", "user", "user@gmail.com", "Ivan",
				"Ivanov", calendar.getTime());

		updatedUser = new User(userRole, "user", "super_user",
				"super_user@gmail.com", "Ivan", "Ivanovich", calendar.getTime());

		userSameLogin = new User(userRole, "user", "anybody",
				"anybody@gmail.com", "Ivan", "Ivanov", calendar.getTime());

		userSameEmail = new User(userRole, "anybody", "anybody",
				"user@gmail.com", "Ivan", "Ivanov", calendar.getTime());

		calendar.set(1970, Calendar.JUNE, 6);
		user2 = new User(adminRole, "admin", "admin", "admin@gmail.com",
				"Alexander", "Alexandrov", calendar.getTime());

		calendar.set(1980, Calendar.OCTOBER, 20);
		user3 = new User(moderatorRole, "moderator", "moderator",
				"moderator@gmail.com", "Maxim", "Mirniy", calendar.getTime());
		calendar.set(1990, Calendar.OCTOBER, 20);
		newUser = new User(userRole, "usernew", "usernew",
				"new_user@gmail.com", "Nikolay", "Novak", calendar.getTime());

		Connection connection = getConnection().getConnection();
		testUtils.createDB(connection);
		testUtils.getRoleDao().create(userRole);
		testUtils.getRoleDao().create(adminRole);
		testUtils.getRoleDao().create(moderatorRole);
		testUtils.getUserDao().create(user1);
		testUtils.getUserDao().create(user2);
		testUtils.getUserDao().create(user3);
	}

	@Override
	public void tearDown() throws Exception {
		Connection connection = getConnection().getConnection();
		testUtils.deleteDB(connection);
	}

	@Test
	public void testCreate() throws Exception {
		
		testUtils.getUserDao().create(newUser);
		IDataSet databaseDataSet = getConnection().createDataSet();
		ITable actualTable = databaseDataSet.getTable("user");
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(getClass()
				.getResource("/AnotherUserCreated.xml"));
		ITable expectedTable = expectedDataSet.getTable("user");

		Assertion.assertEquals(expectedTable, actualTable);
	}

	@Test
	public void testCreateWrongLogin() throws Exception {

		try {
			testUtils.getUserDao().create(userSameLogin);
			fail("No exception was thrown by trying to create user that "
					+ "has existing login");
		} catch (Exception e) {
			IDataSet databaseDataSet = getConnection().createDataSet();
			ITable actualTable = databaseDataSet.getTable("user");
			IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
					.build(getClass().getResource("/UserRoleDataSet.xml"));
			ITable expectedTable = expectedDataSet.getTable("user");

			Assertion.assertEquals(expectedTable, actualTable);
		}
	}

	@Test
	public void testCreateWrongEmail() throws Exception {

		try {
			testUtils.getUserDao().create(userSameEmail);
			fail("No exception was thrown by trying to create user that "
					+ "has existing email");
		} catch (Exception e) {
			IDataSet databaseDataSet = getConnection().createDataSet();
			ITable actualTable = databaseDataSet.getTable("user");
			IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
					.build(getClass().getResource("/UserRoleDataSet.xml"));
			ITable expectedTable = expectedDataSet.getTable("user");

			Assertion.assertEquals(expectedTable, actualTable);
		}
	}
	@Test
	public void testRemoveUser() throws Exception {

		testUtils.getUserDao().remove(user2);

		IDataSet databaseDataSet = getConnection().createDataSet();
		ITable actualTable = databaseDataSet.getTable("user");
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(getClass()
				.getResource("/RemoveOneUser.xml"));
		ITable expectedTable = expectedDataSet.getTable("user");

		Assertion.assertEquals(expectedTable, actualTable);
	}

	@Test
	public void testUpdateUser() throws Exception {

		testUtils.getUserDao().update(updatedUser);

		IDataSet databaseDataSet = getConnection().createDataSet();
		ITable actualTable = databaseDataSet.getTable("user");
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(getClass()
				.getResource("/UpdatedUser.xml"));
		ITable expectedTable = expectedDataSet.getTable("user");

		Assertion.assertEquals(expectedTable, actualTable);
	}

	@Test
	public void testFindAll() throws Exception {

		int numbersOfUsersActual = testUtils.getUserDao().findAll().size();

		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(getClass()
				.getResource("/UserRoleDataSet.xml"));
		int numbersOfUsersExpected = expectedDataSet.getTable("user")
				.getRowCount();

		assertTrue(
				"Number of expected and actual users from tables does not match ",
				(numbersOfUsersActual == numbersOfUsersExpected));
	}

	@Test
	public void testFindUserByLogin() throws Exception {

		User expectedUser = user1;
		User actualUser = testUtils.getUserDao().findByLogin(user1.getLogin());

		assertEquals(expectedUser.getLogin(), actualUser.getLogin());
	}

	@Test
	public void testFindUserByEmail() throws Exception {

		User expectedUser = user2;
		User actualUser = testUtils.getUserDao().findByEmail(user2.getEmail());

		assertEquals(expectedUser.getEmail(), actualUser.getEmail());
	}
}