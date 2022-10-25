package vendingmachine;

import java.util.ArrayList;
import java.util.HashMap;

public class Model {

	private JDBC jdbc;

	private User currentUser;
	private Double totalAmout;
	private HashMap<Product, Integer> recentProducts;
	private ProductType selectedType;
	private HashMap<Product, Integer> listedProducts;

	private HashMap<Product, Integer> selectedProducts;

	public Model(JDBC jdbc) {
		this.jdbc = jdbc;

		this.currentUser = null;
		this.totalAmout = null;
		this.recentProducts = null;
		this.selectedProducts = null;
	}



}
