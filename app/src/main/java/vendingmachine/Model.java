package vendingmachine;

import java.util.ArrayList;
import java.util.HashMap;

public class Model {

	private JDBC jdbc;

	private User currentUser;
	private Double totalAmount;

	private HashMap<Product, Integer> recentProducts;

	private HashMap<Product, Integer> groupedProducts;

	private HashMap<Product, Integer> selectedProducts;

	public Model(JDBC jdbc) {

		this.jdbc = jdbc;

		this.currentUser = new User();
		this.totalAmount = 0.0;

		this.recentProducts = new HashMap<Product, Integer>();
		for (Product p: this.jdbc.getRecent()) {
			this.recentProducts.put(p, 0);
		}
		this.groupedProducts = new HashMap<Product, Integer>();
		this.selectedProducts = new HashMap<Product, Integer>();

		changeGroup(ProductType.DRINK);
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

	public Double getPrice(String productName) {
		return this.jdbc.getProduct(productName).getPrice();
	}

	public ArrayList<Product> getProductsByType(ProductType type) {
		return this.jdbc.getProductsByType(type);
	}

	public HashMap<Product, Integer> getSelectedProducts() {
		return this.selectedProducts;
	}

	public Double getTotalAmount() {
		return this.totalAmount;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
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

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public void setSelectedProducts(HashMap<Product, Integer> selectedProducts) {
		this.selectedProducts = selectedProducts;
	}

	public void changeGroup(ProductType type) {
		System.out.println("MODEL: changeGroup: ");
		this.groupedProducts = new HashMap<Product, Integer>();
		for (Product p: this.jdbc.getProductsByType(type)) {
			this.groupedProducts.put(p, 0);
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

	public void updateGroupedAmount(String productName, Integer newAmount) {
		System.out.println("MODEL: updateGroupedAmount: " + productName + ": " + newAmount);
		// Update grouped products
		Product productCounter = null;
		for (Product gp: this.groupedProducts.keySet()) {
			if (gp.getName().equals(productName)) {
				this.groupedProducts.put(gp, newAmount);
				productCounter = gp;
			}
		}
		
		// Update recent products
		for (Product rp: this.recentProducts.keySet()) {
			if (rp.getName().equals(productName)) {
				this.recentProducts.put(rp, newAmount);
				System.out.println("from recent: " + rp.getName() + ": " + this.recentProducts.get(rp));
			}
		}

		// Update selected products
		System.out.print("from selected: ");
		Boolean inSelected = false;
		Product toRemove = null;
		for (Product sp: this.selectedProducts.keySet()) {
			if (sp.getName().equals(productName)) {
				inSelected = true;
				this.selectedProducts.put(sp, newAmount);
				System.out.println(sp.getName() + ": " + newAmount);
				if (newAmount == 0) {
					toRemove = sp;
				}
			}
		}
		if (toRemove != null) {
			this.selectedProducts.remove(toRemove);
			System.out.println("removed: " + toRemove.getName());
		}
		if (inSelected == false && newAmount > 0) {
			Product newProduct = new Product(productCounter.getId(), productCounter.getType(), productCounter.getName(), productCounter.getPrice(), productCounter.getAmount());
			this.selectedProducts.put(newProduct, newAmount);
			System.out.println("created: " + newProduct.getName() + ": " + newAmount);
		}


		// Update total Amount
	}

	public void updateRecentAmount(String productName, Integer newAmount) {
		System.out.println("MODEL: updateRecentAmount: " + productName + ": " + newAmount);
		// Update recent products
		Product productCounter = null;
		for (Product rp: this.recentProducts.keySet()) {
			if (rp.getName().equals(productName)) {
				this.recentProducts.put(rp, newAmount);
				productCounter = rp;
			}
		}

		// Update grouped prouducts
		for (Product gp: this.groupedProducts.keySet()) {
			if (gp.getName().equals(productName)) {
				this.groupedProducts.put(gp, newAmount);
				System.out.println("from grouped: " + gp.getName() + ": " + this.groupedProducts.get(gp));
			}
		}

		// Update selected products
		System.out.print("from selected: ");
		Boolean inSelected = false;
		Product toRemove = null;
		for (Product sp: this.selectedProducts.keySet()) {
			if (sp.getName().equals(productName)) {
				inSelected = true;
				this.selectedProducts.put(sp, newAmount);
				System.out.println(sp.getName() + ": " + newAmount);
				if (newAmount == 0) {
					toRemove = sp;
				}
			}
		}
		if (toRemove != null) {
			this.selectedProducts.remove(toRemove);
			System.out.println("removed: " + toRemove.getName());
		}
		if (inSelected == false && newAmount > 0) {
			Product newProduct = new Product(productCounter.getId(), productCounter.getType(), productCounter.getName(), productCounter.getPrice(), productCounter.getAmount());
			this.selectedProducts.put(newProduct, newAmount);
			System.out.println("created: " + newProduct.getName() + ": " + newAmount);
		}
	}

	public void updateSelectedAmount(String productName, Integer newAmount) {
		System.out.println("MODEL: updateSelectedAmount: " + productName + ": " + newAmount);
		// Update selected products
		Product toRemove = null;	
		for (Product sp: this.selectedProducts.keySet()) {
			if (sp.getName().equals(productName)) {
				this.selectedProducts.put(sp, newAmount);
				if (newAmount == 0) {
					toRemove = sp;	
				}
			}
		}
		if (toRemove != null) {
			this.selectedProducts.remove(toRemove);
			System.out.println("removed: " + toRemove.getName());
		}


		// Update recent products
		for (Product rp: this.recentProducts.keySet()) {
			if (rp.getName().equals(productName)) {
				this.recentProducts.put(rp, newAmount);
				System.out.println("from recent: " + rp.getName() + ": " + this.recentProducts.get(rp));
			}
		}

		// Update grouped products
		for (Product gp: this.groupedProducts.keySet())	{
			if (gp.getName().equals(productName)) {
				this.groupedProducts.put(gp, newAmount);
				System.out.println("from grouped: " + gp.getName() + ": " + this.groupedProducts.get(gp));
			}
		}
	}
}
