package vendingmachine;

import java.util.ArrayList;

public class User {

	private String name;
	private String password;
	private ArrayList<Product> recentProducts;

	public User() {
		this.name = null;
		this.password = null;
		this.recentProducts = null;
	}

	public User(String name, String password, ArrayList<Product> recentProducts) {
		this.name = name;
		this.password = password;
		this.recentProducts = recentProducts;
	}

	public String getName() {
		return this.name;
	}

	public String getPassword() {
		return this.password;
	}

	public ArrayList<Product> getRecentProducts() {
		return this.recentProducts;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRecentProduct(ArrayList<Product> recentProducts) {
		this.recentProducts = recentProducts;
	}

	public void setRecentProduct(Integer index, Product product) {
		index = index % 5;
		this.recentProducts.add(index, product);
	}
}
