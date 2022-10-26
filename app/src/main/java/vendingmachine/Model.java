package vendingmachine;

import java.util.ArrayList;
import java.util.HashMap;

public class Model {

	private JDBC jdbc;

	private User currentUser;
	private Double totalAmout;

	private HashMap<Product, Integer> recentProducts;

	private ProductType listedType;
	private HashMap<Product, Integer> listedProducts;

	private HashMap<Product, Integer> selectedProducts;

	public Model(JDBC jdbc) {

		this.jdbc = jdbc;

		this.currentUser = null;
		this.totalAmout = null;

		this.recentProducts = new HashMap<Product, Integer>();

		this.listedType = null;

		this.listedProducts = new HashMap<Product, Integer>();

		this.selectedProducts = new HashMap<Product, Integer>();
	}

	public User getCurrentUser() {
		return this.currentUser;
	}

	public HashMap<Product, Integer> getListedProducts() {
		return this.listedProducts;
	}

	public ProductType getListedType() {
		return this.listedType;
	}

	public HashMap<Product, Integer> getRecentProducts() {
		return this.recentProducts;
	}

	public HashMap<Product, Integer> getSelectedProducts() {
		return this.selectedProducts;
	}

	public Double getTotalAmount() {
		return this.totalAmout;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public void setJDBC(JDBC jdbc) {
		this.jdbc = jdbc;
	}

	public void setListedProducts(HashMap<Product, Integer> listedProducts) {
		this.listedProducts = listedProducts;
	}

	public void setListedType(ProductType listedType) {
		this.listedType = listedType;
	}

	public void setRecentProducts(HashMap<Product, Integer> recentProducts) {
		this.recentProducts = recentProducts;
	}

	public void setSelectedProducts(HashMap<Product, Integer> selectedProducts) {
		this.selectedProducts = selectedProducts;
	}

}
