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

	public void setDefaultPageView(DefaultPageView defaultPageView) {
		this.defaultPageView = defaultPageView;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public void ViewUpdate() {
		this.defaultPageView.launchWindow();
	}


}
