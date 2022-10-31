package vendingmachine;

import java.util.ArrayList;
import java.util.HashMap;

public class Controller {

	private Model model;
	private DefaultPageView defaultPageView;
	private CardPayView cardPayView;
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

	public void setCardPayView(CardPayView cardPayView) {
		this.cardPayView = cardPayView;
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

	public void confirmCardPay() {
		// Update recentProducts
		updateRecentAfterPay();
		// Update database
		for (Product p: this.model.getSelectedProducts().keySet()) {
			this.model.updateProductInDB(p.getName(), p.getAmount() - this.model.getSelectedProducts().get(p));
		}
	}

	public int confirmCashPay() {
		Double change = model.getCurrentPrice() - model.getTotalPrice();

		// Check if gave enough money
		if (change < 0) {
			return 1; // Not enough money
		}

		// Check if DB has enough cash for change
		ArrayList<Cash> cashMapInDB = model.getCashMapInDB();
		String[] cashNameList = {
			"$100", "$50", "$20", "$10", 
			"$5", "$2", "$1", "¢50", "¢20",
			"¢10", "¢5", "¢2", "¢1"
		};
		int[] changeAmountLeft = new int[cashNameList.length];
		int index = 0;
		for (Cash c: cashMapInDB) {
			for (String c0: cashNameList) {
				if (c.getName().equals(c0)) {
					Double curretCashValue = c.getValue();
System.out.println("CONTROLLER: confirmCashPay: curretCashValue: " + curretCashValue);
					Integer curretCashAmountInDB = c.getAmount();
System.out.println("CONTROLLER: confirmCashPay: curretCashAmountInDB: " + curretCashAmountInDB);
					Integer curretCashAmountNeeded = 0;
					while (change > curretCashValue && curretCashAmountNeeded <= curretCashAmountInDB && curretCashAmountInDB > 0) {
System.out.println("CONTROLLER: confirmCashPay: change: " + change);
						change -= curretCashValue;
						curretCashAmountNeeded++;
						curretCashAmountInDB--;
					}
					changeAmountLeft[index] = curretCashAmountInDB;
System.out.println("CONTROLLER: confirmCashPay: " + c.getName() + ": left in DB: " + changeAmountLeft[index]);
					index++;
				}
			}
		}
		if (change > 0.01) {
			// Not enough change
			return 2;
		}
		
		// There is enough change; update products amounts
		for (int i = 0; i < cashNameList.length; i++) {
			for (Cash c: cashMapInDB) {
				if (c.getName().equals(cashNameList[i])) {
					c.setAmount(changeAmountLeft[i]);
				}
			}
		}
		this.model.updateCashMapInDB(cashMapInDB);

		// Update recentProducts
		updateRecentAfterPay();

		// Update database
		for (Product p: this.model.getSelectedProducts().keySet()) {
			this.model.updateProductInDB(p.getName(), p.getAmount() - this.model.getSelectedProducts().get(p));
		}
	
		return 0;
	}

	public Boolean ifHasEnoughProductsGrouped(String productName, Integer value, Integer column) {
		if (column.equals(4)) return true;
		for (Product p: this.model.getGroupedProducts().keySet()) {
			if (p.getName().equals(productName)) {
				if (p.getAmount() <= value) return false;
			}
		}
		return true;
	}

	public Boolean ifHasEnoughProductsRecent(String productName, Integer value, Integer column) {
		if (column.equals(4)) return true;
		for (Product p: this.model.getRecentProducts().keySet()) {
			if (p.getName().equals(productName)) {
				if (p.getAmount() <= value) return false;
			}
		}
		return true;
	}

	public Boolean ifHasEnoughProductsSelected(String productName, Integer value, Integer column) {
		if (column.equals(4)) return true;
		for (Product p: this.model.getSelectedProducts().keySet()) {
			if (p.getName().equals(productName)) {
				if (p.getAmount() <= value) return false;
			}
		}
		return true;
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

	public Boolean ifHasCard(String name) {
		return this.model.ifHasCard(name);
	}

	public void register(String userName, String password) {
		this.model.register(userName, password);
	}

	public void resetCashPay() {
		this.model.setCurrentPrice(0.0);
		this.model.resetCashMap();
	}

	public void restart() {
		Model model = new Model(this.model.getJDBC(), this.model.getJSONpath());
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

	public void updateCardInDB(String name, String number) {
		Card newCard = new Card(name, number);
		this.model.updateCardInDB(newCard);
	}

	public void updateCashAmount(String cashName, Integer value, Integer column) {
		Double currentPrice = this.model.getCurrentPrice();
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
				currentPrice -= value * c.getValue();
				cashMap.put(c, newAmount);
				currentPrice += newAmount * c.getValue();
			}
		}
		this.model.setCurrentPrice(currentPrice);
		this.model.setCashMap(cashMap);
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
			Product newProduct = productCounter.duplicate();
			selectedProducts.put(newProduct, newAmount);
System.out.println("created: " + newProduct.getName() + ": " + newAmount);
		}

		// Save data back to model
		this.model.setGroupedProducts(groupedProducts);
		this.model.setRecentProducts(recentProducts);
		this.model.setSelectedProducts(selectedProducts);
	}

