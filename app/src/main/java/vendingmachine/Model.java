package vendingmachine;

import java.io.FileReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.util.ArrayList;
import java.util.HashMap;

public class Model {

	private JDBC jdbc;
	private String JSONpath;

	private User currentUser;
	private Double totalPrice;
	private Double currentPrice;

	private HashMap<Product, Integer> recentProducts;
	private HashMap<Product, Integer> groupedProducts;
	private HashMap<Product, Integer> selectedProducts;

	private HashMap<Cash, Integer> cashMap;

	private HashMap<String, String> cardInfoMap;

	public Model(JDBC jdbc, String JSONpath) {

		this.jdbc = jdbc;
		this.JSONpath = JSONpath;

		this.currentUser = new User();
		this.totalPrice = 0.0;
		this.currentPrice = 0.0;

		this.recentProducts = new HashMap<Product, Integer>();
		this.groupedProducts = new HashMap<Product, Integer>();
		this.selectedProducts = new HashMap<Product, Integer>();
		
		for (Product p: this.jdbc.getRecent()) {
			if (p.getId() == null) continue;
			Product newProduct = p.duplicate();
			this.recentProducts.put(newProduct, 0);
		}

		for (Product p: this.jdbc.getProductsByType(ProductType.DRINK)) {
			if (p.getId() == null) continue;
			Product newProduct = p.duplicate();
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

		this.cashMap = new HashMap<Cash, Integer>();
		for (Cash c: this.jdbc.getCashAll()) {
			if (c.getName() == null) continue;
			this.cashMap.put(c.duplicate(), 0);
		}

		this.cardInfoMap = new HashMap<String, String>();
		setCardInfoMap(JSONpath);
	}

	public Boolean ifHasCard(String name) {
		return this.jdbc.ifHashCard(name);
	}

	public Boolean ifHasUser(String userName) {
		return this.jdbc.ifHasUser(userName);
	}

	public Boolean ifMatchUser(String userName, String password) {
		return this.jdbc.ifMatchUser(userName, password);
	}

	public HashMap<String, String> getCardInfoMap() {
		return this.cardInfoMap;
	}

	public HashMap<Cash, Integer> getCashMap() {
		return this.cashMap;
	}

	public ArrayList<Cash> getCashMapInDB() {
		return this.jdbc.getCashAll();
	}

	public Double getCurrentPrice() {
		return this.currentPrice;
	}

	public User getCurrentUser() {
		return this.currentUser;
	}

	public ArrayList<Product> getGlobalRecent() {
		return this.jdbc.getRecent();
	}

	public HashMap<Product, Integer> getGroupedProducts() {
		return this.groupedProducts;
	}

	public JDBC getJDBC() {
		return this.jdbc;
	}

	public String getJSONpath() {
		return this.JSONpath;
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

	public void register(String userName, String password) {
		ArrayList<Product> recentProducts = new ArrayList<Product>();
		recentProducts.add(new Product());
		recentProducts.add(new Product());
		recentProducts.add(new Product());
		recentProducts.add(new Product());
		recentProducts.add(new Product());
		User newUser = new User(userName, password, recentProducts);
		this.jdbc.insertUser(newUser);
	}

	public void setCardInfoMap(String JSONpath) {
 		JSONParser parser = new JSONParser();
        try {
            Object object = parser.parse(new FileReader(JSONpath));
            JSONArray jsonArray = (JSONArray) object;
            for (Object cardObj: jsonArray) {
                JSONObject cardDetails = (JSONObject) cardObj;
                String name = (String) cardDetails.get("name");
                String number = (String) cardDetails.get("number");
				this.cardInfoMap.put(name, number);
            }

        } catch (Exception e) {
            System.out.println("Card reader error");
        }
	}

	public void resetCashMap() {
		this.cashMap = new HashMap<Cash, Integer>();
		for (Cash c: this.jdbc.getCashAll()) {
			if (c.getName() == null) continue;
			this.cashMap.put(c.duplicate(), 0);
		}
	}

	public void setCashMap(HashMap<Cash, Integer> cashMap) {
		this.cashMap = cashMap;
	}

	public void setCurrentPrice(Double currentPrice) {
		this.currentPrice = currentPrice;
	}

	public void setCurrentUser(String userName) {
		this.currentUser = this.jdbc.getUser(userName);
	}

	public void setGroupedProducts(HashMap<Product, Integer> groupedProducts) {
		this.groupedProducts = groupedProducts;
	}

	public void setJDBC(JDBC jdbc) {
		this.jdbc = jdbc;
	}

	public void setJSONpath(String JSONpath) {
		this.JSONpath = JSONpath;
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

	public void updateCardInDB(Card card) {
		this.jdbc.updateCard(card);
	}

	public void updateCashMapInDB(ArrayList<Cash> cashMap) {
		for (Cash c: cashMap) {
			this.jdbc.updateCash(c);
		}
	}

	public void updateRecentProductsInDB(ArrayList<Product> newRecent) {
			this.jdbc.updateGlobalRecent(newRecent);
	}

	public void updateUserInDB(User user) {
		this.jdbc.updateUser(user);
	}

	public void updateProductInDB(String productName, Integer amount) {
		Product newProduct = this.jdbc.getProduct(productName).duplicate();
		newProduct.setAmount(amount);
		this.jdbc.updateProduct(newProduct);
	}
}
