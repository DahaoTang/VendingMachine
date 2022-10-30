package vendingmachine;

import java.sql.*;
import java.util.ArrayList;

public class JDBC {

	private String dbPath;
	private Connection dbConnection;

	public JDBC() {
		this.dbPath = "VM.db"; // auto-default database path
		this.dbConnection = null;
		tryConnectToDB();
	}

	public JDBC(String dbPath) {
		this.dbPath = dbPath;
		this.dbConnection = null;
		tryConnectToDB();
	}

	/**
	 * ======================
	 * ### INITIALISATION ###
	 * ======================
	 * */
	public void initDB() {

		System.out.println("#####################");
		System.out.println("### DB INIT START ###");
		System.out.println("#####################");

		// Create tables
		createTableCard();
		System.out.println("Card Table created");
		createTableCash();
		System.out.println("Cash Table created");
		createTableProducts();
		System.out.println("Product Table created");
		createTableGlobalRecentProducts();
		System.out.println("Recent Product Table created");
		createTableUser();
		System.out.println("User Table created");

		System.out.println("All Tables created");

		// Insert database
		// Card
		insertCard(new Card("dtan3316", "500173664"));

		System.out.println("Card inserted");

		// Cash
		insertCash(new Cash("$100", 100.0, 0));
		insertCash(new Cash("$50", 50.0, 0));
		insertCash(new Cash("$20", 20.0, 0));
		insertCash(new Cash("$10", 10.0, 10));
		insertCash(new Cash("$5", 5.0, 10));
		insertCash(new Cash("$2", 2.0, 100));
		insertCash(new Cash("$1", 1.0, 100));
		insertCash(new Cash("¢50", 0.5, 100));
		insertCash(new Cash("¢20", 0.2, 100));
		insertCash(new Cash("¢10", 0.1, 100));
		insertCash(new Cash("¢5", 0.05, 100));
		insertCash(new Cash("¢2", 0.02, 100));
		insertCash(new Cash("¢1", 0.01, 100));

		System.out.println("Cash inserted");

		// Products
		Product mineralWater = new Product(101, ProductType.DRINK, "Mineral Water", 3.0, 7);
		Product sprite = new Product(102, ProductType.DRINK, "Sprite", 2.0, 7);
		Product cocaCola = new Product(103, ProductType.DRINK, "Coca Cola", 2.0, 7);
		Product pepsi = new Product(104, ProductType.DRINK, "Pepsi", 2.0, 7);
		Product juice = new Product(105, ProductType.DRINK, "Juice", 3.5, 7);

		Product mars = new Product(201, ProductType.CHOCOLATE, "Mars", 1.0, 7);
		Product mm = new Product(202, ProductType.CHOCOLATE, "M&M", 2.0, 7);
		Product bounty = new Product(203, ProductType.CHOCOLATE, "Bounty", 2.0, 7);
		Product snickers = new Product(204, ProductType.CHOCOLATE, "Snickers", 3.0, 7);

		Product smiths = new Product(301, ProductType.CHIP, "Smiths", 2.0, 7);
		Product pringles = new Product(302, ProductType.CHIP, "Pringles", 2.5, 7);
		Product kettle = new Product(303, ProductType.CHIP, "Kettle", 2.0, 7);
		Product thins = new Product(304, ProductType.CHIP, "Thins", 3.0, 7);

		Product mentos = new Product(401, ProductType.CANDY, "Mentos", 1.0, 7);
		Product sourpatch = new Product(402, ProductType.CANDY, "Sour Patch", 1.0, 7);
		Product skittles = new Product(403, ProductType.CANDY, "Skittles", 1.0, 7);


		insertProduct(mineralWater);
		insertProduct(sprite);
		insertProduct(cocaCola);
		insertProduct(pepsi);
		insertProduct(juice);

		insertProduct(mars);
		insertProduct(mm);
		insertProduct(bounty);
		insertProduct(snickers);

		insertProduct(smiths);
		insertProduct(pringles);
		insertProduct(kettle);
		insertProduct(thins);

		insertProduct(mentos);
		insertProduct(sourpatch);
		insertProduct(skittles);

		System.out.println("Products inserted");

		ArrayList<Product> globalRecent = new ArrayList<Product>();
		globalRecent.add(mineralWater.duplicate());
		globalRecent.add(cocaCola.duplicate());
		globalRecent.add(mars.duplicate());
		globalRecent.add(smiths.duplicate());
		globalRecent.add(mentos.duplicate());

		// Global Recent
		insertRecentAll(globalRecent);

		System.out.println("Recent Products inserted"); 

		// User
		ArrayList<Product> dahaoRecent = new ArrayList<Product>();
		dahaoRecent.add(cocaCola.duplicate());
		dahaoRecent.add(new Product());
		dahaoRecent.add(new Product());
		dahaoRecent.add(new Product());
		dahaoRecent.add(new Product());
		User dahao = new User("dahao", "123", dahaoRecent);
		insertUser(dahao);

		System.out.println("insert dahao");
		ArrayList<Product> testRecent = new ArrayList<Product>();
		testRecent.add(new Product());
		testRecent.add(new Product());
		testRecent.add(new Product());
		testRecent.add(new Product());
		testRecent.add(new Product());
		User test = new User("test", "123", testRecent);
		insertUser(test);
		System.out.println("insert test");

		System.out.println("User inserted");

		System.out.println("###################");
		System.out.println("### DB INIT END ###");
		System.out.println("###################");
	}

