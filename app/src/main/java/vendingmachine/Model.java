package vendingmachine;

import java.util.ArrayList;
import java.util.HashMap;

public class Model {

	private JDBC jdbc;

	private User currentUser;
	private Double totalAmout;

	private HashMap<Product, Integer> recentProducts;

	private HashMap<Product, Integer> groupedProducts;

	private HashMap<Product, Integer> selectedProducts;

	public Model(JDBC jdbc) {

		this.jdbc = jdbc;

		this.currentUser = new User();
		this.totalAmout = 0.0;

		this.recentProducts = new HashMap<Product, Integer>();

		this.groupedProducts = new HashMap<Product, Integer>();
		for (Product p: this.jdbc.getProductsByType(ProductType.DRINK)) {
			this.groupedProducts.put(p, 0);
		}

		this.selectedProducts = new HashMap<Product, Integer>();
	}

	public User getCurrentUser() {
		return this.currentUser;
	}

	public HashMap<Product, Integer> getRecentProducts() {
		return this.recentProducts;
	}

	public HashMap<Product, Integer> getGroupedProducts() {
		return this.groupedProducts;
	}

	public ArrayList<Product> getProductsByType(ProductType type) {
		return this.jdbc.getProductsByType(type);
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

	public void setRecentProducts(HashMap<Product, Integer> recentProducts) {
		this.recentProducts = recentProducts;
	}

	public void setGroupedProducts(HashMap<Product, Integer> groupedProducts) {
		this.groupedProducts = groupedProducts;
	}

	public void setSelectedProducts(HashMap<Product, Integer> selectedProducts) {
		this.selectedProducts = selectedProducts;
	}

	public void updateGrouped(ProductType type) {
		System.out.println("MODEL: update group");
		this.groupedProducts = new HashMap<Product, Integer>();
		for (Product p: this.jdbc.getProductsByType(type)) {
			this.groupedProducts.put(p, 0);
		}
		for (Product p1: this.groupedProducts.keySet()) {
			for (Product p2: this.selectedProducts.keySet()) {
				if (p1.getName().equals(p2.getName())) {
					this.groupedProducts.put(p1, this.selectedProducts.get(p2));
				}
			}
		}
	}

	public void updateGroupedAmount(String productName, Integer newAmount) {
		System.out.println("MODEL: ");
		// Update grouped products amounts
		for (Product p: this.groupedProducts.keySet()) {
			if (p.getName().equals(productName)) {
				this.groupedProducts.put(p, newAmount);
				if (newAmount == 0) {
					this.selectedProducts.remove(p);
				} else {
					this.selectedProducts.put(p, newAmount);
				}
				System.out.println(p.getName() + " " + newAmount);
			}
		}
	}

	public void updateRecent() {
		System.out.println("MODEL: update recent");
		this.recentProducts = new HashMap<Product, Integer>();
		ArrayList<Product> recentProductsList = this.jdbc.getRecent();
		for (Product p: recentProductsList) {
			this.recentProducts.put(p, 0);
		}
		for (Product p1: this.recentProducts.keySet()) {
			for (Product p2: this.selectedProducts.keySet()) {
				if (p1.getName().equals(p2.getName())) {
					this.recentProducts.put(p1, this.selectedProducts.get(p2));
				}
			}
		}
	}

	public void updateRecentAmount(String productName, Integer newAmount) {
		System.out.println("MODEL: ");
		// Update recent products amounts
		for (Product p: this.recentProducts.keySet()) {
			if (p.getName().equals(productName)) {
				this.recentProducts.put(p, newAmount);
				if (newAmount == 0) {
					this.selectedProducts.remove(p);
				} else {
					this.selectedProducts.put(p, newAmount);
				}
				System.out.println(p.getName() + " " + newAmount);
			}
		}
	}

}
