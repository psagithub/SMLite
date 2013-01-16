package com.assignment.vendor.dao;

import org.sakaiproject.entitybus.entityprovider.annotations.EntityId;

/**
 * Bean to store the vendor details.
 * 
 * @author Ashok Kumar P S (Emp ID: 048464)
 */
public class Vendor {

	@EntityId
	private int id;

	private String name;

	private boolean purchaseOrderAvailable;

	private String purchaseNumber;

	private int orderType;

	public Vendor() {
	}

	public Vendor(int id, String name, boolean purchaseOrderAvailable,
			String purchaseNumber, int orderType) {
		this.id = id;
		this.name = name;
		this.purchaseOrderAvailable = purchaseOrderAvailable;
		this.purchaseNumber = purchaseNumber;
		this.orderType = orderType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isPurchaseOrderAvailable() {
		return purchaseOrderAvailable;
	}

	public void setPurchaseOrderAvailable(boolean purchaseOrderAvailable) {
		this.purchaseOrderAvailable = purchaseOrderAvailable;
	}

	public String getPurchaseNumber() {
		return purchaseNumber;
	}

	public void setPurchaseNumber(String purchaseNumber) {
		this.purchaseNumber = purchaseNumber;
	}

	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	public String toString() {
		return "Vendor [id=" + id + ", name=" + name
				+ ", purchaseOrderAvailable=" + purchaseOrderAvailable
				+ ", purchaseNumber=" + purchaseNumber + ", purchaseType="
				+ orderType + "]";
	}
}
