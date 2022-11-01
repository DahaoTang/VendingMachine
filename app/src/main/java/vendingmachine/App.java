package vendingmachine;

public class App {

    public static void main(String[] args) {

		String dbPath = "VM.db";
		String JSONpath = "credit_cards.json";

		JDBC jdbc = new JDBC(dbPath);

		/**
		 * Comment to stop init the database when starting the app
		 * */
		jdbc.initDB();

		Model model = new Model(jdbc, JSONpath);
		DefaultPageView defaultPageView = new DefaultPageView();
		Controller controller = new Controller(model, defaultPageView);
		defaultPageView.setController(controller);
		defaultPageView.setModel(model);

		controller.launchWindow();
    }
}
