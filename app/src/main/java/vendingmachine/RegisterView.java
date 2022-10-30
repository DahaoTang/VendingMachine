package vendingmachine;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class RegisterView implements WindowListener {

	private Model model;
	private Controller controller;

	private JFrame defaultPageViewJFrame;
	private JFrame loginViewJFrame;

	private JFrame jframe;
	private JPanel jpanel;

	private JLabel userNameLabel;
	private JTextField userNameTextField;

	private JLabel passwordLabel;
	private JPasswordField passwordField;

	private JLabel reenterPasswordLabel;
	private JPasswordField reenterPasswordField;

	private JButton cancelButton;
	private JButton registerButton;


	// DATA
	private final int[] WINDOW_SIZE = {300, 240};

	private final int[] USER_NAME_LABEL_BP = {20, 30, 80, 32};
	private final int[] USER_NAME_TEXT_FEILD_BP = {90, 30, 190, 32};

	private final int[] PASSWORD_LABEL_BP = {20, 70, 80, 32};
	private final int[] PASSWORD_FEILD_BP = {90, 70, 190, 32};

	private final int[] PASSWORD_CONFIRM_LABEL_BP = {20, 110, 80, 32};
	private final int[] PASSWORD_CONFIRM_FEILD_BP = {90, 110, 190, 32};

	private final int[] CANCEL_BUTTON_BP = {15, 160, 120, 32};
	private final int[] REGISTER_BUTTON_BP = {160, 160, 120, 32};	



	public RegisterView(Model model, Controller controller, JFrame defaultPageViewJFrame, JFrame loginViewJFrame) {
		this.model = model;
		this.controller = controller;
		this.defaultPageViewJFrame = defaultPageViewJFrame;
		this.loginViewJFrame = loginViewJFrame;

		this.jframe = new JFrame();
		this.jpanel = new JPanel();

		this.userNameLabel = new JLabel();
		this.userNameTextField = new JTextField();

		this.passwordLabel = new JLabel();
		this.passwordField = new JPasswordField();

		this.reenterPasswordLabel = new JLabel();
		this.reenterPasswordField = new JPasswordField();

		this.registerButton = new JButton();
		this.cancelButton = new JButton();

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

		// Re-enter password
		this.reenterPasswordLabel.setText("Re-enter: ");
		this.reenterPasswordLabel.setBounds(
				PASSWORD_CONFIRM_LABEL_BP[0],
				PASSWORD_CONFIRM_LABEL_BP[1],
				PASSWORD_CONFIRM_LABEL_BP[2],
				PASSWORD_CONFIRM_LABEL_BP[3]
			);
		this.jpanel.add(this.reenterPasswordLabel);

		this.reenterPasswordField.setText("");
		this.reenterPasswordField.setBounds(
				PASSWORD_CONFIRM_FEILD_BP[0],
				PASSWORD_CONFIRM_FEILD_BP[1],
				PASSWORD_CONFIRM_FEILD_BP[2],
				PASSWORD_CONFIRM_FEILD_BP[3]
			);
		this.jpanel.add(this.reenterPasswordField);

		/**
		 * ===============
		 * ### Buttons ###
		 * ===============
		 * */

		this.cancelButton.setText("Cancel");
		this.cancelButton.setBounds(
				CANCEL_BUTTON_BP[0],
				CANCEL_BUTTON_BP[1],
				CANCEL_BUTTON_BP[2],
				CANCEL_BUTTON_BP[3]
			);
		this.cancelButton.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				jframe.dispose();
			}
		});
		this.jpanel.add(this.cancelButton);

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
				String userName = userNameTextField.getText();
				String password_1 = passwordField.getText();
				String password_2 = reenterPasswordField.getText();
				if (password_1.equals(password_2)) {
					if (controller.ifHasUser(userName)) {
						JOptionPane.showMessageDialog(null, 
								"User with the same already exists.\nPlease try another."
							);
					} else {
						controller.register(userName, password_1);
						controller.setCurrentUser(userName);
						controller.updateAfterLogin();

						defaultPageViewJFrame.dispose();
						loginViewJFrame.dispose();
						jframe.dispose();
					}
				} else {
					JOptionPane.showMessageDialog(null, 
							"The two passwords entered must be the same!"
						);
				}
			}
		});
		this.jpanel.add(this.registerButton);


		this.jframe.setVisible(true);
	}

	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowClosed(WindowEvent e) {}

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

