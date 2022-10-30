package vendingmachine;

import java.util.ArrayList;
import java.util.HashMap;


public class Controller {

	private Model model;
	private DefaultPageView defaultPageView;
	private CashPayView cashPayView;

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

	public void setCashPayView(CashPayView cashPayView) {
		this.cashPayView = cashPayView;
	}

	public void setDefaultPageView(DefaultPageView defaultPageView) {
		this.defaultPageView = defaultPageView;
	}

	public void setModel(Model model) {
		this.model = model;
	}



	/**
	 * ###############
	 * ### METHODS ###
	 * ###############
	 * */
	public void changeGroup(ProductType type) {
System.out.println("CONTROLLER: changeGroup");

		// retrieve data from model
		HashMap<Product, Integer> groupedProducts = new HashMap<Product, Integer>();
		HashMap<Product, Integer> selectedProducts = this.model.getSelectedProducts();
		for (Product p: this.model.getProductsByType(type)) {
			if (p == null) continue;
			Product newProduct = p.duplicate();
			groupedProducts.put(newProduct, 0);
		}
		for (Product gp: groupedProducts.keySet()) {
			for (Product sp: selectedProducts.keySet()) {
				if (sp == null || gp == null || sp.getName() == null || gp.getName() == null) continue;
				if (gp.getName().equals(sp.getName())) {
					groupedProducts.put(gp, selectedProducts.get(sp));
System.out.println(gp.getName() + ": " + groupedProducts.get(gp));
				}
			}
		}

		// Save data back to model
		this.model.setGroupedProducts(groupedProducts);
	}

	public Boolean ifHasUser(String userName) {
		if (this.model.ifHasUser(userName)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean ifLoggedIn(String userName, String password) {
		if (this.model.ifHasUser(userName)) {
			if (this.model.ifMatchUser(userName, password)) {
				return true;
			}
		}
		return false;
	}

	public void register(String userName, String password) {
		this.model.register(userName, password);
	}

	public void restart() {
		Model model = new Model(this.model.getJDBC());
		DefaultPageView defaultPageView = new DefaultPageView();
		Controller controller = new Controller(model, defaultPageView);
		defaultPageView.setController(controller);
		defaultPageView.setModel(model);
		controller.launchWindow();
	}

	public void setCurrentUser(String userName) {
System.out.println("CONTROLLER: setCurrentUser");
		this.model.setCurrentUser(userName);
	}

	public void updateAfterLogin() {
System.out.println("CONTROLLER: updateAfterLogin");
System.out.println(this.model.getCurrentUser());
		// Rencet products
		HashMap<Product, Integer> recentProducts = new HashMap<Product, Integer>();
		for (Product p: this.model.getCurrentUser().getRecentProducts()) {
			if (p == null) continue;
			Product newProduct = p.duplicate();
			recentProducts.put(newProduct, 0);
		}
		for (Product rp: recentProducts.keySet()) {
			for (Product sp: this.model.getSelectedProducts().keySet()) {
				if (rp == null || sp == null || rp.getName() == null || sp.getName() == null) continue;
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
			if (gp == null || gp.getName() == null) continue;
			if (gp.getName().equals(productName)) {
				groupedProducts.put(gp, newAmount);
				productCounter = gp;
			}
		}
		
		// Update recent products
		for (Product rp: recentProducts.keySet()) {
			if (rp == null || rp.getName() == null) continue;
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
			if (sp == null || sp.getName() == null) continue;
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


	public void updateCashAmount(String cashName, Integer value, Integer column) {
		Double currentPrice = this.model.getTotalPrice();
		HashMap<Cash, Integer> cashMap = this.model.getCashMap();
		Integer newAmount = value;
		if (column == 1) {
			if (newAmount > 0) {
				newAmount--;
			}
		} else {
			newAmount++;
		}
		for (Cash c: cashMap.keySet()) {
			if (c == null || c.getName() == null) continue;
			if (c.getName().equals(cashName)) {
				currentPrice -= cashMap.get(c) * c.getValue();
				cashMap.put(c, newAmount);
				currentPrice += cashMap.get(c) * c.getValue();
			}
		}
		this.model.setCurrentPrice(currentPrice);
		this.model.setCashMap(cashMap);
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
			if (rp == null || rp.getName() == null) continue;
			if (rp.getName().equals(productName)) {
				recentProducts.put(rp, newAmount);
				productCounter = rp;
			}
		}

		// Update grouped prouducts
		for (Product gp: groupedProducts.keySet()) {
			if (gp == null || gp.getName() == null) continue;
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
			if (sp == null || sp.getName() == null) continue;
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
			if (sp == null || sp.getName() == null) continue;
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
			if (rp == null || rp.getName() == null) continue;
			if (rp.getName().equals(productName)) {
				recentProducts.put(rp, newAmount);
System.out.println("from recent: " + rp.getName() + ": " + recentProducts.get(rp));
			}
		}

		// Update grouped products
		for (Product gp: groupedProducts.keySet())	{
			if (gp == null || gp.getName() == null) continue;
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

	public void updateViewDefaultPage() {
		this.defaultPageView.updateView();
	}

	public void updateViewCashPay() {
		this.cashPayView.updateView();
	}

}
