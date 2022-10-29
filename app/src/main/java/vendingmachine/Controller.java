package vendingmachine;

import java.util.ArrayList;
import java.util.HashMap;

public class Controller {

	private Model model;
	private DefaultPageView defaultPageView;

	public Controller() {
		this.model = null;
		this.defaultPageView = null;
	}

	public Controller(Model model, DefaultPageView defaultPageView) {
		this.model = model;
		this.defaultPageView = defaultPageView;
	}

	public void launchWindow() {
		this.defaultPageView.launchWindow();
	}

	public void setDefaultPageView(DefaultPageView defaultPageView) {
		this.defaultPageView = defaultPageView;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public Boolean ifLoggedIn(String userName, String password) {
		if (this.model.ifHasUser(userName)) {
			if (this.model.ifMatchUser(userName, password)) {
				return true;
			}
		}
		return false;
	}

	public void setCurrentUser(String userName) {
		this.model.setCurrentUser(userName);
	}

	public void updateAfterLogin() {
		System.out.println("CONTROLLER: updateAfterLogin");
		System.out.println(this.model.getCurrentUser());
		// Rencet products
		HashMap<Product, Integer> recentProducts = new HashMap<Product, Integer>();
		for (Product p: this.model.getCurrentUser().getRecentProducts()) {
			Product newProduct = new Product(p.getId(), p.getType(), p.getName(), p.getPrice(), p.getAmount());
			recentProducts.put(newProduct, 0);
		}
		for (Product rp: recentProducts.keySet()) {
			for (Product sp: this.model.getSelectedProducts().keySet()) {
				if (rp.getName().equals(sp.getName())) {
					recentProducts.put(rp, this.model.getSelectedProducts().get(sp));
				}
			}
		}
		this.model.setRecentProducts(recentProducts);
	}

	public void updateGroupedAmount(String productName, Integer value, Integer column) {
		Double totalPrice = this.model.getTotalPrice();
		Double price = this.model.getPrice(productName);
		Integer newAmount = value;
		if (column == 4) {
			if (newAmount > 0) {
				newAmount--;
				totalPrice -= price;
			}
		} else {
			newAmount++;
			totalPrice += price;
		}
		this.model.setTotalPrice(totalPrice);
		System.out.println("CONTROLLER: updateGroupedAmount: " + productName + " " + newAmount);

		// Retrieve data from model
		HashMap<Product, Integer> groupedProducts = this.model.getGroupedProducts();
		HashMap<Product, Integer> recentProducts = this.model.getRecentProducts();
		HashMap<Product, Integer> selectedProducts = this.model.getSelectedProducts();
		
		// Update grouped products
		Product productCounter = null;
		for (Product gp: groupedProducts.keySet()) {
			if (gp.getName().equals(productName)) {
				groupedProducts.put(gp, newAmount);
				productCounter = gp;
			}
		}
		
		// Update recent products
		for (Product rp: recentProducts.keySet()) {
			if (rp.getName().equals(productName)) {
				recentProducts.put(rp, newAmount);
				System.out.println("from recent: " + rp.getName() + ": " + recentProducts.get(rp));
			}
		}

		// Update selected products
		System.out.print("from selected: ");
		Boolean inSelected = false;
		Product toRemove = null;
		for (Product sp: selectedProducts.keySet()) {
			if (sp.getName().equals(productName)) {
				inSelected = true;
				selectedProducts.put(sp, newAmount);
				System.out.println(sp.getName() + ": " + newAmount);
				if (newAmount == 0) {
					toRemove = sp;
				}
			}
		}
		if (toRemove != null) {
			selectedProducts.remove(toRemove);
			System.out.println("removed: " + toRemove.getName());
		}
		if (inSelected == false && newAmount > 0) {
			Product newProduct = new Product(
					productCounter.getId(), 
					productCounter.getType(),
					productCounter.getName(), 
					productCounter.getPrice(), 
					productCounter.getAmount()
				);
			selectedProducts.put(newProduct, newAmount);
			System.out.println("created: " + newProduct.getName() + ": " + newAmount);
		}

		// Save data back to model
		this.model.setGroupedProducts(groupedProducts);
		this.model.setRecentProducts(recentProducts);
		this.model.setSelectedProducts(selectedProducts);
	}

	public void changeGroup(ProductType type) {
		System.out.println("CONTROLLER: changeGroup");

		// retrieve data from model
		HashMap<Product, Integer> groupedProducts = new HashMap<Product, Integer>();
		HashMap<Product, Integer> selectedProducts = this.model.getSelectedProducts();
		for (Product p: this.model.getProductsByType(type)) {
			Product newProduct = new Product(p.getId(), p.getType(), p.getName(), p.getPrice(), p.getAmount());
			groupedProducts.put(newProduct, 0);
		}
		for (Product gp: groupedProducts.keySet()) {
			for (Product sp: selectedProducts.keySet()) {
				if (gp.getName().equals(sp.getName())) {
					groupedProducts.put(gp, selectedProducts.get(sp));
					System.out.println(gp.getName() + ": " + groupedProducts.get(gp));
				}
			}
		}

		// Save data back to model
		this.model.setGroupedProducts(groupedProducts);
	}

	public void updateRecentAmount(String productName, Integer value, Integer column) {
		Double totalPrice = this.model.getTotalPrice();
		Double price = this.model.getPrice(productName);
		Integer newAmount = value;
		if (column == 4) {
			if (newAmount > 0) {
				newAmount--;
				totalPrice -= price;
			}
		} else {
			newAmount++;
			totalPrice += price;
		}
		this.model.setTotalPrice(totalPrice);
		System.out.println("CONTROLLER: updateRecentAmount: " + productName + " " + newAmount);
		
		// Retrieve data from model
		HashMap<Product, Integer> groupedProducts = this.model.getGroupedProducts();
		HashMap<Product, Integer> recentProducts = this.model.getRecentProducts();
		HashMap<Product, Integer> selectedProducts = this.model.getSelectedProducts();

		// Update recent products
		Product productCounter = null;
		for (Product rp: recentProducts.keySet()) {
			if (rp.getName().equals(productName)) {
				recentProducts.put(rp, newAmount);
				productCounter = rp;
			}
		}

		// Update grouped prouducts
		for (Product gp: groupedProducts.keySet()) {
			if (gp.getName().equals(productName)) {
				groupedProducts.put(gp, newAmount);
				System.out.println("from grouped: " + gp.getName() + ": " + groupedProducts.get(gp));
			}
		}

		// Update selected products
		System.out.print("from selected: ");
		Boolean inSelected = false;
		Product toRemove = null;
		for (Product sp: selectedProducts.keySet()) {
			if (sp.getName().equals(productName)) {
				inSelected = true;
				selectedProducts.put(sp, newAmount);
				System.out.println(sp.getName() + ": " + newAmount);
				if (newAmount == 0) {
					toRemove = sp;
				}
			}
		}
		if (toRemove != null) {
			selectedProducts.remove(toRemove);
			System.out.println("removed: " + toRemove.getName());
		}
		if (inSelected == false && newAmount > 0) {
			Product newProduct = new Product(
					productCounter.getId(), 
					productCounter.getType(), 
					productCounter.getName(), 
					productCounter.getPrice(), 
					productCounter.getAmount()
				);
			selectedProducts.put(newProduct, newAmount);
			System.out.println("created: " + newProduct.getName() + ": " + newAmount);
		}

		// Save data back to model
		this.model.setGroupedProducts(groupedProducts);
		this.model.setRecentProducts(recentProducts);
		this.model.setSelectedProducts(selectedProducts);
	}

	public void updateSelectedAmount(String productName, Integer value, Integer column) {
		Double totalPrice = this.model.getTotalPrice();
		Double price = this.model.getPrice(productName);
		Integer newAmount = value;
		if (column == 4) {
			if (newAmount > 0) {
				newAmount--;
				totalPrice -= price;
			}
		} else {
			newAmount++;
			totalPrice += price;
		}
		this.model.setTotalPrice(totalPrice);
		System.out.println("CONTROLLER: updateSelectedAmount: " + productName + " " + newAmount);

		// Retrieve data from model
		HashMap<Product, Integer> groupedProducts = this.model.getGroupedProducts();
		HashMap<Product, Integer> recentProducts = this.model.getRecentProducts();
		HashMap<Product, Integer> selectedProducts = this.model.getSelectedProducts();

		// Update selected products
		Product toRemove = null;	
		for (Product sp: selectedProducts.keySet()) {
			if (sp.getName().equals(productName)) {
				selectedProducts.put(sp, newAmount);
				if (newAmount == 0) {
					toRemove = sp;	
				}
			}
		}
		if (toRemove != null) {
			selectedProducts.remove(toRemove);
			System.out.println("removed: " + toRemove.getName());
		}


		// Update recent products
		for (Product rp: recentProducts.keySet()) {
			if (rp.getName().equals(productName)) {
				recentProducts.put(rp, newAmount);
				System.out.println("from recent: " + rp.getName() + ": " + recentProducts.get(rp));
			}
		}

		// Update grouped products
		for (Product gp: groupedProducts.keySet())	{
			if (gp.getName().equals(productName)) {
				groupedProducts.put(gp, newAmount);
				System.out.println("from grouped: " + gp.getName() + ": " + groupedProducts.get(gp));
			}
		}

		// Save data back to model
		this.model.setGroupedProducts(groupedProducts);
		this.model.setRecentProducts(recentProducts);
		this.model.setSelectedProducts(selectedProducts);
	}

	public void updateView() {
		this.defaultPageView.updateView();
	}

}
