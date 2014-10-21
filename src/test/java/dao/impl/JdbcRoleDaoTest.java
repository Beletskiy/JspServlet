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

public class JdbcRoleDaoTest extends DBTestCase {

	private JdbcDaoTestUtils testUtils;
	private Role role1;
	private Role role2;
	private Role role3;
	private Role newRole;
	private Role updatedRoleGood;
	private Role updatedRoleWrong;
	private User user1;
	
	public JdbcRoleDaoTest(String name) throws Exception {
		super(name);
		testUtils = new JdbcDaoTestUtils();
	}

	protected IDataSet getDataSet() throws Exception {

		return new FlatXmlDataSetBuilder().build(getClass().getResource(
				"/UserRoleDataSet.xml"));
	}

	@Override
	public void setUp() throws Exception {

		role1 = new Role(1, "user");
		role2 = new Role(2, "admin");
		role3 = new Role(3, "moderator");
		newRole = new Role(4, "newUser");
		updatedRoleGood = new Role(1, "super_user");
		updatedRoleWrong = new Role(2, "admin");

		Calendar calendar = Calendar.getInstance();
		calendar.set(1980, Calendar.JANUARY, 1);
		user1 = new User(role1, "user", "user", "user@gmail.com", "Ivan",
				"Ivanov", calendar.getTime());

		Connection connection = getConnection().getConnection();
		testUtils.createDB(connection);
		testUtils.getRoleDao().create(role1);
		testUtils.getRoleDao().create(role2);
		testUtils.getRoleDao().create(role3);
	}

	@Override
	public void tearDown() throws Exception {
		Connection connection = getConnection().getConnection();
		testUtils.deleteDB(connection);
	}

	@Test
	public void testCreate() throws Exception {
		testUtils.getRoleDao().create(newRole);
		IDataSet databaseDataSet = getConnection().createDataSet();
		ITable actualTable = databaseDataSet.getTable("role");

		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(getClass()
				.getResource("/AnotherRoleCreated.xml"));

		ITable expectedTable = expectedDataSet.getTable("role");

		Assertion.assertEquals(expectedTable, actualTable);
	}

	@Test
	public void testCreateTwiceSameRole() throws Exception {
		
		try {
			testUtils.getRoleDao().create(role1);
			fail("No exception was thrown by trying to create two the same roles");
		} catch (Exception e) {
			IDataSet databaseDataSet = getConnection().createDataSet();
			ITable actualTable = databaseDataSet.getTable("role");
			IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
					.build(getClass().getResource("/UserRoleDataSet.xml"));
			ITable expectedTable = expectedDataSet.getTable("role");

			Assertion.assertEquals(expectedTable, actualTable);
		}
	}

	@Test
	public void testRemoveRoleGood() throws Exception {
		
		testUtils.getRoleDao().remove(role1);

		IDataSet databaseDataSet = getConnection().createDataSet();
		ITable actualTable = databaseDataSet.getTable("role");
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(getClass()
				.getResource("/RemoveOneRole.xml"));
		ITable expectedTable = expectedDataSet.getTable("role");

		Assertion.assertEquals(expectedTable, actualTable);
	}

	@Test
	public void testRemoveRoleWrong() throws Exception {
		
		testUtils.getUserDao().create(user1); // user1 has role1

		try {
			testUtils.getRoleDao().remove(role1);
			fail("No exception was thrown by trying to remove role that "
					+ "existing in [user] table");
		} catch (Exception e) {
			IDataSet databaseDataSet = getConnection().createDataSet();
			ITable actualTable = databaseDataSet.getTable("role");
			IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
					.build(getClass().getResource("/UserRoleDataSet.xml"));
			ITable expectedTable = expectedDataSet.getTable("role");

			Assertion.assertEquals(expectedTable, actualTable);
		}
	}

	@Test
	public void testUpdateGood() throws Exception {
	
		testUtils.getRoleDao().update(updatedRoleGood);
		IDataSet databaseDataSet = getConnection().createDataSet();
		ITable actualTable = databaseDataSet.getTable("role");
		IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(getClass()
				.getResource("/UpdatedRole.xml"));
		ITable expectedTable = expectedDataSet.getTable("role");

		Assertion.assertEquals(expectedTable, actualTable);
	}

	@Test
	public void testUpdateWrong() throws Exception {
		
		try {
			testUtils.getRoleDao().update(updatedRoleWrong);
			fail("No exception was thrown by trying to update role with "
					+ "name that already exists in the [role] table");
		} catch (Exception e) {
			IDataSet databaseDataSet = getConnection().createDataSet();
			ITable actualTable = databaseDataSet.getTable("role");
			IDataSet expectedDataSet = new FlatXmlDataSetBuilder()
					.build(getClass().getResource("/UserRoleDataSet.xml"));
			ITable expectedTable = expectedDataSet.getTable("role");

			Assertion.assertEquals(expectedTable, actualTable);
		}
	}

	@Test
	public void testFindByName() throws Exception {
	
		Role expectedRole = role3;
		Role actualRole = testUtils.getRoleDao().findByName(role3.getName());

		assertEquals(expectedRole, actualRole);
	}
}
