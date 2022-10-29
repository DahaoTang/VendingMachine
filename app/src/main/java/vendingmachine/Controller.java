package vendingmachine;

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

	public void updateGroupedAmount(String productName, Integer value, Integer column) {
		Double totalAmout = this.model.getTotalAmount();
		Double price = this.model.getPrice(productName);
		Integer newAmount = value;
		if (column == 4) {
			if (newAmount > 0) {
				newAmount--;
				totalAmout -= price;
			}
		} else {
			newAmount++;
			totalAmout += price;
		}
		this.model.setTotalAmount(totalAmout);
		System.out.println("CONTROLLER: updateGroupedAmount: " + productName + " " + newAmount);
		this.model.updateGroupedAmount(productName, newAmount);
	}

	public void changeGroup(ProductType type) {
		System.out.println("CONTROLLER: changeGroup");
		this.model.changeGroup(type);
	}

	public void updateRecentAmount(String productName, Integer value, Integer column) {
		Double totalAmout = this.model.getTotalAmount();
		Double price = this.model.getPrice(productName);
		Integer newAmount = value;
		if (column == 4) {
			if (newAmount > 0) {
				newAmount--;
				totalAmout -= price;
			}
		} else {
			newAmount++;
			totalAmout += price;
		}
		this.model.setTotalAmount(totalAmout);
		System.out.println("CONTROLLER: updateRecentAmount: " + productName + " " + newAmount);
		this.model.updateRecentAmount(productName, newAmount);
	}

	public void updateSelectedAmount(String productName, Integer value, Integer column) {
		Double totalAmout = this.model.getTotalAmount();
		Double price = this.model.getPrice(productName);
		Integer newAmount = value;
		if (column == 4) {
			if (newAmount > 0) {
				newAmount--;
				totalAmout -= price;
			}
		} else {
			newAmount++;
			totalAmout += price;
		}
		this.model.setTotalAmount(totalAmout);
		System.out.println("CONTROLLER: updateSelectedAmount: " + productName + " " + newAmount);
		this.model.updateSelectedAmount(productName, newAmount);
	}

	public void updateView() {
		this.defaultPageView.updateView();
	}

}
