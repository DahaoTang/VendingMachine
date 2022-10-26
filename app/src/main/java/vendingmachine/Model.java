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

		this.currentUser = new User();
		this.totalAmout = 0.0;

		this.recentProducts = new HashMap<Product, Integer>();
		ArrayList<Product> recentProductsList = this.jdbc.getRecent();
		for (Product p: recentProductsList) {
			this.recentProducts.put(p, 0);
		}

		this.listedType = ProductType.ALL;

		this.listedProducts = new HashMap<Product, Integer>();
		ArrayList<Product> allProducts = this.jdbc.getProductsAll();
		for (Product p: allProducts) {
			this.listedProducts.put(p, 0);
		}

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
