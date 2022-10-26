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

	public void updateRecentAmount(String productName, Integer value, Integer column) {
		Integer newAmount = value;
		if (column == 4) {
			if (newAmount > 0) newAmount--;
		} else {
			newAmount++;
		}
		this.model.updateRecentAmount(productName, newAmount);
		updateView();	
	}

	public void updateView() {
		this.defaultPageView.updateView();
	}

}