	/**
	 * ==============
	 * ### CREATE ###
	 * ==============
	 * */
	/**
	 * Create the Card Table
	 * NAME - the name of the card
	 * NUMBER - the card number
	 * */
	public void createTableCard() {
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
System.out.println("JDBC: createTableCard connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "CREATE TABLE CARD" +
							"(" + 
								"NAME varchar(255)," +
								"NUMBER varchar(255)" + 
							");";
			statement.executeUpdate(sql);
			statement.close();
		} catch (Exception e) {
			System.out.println("From createTableCard");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
System.out.println("JDBC: createTableCard closed");
			} catch (SQLException e) {
				System.out.println("From createTableCard");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
	}

	/**
	 * Create Cash Table
	 * NAME - Name of the cash type
	 * VALUE - the currency value of the cash
	 * AMOUNT - the amount stored
	 * */
	public void createTableCash() {
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
System.out.println("JDBC: createTableCash connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "CREATE TABLE CASH" +
							"(" + 
								"NAME varchar(255)," +
								"VALUE DOUBLE," + 
								"AMOUNT INT" + 
							");";
			statement.executeUpdate(sql);
			statement.close();
		} catch (Exception e) {
			System.out.println("From createTableCash");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
System.out.println("JDBC: createTableCash closed");
			} catch (SQLException e) {
				System.out.println("From createTableCash");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
	}

	/**
	 * Create the Recent Products Table for all users
	 * PRODUCT_*_ID - the *th most recent product
	 * */
	public void createTableGlobalRecentProducts() {
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
System.out.println("JDBC: createTableGlobalRecentProducts connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "CREATE TABLE RECENT" +
							"(" + 
								"PRODUCT_1_ID INT," + 
								"PRODUCT_2_ID INT," + 
								"PRODUCT_3_ID INT," + 
								"PRODUCT_4_ID INT," + 
								"PRODUCT_5_ID INT" + 
							");";
			statement.executeUpdate(sql);
			statement.close();
		} catch (Exception e) {
			System.out.println("From createTableGlobalRecentProducts");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
System.out.println("JDBC: createTableGlobalRecentProducts closed");
			} catch (SQLException e) {
			System.out.println("From createTableGlobalRecentProducts");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
	}

	/**
	 * Create the Products Table
	 * ID - the id of the product
	 * NAME - the name of the product
	 * TYPE - the type of the product 
	 * PRICE - the current price of the product
	 * AMOUNT - the amount of the product stored
	 * */
	public void createTableProducts() {
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
System.out.println("JDBC: createTableProducts connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "CREATE TABLE PRODUCT" +
							"(" + 
								"ID INT," + 
								"NAME varchar(255)," +
								"TYPE varchar(255)," + 
								"PRICE DOUBLE," + 
								"AMOUNT INT" + 
							");";
			statement.executeUpdate(sql);
			statement.close();
		} catch (Exception e) {
			System.out.println("From createTableProducts");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
System.out.println("JDBC: createTableProducts closed");
			} catch (SQLException e) {
			System.out.println("From createTableProducts");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
	}

	/**
	 * Create the User Table
	 * NAME - the name of the user
	 * PASSWORD - the password of the user
	 * RECENT_PRODUCT_*_ID - the *th recent product bought by this user
	 * */
	public void createTableUser() {
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
System.out.println("JDBC: createTableUser connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "CREATE TABLE USER" +
							"(" + 
								"NAME varchar(255)," +
								"PASSWORD varchar(255)," + 
								"RECENT_PRODUCT_1_ID INT," + 
								"RECENT_PRODUCT_2_ID INT," + 
								"RECENT_PRODUCT_3_ID INT," + 
								"RECENT_PRODUCT_4_ID INT," + 
								"RECENT_PRODUCT_5_ID INT" + 
							");";
			statement.executeUpdate(sql);
			statement.close();
		} catch (Exception e) {
			System.out.println("From createTableUser");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
System.out.println("JDBC: createTableUser closed");
			} catch (SQLException e) {
				System.out.println("From createTableUser");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
	}

	/**
	 * ==============
	 * ### DELETE ###
	 * ==============
	 * */
	/**
	 * Delete the card specified by the name provided from the Card Table
	 * */
	public void deleteCard(String name) {
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
System.out.println("JDBC: deleteCard connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "DELETE FROM CARD WHERE NAME='" + name + "';";
			statement.executeUpdate(sql);
			statement.close();
		} catch (Exception e) {
			System.out.println("From deleteCard");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
System.out.println("JDBC: deleteCard closed");
			} catch (SQLException e) {
				System.out.println("From deleteCard");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}

	}

	public void deleteCard(Card card) {
		deleteCard(card.getName());
	}

	/**
	 * Delete the cash specified by the name provided from the Cash Table
	 * */
	public void deleteCash(String name) {
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
System.out.println("JDBC: deleteCash connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "DELETE FROM CASH WHERE NAME='" + name + "';";
			statement.executeUpdate(sql);
			statement.close();
		} catch (Exception e) {
			System.out.println("From deleteCash");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
System.out.println("JDBC: deleteCash closed");
			} catch (SQLException e) {
				System.out.println("From deleteCash");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
	}

	public void deleteCash(Cash cash) {
		deleteCash(cash.getName());
	}

	/**
	 * Delete the product with the same id from the Product Table 
	 * */
	public void deleteProduct(Integer id) {
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
System.out.println("JDBC: deleteProduct connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "DELETE FROM PRODUCT WHERE ID=" + id + ";";
			statement.executeUpdate(sql);
			statement.close();
		} catch (Exception e) {
			System.out.println("From deleteProduct");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
System.out.println("JDBC: deleteProduct closed");
			} catch (SQLException e) {
				System.out.println("From deleteProduct");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
	}

	public void deleteProduct(Product product) {
		deleteProduct(product.getId());
	}

	/**
	 * Delete the user with the same name from the User Table
	 * */
	public void deleteUser(String name) {
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
System.out.println("JDBC: deleteUser connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "DELETE FROM USER WHERE NAME='" + name + "';";
			statement.executeUpdate(sql);
			statement.close();
		} catch (Exception e) {
			System.out.println("From deleteUser");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
System.out.println("JDBC: deleteUser closed");
			} catch (SQLException e) {
				System.out.println("From deleteUser");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
	}

	public void deleteUser(User user) {
		deleteUser(user.getName());
	}


	/**
	 * ===========
	 * ### GET ###
	 * ===========
	 * */

	public ArrayList<Cash> getCashAll() {
		ArrayList<Cash> cashMap = new ArrayList<Cash>();
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
System.out.println("JDBC: getCashAll connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "SELECT * FROM CASH;";
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				String cashName = resultSet.getString("NAME");
				Double cashValue = resultSet.getDouble("VALUE");
				Integer cashAmount = resultSet.getInt("AMOUNT");
				cashMap.add(new Cash(cashName, cashValue, cashAmount));
			}
			resultSet.close();
			statement.close();
		} catch (Exception e) {
			System.out.println("From getCashAll");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
System.out.println("JDBC: getCashAll connected");
			} catch (SQLException e) {
				System.out.println("From getCashAll");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
		return cashMap;
	}

	/** 
	 * Get product with name specified
	 * */
	public Product getProduct(String name) {
		Product product = new Product();
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
System.out.println("JDBC: getProduct connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "SELECT * FROM PRODUCT WHERE NAME='" + name + "';";
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				// Retrieve values back 
				Integer product_id = resultSet.getInt("ID");
				String product_name = resultSet.getString("NAME");
				String product_type = resultSet.getString("TYPE");
				Double product_price = resultSet.getDouble("PRICE");
				Integer product_amount = resultSet.getInt("AMOUNT");
				// Set value to the product to return
				product.setId(product_id);
				product.setName(product_name);
				if (product_type.equals("DRINK")) product.setType(ProductType.DRINK);
				else if (product_type.equals("CHOCOLATE")) product.setType(ProductType.CHOCOLATE);
				else if (product_type.equals("CHIP")) product.setType(ProductType.CHIP);
				else if (product_type.equals("CANDY")) product.setType(ProductType.CANDY);
				product.setPrice(product_price);
				product.setAmount(product_amount);
			}
			resultSet.close();
			statement.close();
		} catch (Exception e) {
			System.out.println("From getProduct");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
System.out.println("JDBC: getProduct closed");
			} catch (SQLException e) {
				System.out.println("From getProduct");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
		return product;
	}

	/** 
	 * Get product with id specified
	 * */
	public Product getProduct(Integer id) {
		Product product = new Product();
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
System.out.println("JDBC: getProduct connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "SELECT * FROM PRODUCT WHERE ID=" + id + ";";
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				// Retrieve values back 
				Integer product_id = resultSet.getInt("ID");
				String product_name = resultSet.getString("NAME");
				String product_type = resultSet.getString("TYPE");
				Double product_price = resultSet.getDouble("PRICE");
				Integer product_amount = resultSet.getInt("AMOUNT");
				// Set value to the product to return
				product.setId(product_id);
				product.setName(product_name);
				if (product_type.equals("DRINK")) product.setType(ProductType.DRINK);
				else if (product_type.equals("CHOCOLATE")) product.setType(ProductType.CHOCOLATE);
				else if (product_type.equals("CHIP")) product.setType(ProductType.CHIP);
				else if (product_type.equals("CANDY")) product.setType(ProductType.CANDY);
				product.setPrice(product_price);
				product.setAmount(product_amount);
			}
			resultSet.close();
			statement.close();
		} catch (Exception e) {
			System.out.println("From getProduct");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
System.out.println("JDBC: getProduct closed");
			} catch (SQLException e) {
				System.out.println("From getProduct");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
		return product;
	}

	/**
	 * Get all the products of the four types
	 * */
	public ArrayList<Product> getProductsAll() {
		ArrayList<Product> allProducts = new ArrayList<Product>();
		for (Product p: getProductsByType(ProductType.DRINK)) {
			allProducts.add(p);
		}
		for (Product p: getProductsByType(ProductType.CHOCOLATE)) {
			allProducts.add(p);
		}
		for (Product p: getProductsByType(ProductType.CHIP)) {
			allProducts.add(p);
		}
		for (Product p: getProductsByType(ProductType.CANDY)) {
			allProducts.add(p);
		}
		return allProducts;
	}

	/**
	 * Get all the products with the type given
	 * */
	public ArrayList<Product> getProductsByType(ProductType ptype) {
		// Convert product type into string
		String type = "";
		if (ptype.equals(ProductType.DRINK)) type = "DRINK";
		else if (ptype.equals(ProductType.CHOCOLATE)) type = "CHOCOLATE";
		else if (ptype.equals(ProductType.CHIP)) type = "CHIP";
		else if (ptype.equals(ProductType.CANDY)) type = "CANDY";
		// Get all the products with the same type
		ArrayList<Product> products = new ArrayList<Product>();
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
System.out.println("JDBC: getProductsByType connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "SELECT * FROM PRODUCT WHERE TYPE='" + type + "';";
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				Integer product_id = resultSet.getInt("ID");
				Product product = getProduct(product_id);
				products.add(product);
			}
			resultSet.close();
			statement.close();
		} catch (Exception e) {
			System.out.println("From getProductsByType");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
System.out.println("JDBC: getProductsByType closed");
			} catch (SQLException e) {
				System.out.println("From getProductsByType");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
		return products;
	}

	/**
	 * Get the global 5 recent product
	 * */
	public ArrayList<Product> getRecent() {
		ArrayList<Product> recent = new ArrayList<Product>();
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
System.out.println("JDBC: getRecent connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "SELECT * FROM RECENT;";
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				for (int index = 1; index < 6; index++) {
					Integer product_id = resultSet.getInt("PRODUCT_" + index + "_ID");
					Product product = getProduct(product_id);
					recent.add(product);
				}
			}
			resultSet.close();
			statement.close();
		} catch (Exception e) {
			System.out.println("From getRecent");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
System.out.println("JDBC: getRecent closed");
			} catch (SQLException e) {
				System.out.println("From getRecent");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
		return recent;
	}
	
	/**
	 * Get the user with the name specified
	 * */
	public User getUser(String name) {
		User user = new User();
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
System.out.println("JDBC: getUser connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "SELECT * FROM USER WHERE NAME='" + name + "';";
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				// Retrieve values back 
				String user_name = resultSet.getString("NAME");
				String user_password = resultSet.getString("PASSWORD");
				Integer user_product_1_id = resultSet.getInt("RECENT_PRODUCT_1_ID");
				Product user_product_1 = getProduct(user_product_1_id);
				Integer user_product_2_id = resultSet.getInt("RECENT_PRODUCT_2_ID");
				Product user_product_2 = getProduct(user_product_2_id);
				Integer user_product_3_id = resultSet.getInt("RECENT_PRODUCT_3_ID");
				Product user_product_3 = getProduct(user_product_3_id);
				Integer user_product_4_id = resultSet.getInt("RECENT_PRODUCT_4_ID");
				Product user_product_4 = getProduct(user_product_4_id);
				Integer user_product_5_id = resultSet.getInt("RECENT_PRODUCT_5_ID");
				Product user_product_5 = getProduct(user_product_5_id);
				// Set value to the product to return
				user.setName(user_name);
				user.setPassword(user_password);
				user.setRecentProduct(0, user_product_1);
				user.setRecentProduct(1, user_product_2);
				user.setRecentProduct(2, user_product_3);
				user.setRecentProduct(3, user_product_4);
				user.setRecentProduct(4, user_product_5);
			}
			resultSet.close();
			statement.close();
		} catch (Exception e) {
			System.out.println("From getUser");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
System.out.println("JDBC: getUser closed");
			} catch (SQLException e) {
				System.out.println("From getUser");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
		return user;
	}

	/**
	 * =============
	 * ### CHECK ###
	 * =============
	 * */
	/**
	 * Check if the user with the name specified exists
	 * */
	public boolean ifHasUser(String name) {
		boolean return_value = true;
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
System.out.println("JDBC: ifHasUser connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "SELECT * FROM USER WHERE NAME='" + name + "';";
			ResultSet resultSet = statement.executeQuery(sql);
			if (!resultSet.isBeforeFirst()) {
				return_value = false;
			} else {
				return_value = true;
			}
			resultSet.close();
			statement.close();
		} catch (Exception e) {
			System.out.println("From ifHasUser");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
System.out.println("JDBC: ifHasUser closed");
			} catch (SQLException e) {
				System.out.println("From ifHasUser");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
		return return_value;
	}

	/**
	 * Check if the username and password match the ones in the database
	 * */
	public boolean ifMatchUser(String name, String password) {
		boolean return_value = false;
		if (ifHasUser(name)) {
			User user = getUser(name);
			if (user.getPassword().equals(password)) {
				return_value = true;
			}
		}
		return return_value;
	}


	/**
	 * ==============
	 * ### INSERT ###
	 * ==============
	 * */
	/**
	 * Insert a new card into the database
	 * */
	public void insertCard(Card card) {
		String name = card.getName();
		String number = card.getName();
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
System.out.println("JDBC: insertCard connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "INSERT INTO CARD " +
							"(NAME, NUMBER) " +
							"VALUES ('" + name + "', '" + number + "');";
			statement.executeUpdate(sql);
			statement.close();
		} catch (Exception e) {
			System.out.println("From insertCard");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
System.out.println("JDBC: insertCard closed");
			} catch (SQLException e) {
				System.out.println("From insertCard");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
	}

	/**
	 * Insert a new type of currency into database
	 * */
	public void insertCash(Cash cash) {
		String name = cash.getName();
		Double value = cash.getValue();
		Integer amount = cash.getAmount();
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
System.out.println("JDBC: insertCash connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "INSERT INTO CASH " +
							"(NAME, VALUE, AMOUNT) " +
							"VALUES ('" + name + "', " + value + ", " + amount + ");";
			statement.executeUpdate(sql);
			statement.close();
		} catch (Exception e) {
			System.out.println("From insertCash");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
System.out.println("JDBC: insertCash closed");
			} catch (SQLException e) {
				System.out.println("From insertCash");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
	}

	/**
	 * Insert a new type of product into database
	 * */
	public void insertProduct(Product product) {
		Integer id = product.getId();
		String typeString = product.getTypeString();
		String name = product.getName();
		Double price = product.getPrice();
		Integer amount = product.getAmount();
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
System.out.println("JDBC: insertProduct connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "INSERT INTO PRODUCT " +
							"(ID, TYPE, NAME, PRICE, AMOUNT) " +
							"VALUES (" + id + ", '" + typeString + "', '" + name + "', " + price + ", " + amount + ");";
			statement.executeUpdate(sql);
			statement.close();
		} catch (Exception e) {
			System.out.println("From insertProduct");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
System.out.println("JDBC: insertProduct closed");
			} catch (SQLException e) {
				System.out.println("From insertProduct");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
	}

	/**
	 * Insert global recent products
	 * */
	public void insertRecentAll(ArrayList<Product> recentProducts) {
		Integer id_1 = recentProducts.get(0).getId();
		Integer id_2 = recentProducts.get(1).getId();
		Integer id_3 = recentProducts.get(2).getId();
		Integer id_4 = recentProducts.get(3).getId();
		Integer id_5 = recentProducts.get(4).getId();
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
System.out.println("JDBC: insertRecentAll connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "INSERT INTO RECENT " +
							"(PRODUCT_1_ID, PRODUCT_2_ID, PRODUCT_3_ID, PRODUCT_4_ID, PRODUCT_5_ID) " +
							"VALUES (" + id_1 + ", " + id_2 + ", " + id_3 + ", " + id_4 + ", " + id_5 + ");";
			statement.executeUpdate(sql);
			statement.close();
		} catch (Exception e) {
			System.out.println("From insertRecentAll");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
System.out.println("JDBC: insertRecentAll closed");
			} catch (SQLException e) {
				System.out.println("From insertRecentAll");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
	}

	/**
	 * Insert a new user into database
	 * */
	public void insertUser(User user) {
		String name = user.getName();
		String password = user.getPassword();
		ArrayList<Product> products = user.getRecentProducts();
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
System.out.println("JDBC: insertUser connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "INSERT INTO USER " +
							"(NAME, PASSWORD, RECENT_PRODUCT_1_ID, RECENT_PRODUCT_2_ID, RECENT_PRODUCT_3_ID, RECENT_PRODUCT_4_ID, RECENT_PRODUCT_5_ID) " +
							"VALUES ('" + 
										name + "', '" + 
										password + "', " + 
										products.get(0).getId() + ", " + 
										products.get(1).getId() + ", " + 
										products.get(2).getId() + ", " + 
										products.get(3).getId() + ", " + 
										products.get(4).getId() + 
									");";
			statement.executeUpdate(sql);
System.out.println("executed");
			statement.close();
System.out.println("statement closed");
		} catch (SQLException e) {
			System.out.println("From insertUser");
			System.err.println( e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
System.out.println("JDBC: insertUser closed");
			} catch (SQLException e) {
				System.out.println("From insertUser");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
	}

	/**
	 * ==============
	 * ### UPDATE ###
	 * ==============
	 * */

	public void updateCard(Card card) {
		deleteCard(card.getName());
		insertCard(card);
	}

	public void updateCash(Cash cash) {
		deleteCash(cash.getName());
		insertCash(cash);
	}

	public void updateProduct(Product product) {
		deleteProduct(product.getId());
		insertProduct(product);
	}

	public void updateUser(User user) {
		deleteUser(user.getName());
		insertUser(user);
	}

	public void updateGlobalRecent(ArrayList<Product> recentProducts) {
		Integer id_1 = recentProducts.get(0).getId();
		Integer id_2 = recentProducts.get(1).getId();
		Integer id_3 = recentProducts.get(2).getId();
		Integer id_4 = recentProducts.get(3).getId();
		Integer id_5 = recentProducts.get(4).getId();

		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
System.out.println("JDBC: updateGlobalRecent connected");
			this.dbConnection.setAutoCommit(true);
			Statement statement = this.dbConnection.createStatement();
			String sql = "UPDATE RECENT SET " + 
				"PRODUCT_1_ID=" + id_1 + ", " + 
				"PRODUCT_2_ID=" + id_2 + ", " + 
				"PRODUCT_3_ID=" + id_3 + ", " + 
				"PRODUCT_4_ID=" + id_4 + ", " + 
				"PRODUCT_5_ID=" + id_5 + 
				";";
			statement.executeUpdate(sql);
			statement.close();
		} catch (Exception e) {
			System.out.println("From updateGlobalRecent");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally {
			try {
				this.dbConnection.close();
System.out.println("JDBC: updateGlobalRecent closed");
			} catch (SQLException e) {
				System.out.println("From updateGlobalRecent");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
	}

	/**
	 * ########################
	 * ### HELPER FUNCTIONS ###
	 * ########################
	 * */

	/**
	 * Try to connect to the database
	 * A new database will be created if the database specified by the path doesn't exist
	 * */
	private void tryConnectToDB() {
		try {
			this.dbConnection = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
System.out.println("JDBC: tryConnectToDB connected");
		} catch (Exception e) {
			System.out.println("From tryConnectToDB");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}finally {
			try {
				this.dbConnection.close();
System.out.println("JDBC: tryConnectToDB closed");
			} catch (SQLException e) {
				System.out.println("From tryConnectToDB");
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}
	}
}
