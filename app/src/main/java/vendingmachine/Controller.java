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
		Integer newAmount = value;
		if (column == 4) {
			if (newAmount > 0) newAmount--;
		} else {
			newAmount++;
		}
		System.out.println("CONTROLLER: updateGroupedAmount: " + productName + " " + newAmount);
		this.model.updateGroupedAmount(productName, newAmount);
	}

	public void updateGrouped(ProductType type) {
		System.out.println("CONTROLLER: updateGrouped");
		this.model.updateGrouped(type);
	}

	public void updateRecent() {
		System.out.println("CONTROLLER: updateRecent");
		this.model.updateRecent();
	}

	public void updateRecentAmount(String productName, Integer value, Integer column) {
		Integer newAmount = value;
		if (column == 4) {
			if (newAmount > 0) newAmount--;
		} else {
			newAmount++;
		}
		System.out.println("CONTROLLER: updateRecentAmount: " + productName + " " + newAmount);
		this.model.updateRecentAmount(productName, newAmount);
	}

	public void updateView() {
		this.defaultPageView.updateView();
	}

}
