package com.assignment.vendor;

import static com.assignment.vendor.helper.Constants.DBNAME;
import static com.assignment.vendor.helper.Constants.DRIVER;
import static com.assignment.vendor.helper.Constants.PASSWORD;
import static com.assignment.vendor.helper.Constants.SQL_GET_ORDER_TYPES;
import static com.assignment.vendor.helper.Constants.URL;
import static com.assignment.vendor.helper.Constants.USERNAME;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.whenNew;
import static org.powermock.reflect.Whitebox.getInternalState;
import static org.powermock.reflect.Whitebox.setInternalState;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.assignment.vendor.dao.OrderType;
import com.assignment.vendor.helper.DBHelper;

/**
 * Test class for DBHelper. Not all methods are tested. For time being just
 * showing the understanding on PowerMockito testing framework.
 * 
 * @author Ashok Kumar P S (Emp ID: 048464)
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ DBHelper.class, Class.class, DriverManager.class })
public class DBHelperTest {

	private DBHelper instance;

	@Before
	public void init() {
		instance = DBHelper.getInstance();
	}

	@Test
	public void testSingleton() {
		assertEquals(instance, DBHelper.getInstance());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testInit() throws Exception {

		mockStatic(Class.class);
		mockStatic(DriverManager.class);

		Connection conn = mock(Connection.class);
		Statement stmt = mock(Statement.class);
		ResultSet rs = mock(ResultSet.class);

		PowerMockito.when(
				DriverManager.getConnection(URL + DBNAME, USERNAME, PASSWORD))
				.thenReturn(conn);
		when(conn.createStatement()).thenReturn(stmt);
		when(stmt.executeQuery(SQL_GET_ORDER_TYPES)).thenReturn(rs);
		when(rs.next()).thenReturn(true).thenReturn(false);

		OrderType orderType = mock(OrderType.class);
		int id = 1;
		String name = "type1";
		whenNew(OrderType.class).withArguments(id, name).thenReturn(orderType);
		when(rs.getInt(1)).thenReturn(id);
		when(rs.getString(2)).thenReturn(name);

		// Invoking the actual method.
		instance.init();

		// Verifying calls and states.
		verifyStatic();
		Class.forName(DRIVER);

		assertEquals(conn, getInternalState(instance, "conn"));
		List<OrderType> listOrderTypes = (List<OrderType>) getInternalState(
				instance, "listOrderTypes");
		assertEquals(1, listOrderTypes.size());
		assertEquals(orderType, listOrderTypes.get(0));
	}

	@Test
	public void testInit_SQLException() throws Exception {

		mockStatic(Class.class);
		mockStatic(DriverManager.class);

		PowerMockito.when(
				DriverManager.getConnection(URL + DBNAME, USERNAME, PASSWORD))
				.thenThrow(new SQLException());

		// Invoking the actual method.
		instance.init();

		// Verifying calls and states.
		verifyStatic();
		Class.forName(DRIVER);

		assertEquals(null, getInternalState(instance, "conn"));
		assertEquals(null, getInternalState(instance, "listOrderTypes"));
	}

	@Test
	public void testDeInit() throws SQLException {
		Connection conn = mock(Connection.class);
		setInternalState(instance, "conn", conn);

		// Invoking the actual method.
		instance.deInit();

		// Verifying calls and states.
		verify(conn).close();
		assertEquals(null, getInternalState(DBHelper.class, "instance"));
	}

	@After
	public void deInit() {
		setInternalState(DBHelper.class, "instance", (DBHelper) null);
	}
}
