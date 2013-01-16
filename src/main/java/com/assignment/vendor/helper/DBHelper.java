package com.assignment.vendor.helper;

import static com.assignment.vendor.helper.Constants.DBNAME;
import static com.assignment.vendor.helper.Constants.DRIVER;
import static com.assignment.vendor.helper.Constants.PASSWORD;
import static com.assignment.vendor.helper.Constants.SQL_ADD_VENDOR;
import static com.assignment.vendor.helper.Constants.SQL_DELETE_VENDOR;
import static com.assignment.vendor.helper.Constants.SQL_GET_ORDER_TYPES;
import static com.assignment.vendor.helper.Constants.SQL_GET_VENDOR;
import static com.assignment.vendor.helper.Constants.SQL_GET_VENDORS;
import static com.assignment.vendor.helper.Constants.SQL_UPDATE_VENDOR;
import static com.assignment.vendor.helper.Constants.URL;
import static com.assignment.vendor.helper.Constants.USERNAME;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.assignment.vendor.dao.OrderType;
import com.assignment.vendor.dao.Vendor;

/**
 * Convenient helper class for DB CRUD operations.<br>
 * <br>
 * <ol>
 * <b><u>DB Details:</u></b>
 * <li>DB Name: vendor</li>
 * <li>Table1: vendor</li>
 * <li>Table2: order_type</li>
 * </ol>
 * 
 * <b><u>Note:</u></b> The DB schema is created using the sql file in this
 * project named "vendor.sql".<br>
 * <br>
 * 
 * @author Ashok Kumar P S (Emp ID: 048464)
 */
public class DBHelper {

	private Connection conn;

	private List<OrderType> listOrderTypes;

	private static DBHelper instance;

	private DBHelper() {
	}

	public static DBHelper getInstance() {
		if (instance == null) {
			instance = new DBHelper();
		}
		return instance;
	}

	public void init() throws ClassNotFoundException, SQLException {
		Class.forName(DRIVER);
		conn = DriverManager.getConnection(URL + DBNAME, USERNAME, PASSWORD);

		// Loading order types.
		ResultSet rs = conn.createStatement().executeQuery(SQL_GET_ORDER_TYPES);
		listOrderTypes = new ArrayList<OrderType>(4);
		while (rs.next()) {
			listOrderTypes.add(new OrderType(rs.getInt(1), rs.getString(2)));
		}
	}

	public void deInit() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		instance = null;
	}

	public Vendor getVendor(int id) throws SQLException {
		Vendor vendor = null;
		if (conn != null) {
			PreparedStatement stmt = conn.prepareStatement(SQL_GET_VENDOR);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.first()) {
				vendor = new Vendor();
				vendor.setId(rs.getInt(1));
				vendor.setName(rs.getString(2));
				vendor.setPurchaseOrderAvailable(rs.getBoolean(3));
				vendor.setPurchaseNumber(rs.getString(4));
				vendor.setOrderType(rs.getInt(5));
			}
		}
		return vendor;
	}

	public List<Vendor> getVendors() throws SQLException {
		List<Vendor> listVendors = new ArrayList<Vendor>(1);
		if (conn != null) {
			ResultSet rs = conn.createStatement().executeQuery(SQL_GET_VENDORS);
			while (rs.next()) {
				Vendor vendor = new Vendor();
				vendor.setId(rs.getInt(1));
				vendor.setName(rs.getString(2));
				vendor.setPurchaseOrderAvailable(rs.getBoolean(3));
				vendor.setPurchaseNumber(rs.getString(4));
				vendor.setOrderType(rs.getInt(5));
				listVendors.add(vendor);
			}
		}

		return listVendors;
	}

	public int addVendor(Vendor vendor) throws SQLException {
		if (conn != null) {
			PreparedStatement stmt = conn.prepareStatement(SQL_ADD_VENDOR,
					Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, vendor.getName());
			stmt.setInt(2, vendor.isPurchaseOrderAvailable() ? 1 : 0);
			stmt.setString(3, vendor.getPurchaseNumber());
			stmt.setInt(4, vendor.getOrderType());
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.first()) {
				return rs.getInt(1);
			}
		}
		return -1;
	}

	public int updateVendor(Vendor vendor) throws SQLException {
		if (conn != null) {
			PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE_VENDOR,
					Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, vendor.getName());
			stmt.setInt(2, vendor.isPurchaseOrderAvailable() ? 1 : 0);
			stmt.setString(3, vendor.getPurchaseNumber());
			stmt.setInt(4, vendor.getOrderType());
			stmt.setInt(5, vendor.getId());
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.first()) {
				return rs.getInt(1);
			}
		}
		return -1;
	}

	public void deleteVendor(int id) throws SQLException {
		if (conn != null) {
			PreparedStatement stmt = conn.prepareStatement(SQL_DELETE_VENDOR);
			stmt.setInt(1, id);
			stmt.executeUpdate();
		}
	}

	public List<OrderType> getOderTypes() {
		return listOrderTypes;
	}
}
