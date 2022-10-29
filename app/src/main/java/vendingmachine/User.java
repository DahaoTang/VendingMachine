package vendingmachine;

import java.util.ArrayList;

public class User {

	private String name;
	private String password;
	private ArrayList<Product> recentProducts;

	public User() {
		this.name = null;
		this.password = null;
		this.recentProducts = new ArrayList<Product>();
		this.recentProducts.add(null);
		this.recentProducts.add(null);
		this.recentProducts.add(null);
		this.recentProducts.add(null);
		this.recentProducts.add(null);
	}

	public User(String name, String password, ArrayList<Product> recentProducts) {
		this.name = name;
		this.password = password;
		this.recentProducts = new ArrayList<Product>();
		for (Product p: recentProducts) {
			Product newProduct = new Product(
					p.getId(),
					p.getType(),
					p.getName(),
					p.getPrice(),
					p.getAmount()
				);
			this.recentProducts.add(newProduct);
		}
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

	public String toString() {
		String output = "";
		output += "Name: " + this.name + "\n";
		output += "Password: " + this.password + "\n";
		output += "Rencet Products: " + this.recentProducts.get(0).getName() + ", ";
		output += this.recentProducts.get(1).getName() + ", ";
		output += this.recentProducts.get(2).getName() + ",";
		output += this.recentProducts.get(3).getName() + ", ";
		output += this.recentProducts.get(4).getName();
		return output;
	}
}
