package vendingmachine;

import java.awt.event.*;
import javax.swing.*;

public class LoginView implements WindowListener {

	private Model model;
	private Controller controller;

	private JFrame defaultPageViewJFrame;

	private JFrame jframe;
	private JPanel jpanel;

	private JLabel userNameLabel;
	private JTextField userNameTextField;

	private JLabel passwordLabel;
	private JPasswordField passwordField;

	private JButton registerButton;
	private JButton loginButton;


	// FINAL DATA
	private final int[] WINDOW_SIZE = {300, 200};

	private final int[] USER_NAME_LABEL_BP = {20, 30, 80, 32};
	private final int[] USER_NAME_TEXT_FEILD_BP = {90, 30, 190, 32};

	private final int[] PASSWORD_LABEL_BP = {20, 70, 80, 32};
	private final int[] PASSWORD_FEILD_BP = {90, 70, 190, 32};

	private final int[] REGISTER_BUTTON_BP = {15, 120, 120, 32};
	private final int[] LOGIN_BUTTON_BP = {160, 120, 120, 32};


	public LoginView(Model model, Controller controller, JFrame defaultPageViewJFrame) {
		this.model = model;
		this.controller = controller;
		this.defaultPageViewJFrame = defaultPageViewJFrame;

		this.jframe = new JFrame();
		this.jpanel = new JPanel();

		this.userNameLabel = new JLabel();
		this.userNameTextField = new JTextField();

		this.passwordLabel = new JLabel();
		this.passwordField = new JPasswordField();

		this.registerButton = new JButton();
		this.loginButton = new JButton();
	}

	public void launchWindow() {
		/**
		 * ===================
		 * ### Basic Setup ###
		 * ===================
		 * */
		this.jframe.setTitle("Login");
		this.jframe.setSize(WINDOW_SIZE[0], WINDOW_SIZE[1]);;
		this.jframe.setResizable(false);
		this.jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.jframe.setLocationRelativeTo(null);

		this.jframe.addWindowListener(this);

		this.jpanel.setLayout(null);
		this.jframe.add(this.jpanel);


		/**
		 * ===========================
		 * ### Label and TextFeild ###
		 * ===========================
		 * */
		// User Name
		this.userNameLabel.setText("Username: ");
		this.userNameLabel.setBounds(
				USER_NAME_LABEL_BP[0],
				USER_NAME_LABEL_BP[1],
				USER_NAME_LABEL_BP[2],
				USER_NAME_LABEL_BP[3]
			);
		this.jpanel.add(this.userNameLabel);

		this.userNameTextField.setBounds(
				USER_NAME_TEXT_FEILD_BP[0],
				USER_NAME_TEXT_FEILD_BP[1],
				USER_NAME_TEXT_FEILD_BP[2],
				USER_NAME_TEXT_FEILD_BP[3]
			);
		this.jpanel.add(this.userNameTextField);

		// Password
		this.passwordLabel.setText("Password: ");
		this.passwordLabel.setBounds(
				PASSWORD_LABEL_BP[0],
				PASSWORD_LABEL_BP[1],
				PASSWORD_LABEL_BP[2],
				PASSWORD_LABEL_BP[3]
			);
		this.jpanel.add(this.passwordLabel);

		this.passwordField.setText("");
		this.passwordField.setBounds(
				PASSWORD_FEILD_BP[0],
				PASSWORD_FEILD_BP[1],
				PASSWORD_FEILD_BP[2],
				PASSWORD_FEILD_BP[3]
			);
		this.jpanel.add(this.passwordField);


		/**
		 * ==========================
		 * ### Register and Login ###
		 * ==========================
		 * */

		// Register
		this.registerButton.setText("Register");
		this.registerButton.setBounds(
				REGISTER_BUTTON_BP[0],
				REGISTER_BUTTON_BP[1],
				REGISTER_BUTTON_BP[2],
				REGISTER_BUTTON_BP[3]
			);
		this.registerButton.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				RegisterView registerView = new RegisterView(model, controller, defaultPageViewJFrame, jframe);
				registerView.launchWindow();
			}
		});
		this.jpanel.add(this.registerButton);

		// Login
		this.loginButton.setText("Confirm");
		this.loginButton.setBounds(
				LOGIN_BUTTON_BP[0],
				LOGIN_BUTTON_BP[1],
				LOGIN_BUTTON_BP[2],
				LOGIN_BUTTON_BP[3]
			);
		this.loginButton.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				String userName = userNameTextField.getText();
				String password = passwordField.getText();
				if (controller.ifMatchUserInDB(userName, password)){
					controller.setCurrentUser(userName);
					controller.updateAfterLogin();
					jframe.dispose();
				} else {
					JOptionPane.showMessageDialog(null, "Invalid user name or password.");
				}
			}
		});
		this.jpanel.add(this.loginButton);


		this.jframe.setVisible(true);
	}

	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowClosed(WindowEvent e) {
		this.defaultPageViewJFrame.dispose();
		this.controller.setDefaultPageView(null);

		// Normal user or Anonymous
		if (this.model.getCurrentUser().getName() == null || this.model.getCurrentUser().getType().equals(UserType.NORMAL)) {
			DefaultPageView defaultPageView = new DefaultPageView();
			this.controller.setDefaultPageView(defaultPageView);
			defaultPageView.setModel(this.model);
			defaultPageView.setController(this.controller);
			defaultPageView.launchWindow();

		// Seller
		} else if (this.model.getCurrentUser().getType().equals(UserType.SELLER)) {
			SellerView sellerView = new SellerView();
			Model newModel = new Model(this.model.getJDBC(), this.model.getJSONpath());
			newModel.setCurrentUser(this.model.getCurrentUser());
			sellerView.setModel(newModel);	
			sellerView.setController(this.controller);
			sellerView.launchWindow();

		// Cashier
		} else if (this.model.getCurrentUser().getType().equals(UserType.CASHIER)) {

		// Owner
		} else if (this.model.getCurrentUser().getType().equals(UserType.OWNER)) {

		}
	}

	@Override
	public void windowClosing(WindowEvent e) {}

	@Override
	public void windowDeactivated(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowOpened(WindowEvent e) {}
}

