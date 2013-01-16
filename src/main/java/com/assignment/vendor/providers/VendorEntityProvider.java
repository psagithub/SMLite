package com.assignment.vendor.providers;

import static com.assignment.vendor.helper.Constants.ENTITY_PREFIX_VENDOR;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.sakaiproject.entitybus.EntityReference;
import org.sakaiproject.entitybus.entityprovider.CoreEntityProvider;
import org.sakaiproject.entitybus.entityprovider.EntityProviderManager;
import org.sakaiproject.entitybus.entityprovider.capabilities.RESTful;
import org.sakaiproject.entitybus.entityprovider.extension.Formats;
import org.sakaiproject.entitybus.entityprovider.search.Search;
import org.sakaiproject.entitybus.exception.EntityException;
import org.sakaiproject.entitybus.exception.EntityNotFoundException;
import org.sakaiproject.entitybus.util.AbstractAutoRegisteringProvider;

import com.assignment.vendor.dao.Vendor;
import com.assignment.vendor.helper.DBHelper;

/**
 * Defines RESTful services for Vendor CRUD operation.
 * 
 * @author Ashok Kumar P S (Emp ID: 048464)
 */
public class VendorEntityProvider extends AbstractAutoRegisteringProvider
		implements CoreEntityProvider, RESTful {

	public VendorEntityProvider(EntityProviderManager entityProviderManager) {
		super(entityProviderManager);
	}

	@Override
	public String getEntityPrefix() {
		return ENTITY_PREFIX_VENDOR;
	}

	@Override
	public Object getSampleEntity() {
		return new Vendor();
	}

	@Override
	public Object getEntity(EntityReference ref) {
		Vendor vendor = null;
		String msg = null;
		try {
			vendor = DBHelper.getInstance().getVendor(
					Integer.parseInt(ref.getId()));
			if (vendor == null) {
				msg = "Couldn't find the vendor.";
			}
		} catch (NumberFormatException e) {
			msg = "Error parsing the vendor id.";
		} catch (SQLException e) {
			e.printStackTrace();
			msg = "Error while retrieving the vendor.";
		}
		if (vendor == null) {
			System.err.println(msg);
			throw new EntityNotFoundException(msg, ref.getId());
		}
		return vendor;
	}

	@Override
	public List<?> getEntities(EntityReference ref, Search search) {
		List<Vendor> vendors = null;
		try {
			vendors = DBHelper.getInstance().getVendors();
			return vendors;
		} catch (SQLException e) {
			String msg = "Error while retrieving vendors.";
			System.err.println(msg);
			throw new EntityException(msg, ref.getId());
		}
	}

	@Override
	public String createEntity(EntityReference ref, Object entity,
			Map<String, Object> params) {
		Vendor vendor = (Vendor) entity;
		DBHelper dbHelper = DBHelper.getInstance();
		if (!vendor.isPurchaseOrderAvailable()) {
			vendor.setPurchaseNumber("");
			vendor.setOrderType(dbHelper.getOderTypes().get(0).getId());
		}
		try {
			return String.valueOf(dbHelper.addVendor((Vendor) entity));
		} catch (SQLException e) {
			String msg = "Error while creating the vendor; " + vendor;
			System.err.println(msg);
			throw new EntityException(msg, ref.getId());
		}
	}

	@Override
	public void updateEntity(EntityReference ref, Object entity,
			Map<String, Object> params) {
		Vendor vendor = (Vendor) entity;
		DBHelper dbHelper = DBHelper.getInstance();
		if (!vendor.isPurchaseOrderAvailable()) {
			vendor.setPurchaseNumber("");
			vendor.setOrderType(dbHelper.getOderTypes().get(0).getId());
		}
		try {
			DBHelper.getInstance().updateVendor(vendor);
		} catch (SQLException e) {
			String msg = "Error while updating the vendor; " + vendor;
			System.err.println(msg);
			throw new EntityException(msg, ref.getId());
		}
	}

	@Override
	public void deleteEntity(EntityReference ref, Map<String, Object> params) {
		String msg = null;
		try {
			DBHelper.getInstance().deleteVendor(Integer.valueOf(ref.getId()));
		} catch (NumberFormatException e) {
			msg = "Error parsing the vendor id.";
		} catch (SQLException e) {
			msg = "Error while deleting the vendor; ID: " + ref.getId();
		}
		if (msg != null) {
			System.err.println(msg);
			throw new EntityException(msg, ref.getId());
		}
	}

	@Override
	public String[] getHandledOutputFormats() {
		return new String[] { Formats.HTML, Formats.JSON, Formats.FORM };
	}

	@Override
	public String[] getHandledInputFormats() {
		return new String[] { Formats.HTML, Formats.JSON, Formats.FORM };
	}

	@Override
	public boolean entityExists(String id) {
		try {
			return DBHelper.getInstance().getVendor(Integer.valueOf(id)) != null;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