	public void updateRecentAfterPay() {
		HashMap<Product, Integer> selectedProducts = this.model.getSelectedProducts();
		int selectedProductsLength = selectedProducts.size();

		// Update Global
		ArrayList<Product> globalRecent = this.model.getGlobalRecent();
		for (int j = 0; j < selectedProductsLength; j++) {
			// Check duplicate
			Product newProduct = (Product)(selectedProducts.keySet().toArray())[j];
			boolean hasProduct = false;
			for (Product p: globalRecent) {
				if (p.getName().equals(newProduct.getName())) hasProduct = true;
			}
			if (hasProduct) continue;
			// No duplicate
			for (int i = 0; i < 4; i++) {
				globalRecent.set(4-i, globalRecent.get(3-i));
			}
			globalRecent.set(0, newProduct);
		}
		for (Product sp: selectedProducts.keySet()) {
			for (Product rp: globalRecent) {
				if (sp == null || rp == null || sp.getName() == null || rp.getName() == null) continue;
				if (rp.getName().equals(sp.getName())) {
					rp.setAmount(rp.getAmount() - selectedProducts.get(sp));
				}
			}
		}
		this.model.updateRecentProductsInDB(globalRecent);

		if (this.model.getCurrentUser().getName() == null) return;

		// Update User
		HashMap<Product, Integer> recentProducts = this.model.getRecentProducts();
		ArrayList<Product> userRecent = new ArrayList<Product>();
		for (Product p: recentProducts.keySet()) userRecent.add(p.duplicate());
		for (int j = 0; j < selectedProductsLength; j++) {
			// Check duplicate
			Product newProduct = (Product)(selectedProducts.keySet().toArray())[j];
			boolean hasProduct = false;
			for (Product p: userRecent) {
				if (p == null || p.getName() == null) continue;
				if (p.getName().equals(newProduct.getName())) hasProduct = true;
			}
			if (hasProduct) continue;
			// No duplicate
			for (int i = 0; i < 4; i++) {
				userRecent.set(4-i, userRecent.get(3-i));
			}
			userRecent.set(0, newProduct);
		}
		for (Product sp: selectedProducts.keySet()) {
			for (Product rp: userRecent) {
				if (sp == null || rp == null || sp.getName() == null || rp.getName() == null) continue;
				if (rp.getName().equals(sp.getName())) {
					rp.setAmount(rp.getAmount() - selectedProducts.get(sp));
				}
			}
		}
		User user = this.model.getCurrentUser();
		user.setRecentProduct(userRecent);
		this.model.updateUserInDB(user);
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
			Product newProduct = productCounter.duplicate();
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
