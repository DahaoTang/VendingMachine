package vendingmachine;

public class Product {

	private Integer id;
	private ProductType type;
	private String name;
	private Double price;
	private Integer amount;

	public Product() {
		this.id = null;
		this.type = null;
		this.name = null;
		this.price = null;
		this.amount = null;
	}

	public Product(Integer id, ProductType type, String name, Double price, Integer amount) {
		this.id = id;
		this.type = type;
		this.name = name;
		this.price = price;
		this.amount = amount;
	}

	public Integer getAmount() {
		return this.amount;
	}

	public Integer getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public Double getPrice() {
		return this.price;
	}

	public ProductType getType() {
		return this.type;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public void setType(ProductType type) {
		this.type = type;
	}
}
