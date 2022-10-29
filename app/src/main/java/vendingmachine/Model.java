package vendingmachine;

import java.util.ArrayList;
import java.util.HashMap;

public class Model {

	private JDBC jdbc;

	private User currentUser;
	private Double totalPrice;

	private HashMap<Product, Integer> recentProducts;
	private HashMap<Product, Integer> groupedProducts;
	private HashMap<Product, Integer> selectedProducts;

	public Model(JDBC jdbc) {

		this.jdbc = jdbc;

		this.currentUser = new User();
		this.totalPrice = 0.0;

		this.recentProducts = new HashMap<Product, Integer>();
		this.groupedProducts = new HashMap<Product, Integer>();
		this.selectedProducts = new HashMap<Product, Integer>();
		
		for (Product p: this.jdbc.getRecent()) {
			Product newProduct = new Product(p.getId(), p.getType(), p.getName(), p.getPrice(), p.getAmount());
			this.recentProducts.put(newProduct, 0);
		}

		for (Product p: this.jdbc.getProductsByType(ProductType.DRINK)) {
			Product newProduct = new Product(p.getId(), p.getType(), p.getName(), p.getPrice(), p.getAmount());
			this.groupedProducts.put(newProduct, 0);
		}
		for (Product gp: this.groupedProducts.keySet()) {
			for (Product sp: this.selectedProducts.keySet()) {
				if (gp.getName().equals(sp.getName())) {
					this.groupedProducts.put(gp, this.selectedProducts.get(sp));
					System.out.println(gp.getName() + ": " + this.groupedProducts.get(gp));
				}
			}
		}
	}

	public Boolean ifHasUser(String userName) {
		return this.jdbc.ifHasUser(userName);
	}

	public Boolean ifMatchUser(String userName, String password) {
		return this.jdbc.ifMatchUser(userName, password);
	}

	public User getCurrentUser() {
		return this.currentUser;
	}

	public HashMap<Product, Integer> getGroupedProducts() {
		return this.groupedProducts;
	}

	public JDBC getJDBC() {
		return this.jdbc;
	}

	public Double getPrice(String productName) {
		return this.jdbc.getProduct(productName).getPrice();
	}

	public ArrayList<Product> getProductsByType(ProductType type) {
		return this.jdbc.getProductsByType(type);
	}

	public HashMap<Product, Integer> getRecentProducts() {
		return this.recentProducts;
	}

	public HashMap<Product, Integer> getSelectedProducts() {
		return this.selectedProducts;
	}

	public Double getTotalPrice() {
		return this.totalPrice;
	}

	public void setCurrentUser(String userName) {
		User user = this.jdbc.getUser(userName);
		this.currentUser = user;
	}

	public void setGroupedProducts(HashMap<Product, Integer> groupedProducts) {
		this.groupedProducts = groupedProducts;
	}

	public void setJDBC(JDBC jdbc) {
		this.jdbc = jdbc;
	}

	public void setRecentProducts(HashMap<Product, Integer> recentProducts) {
		this.recentProducts = recentProducts;
	}

	public void setSelectedProducts(HashMap<Product, Integer> selectedProducts) {
		this.selectedProducts = selectedProducts;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
}
