package vendingmachine;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import java.util.ArrayList;
import java.util.HashMap;

public class CardPayView {

	private Model model;
	private Controller controller;

	private JFrame defaultPageViewJFrame;

	private JFrame jframe;
	private JPanel jpanel;

	private JLabel cardNameLabel;
	private JTextField cardNameTextField;

	private JLabel cardNumberLabel;
	private JPasswordField cardNumberField;

	private JButton backButton;
	private JButton confirmButton;
	private JButton cancelButton;


	// DATA
	
	private final int[] WINDOW_SIZE = {300, 200};

	private final int[] CARD_NAME_LABEL_BP = {20, 10, 80, 32};
	private final int[] CARD_NAME_TEXT_FEILD_BP = {90, 10, 190, 32};

	private final int[] CARD_NUMBER_LABEL_BP = {20, 50, 80, 32};
	private final int[] CARD_NUMBER_FEILD_BP = {90, 50, 190, 32};

	private final int[] BACK_BUTTON_BP = {20, 95, 120, 32};
	private final int[] CONFIRM_BUTTON_BP = {160, 95, 120, 32};	
	private final int[] CANCEL_BUTTON_BP = {20, 125, 260, 32};	

	public CardPayView(Model model, Controller controller, JFrame defaultPageViewJFrame) {
		this.model = model;
		this.controller = controller;
		this.controller.setCardPayView(this);
		this.defaultPageViewJFrame = defaultPageViewJFrame;

		this.jframe = new JFrame();
		this.jpanel = new JPanel();

		this.cardNameLabel = new JLabel();
		this.cardNameTextField = new JTextField();

		this.cardNumberLabel = new JLabel();
		this.cardNumberField = new JPasswordField();

		this.backButton = new JButton();
		this.confirmButton = new JButton();
		this.cancelButton = new JButton();
	}

	public void launchWindow() {
		/**
		 * ===================
		 * ### Basic Setup ###
		 * ===================
		 * */
		this.jframe.setTitle("Cash Pay");
		this.jframe.setSize(WINDOW_SIZE[0], WINDOW_SIZE[1]);;
		this.jframe.setResizable(false);
		this.jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.jframe.setLocationRelativeTo(null);

		this.jpanel.setLayout(null);
		this.jframe.add(this.jpanel);

		/**
		 * =================
		 * ### Card Name ###
		 * =================
		 * */
		this.cardNameLabel.setText("Name: ");
		this.cardNameLabel.setBounds(
				CARD_NAME_LABEL_BP[0],
				CARD_NAME_LABEL_BP[1],
				CARD_NAME_LABEL_BP[2],
				CARD_NAME_LABEL_BP[3]
			);
		this.jpanel.add(this.cardNameLabel);

		this.cardNameTextField.setBounds(
				CARD_NAME_TEXT_FEILD_BP[0],
				CARD_NAME_TEXT_FEILD_BP[1],
				CARD_NAME_TEXT_FEILD_BP[2],
				CARD_NAME_TEXT_FEILD_BP[3]
			);
		this.jpanel.add(this.cardNameTextField);


		/**
		 * =================
		 * ### Card Name ###
		 * =================
		 * */
		this.cardNumberLabel.setText("Number: ");
		this.cardNumberLabel.setBounds(
				CARD_NUMBER_LABEL_BP[0],
				CARD_NUMBER_LABEL_BP[1],
				CARD_NUMBER_LABEL_BP[2],
				CARD_NUMBER_LABEL_BP[3]
			);
		this.jpanel.add(this.cardNumberLabel);

		this.cardNumberField.setBounds(
				CARD_NUMBER_FEILD_BP[0],
				CARD_NUMBER_FEILD_BP[1],
				CARD_NUMBER_FEILD_BP[2],
				CARD_NUMBER_FEILD_BP[3]
			);
		this.jpanel.add(this.cardNumberField);

		/**
		 * ===============
		 * ### Buttons ###
		 * ===============
		 * */
		
		// Back
		this.backButton.setText("Back");
		this.backButton.setBounds(
				BACK_BUTTON_BP[0],
				BACK_BUTTON_BP[1],
				BACK_BUTTON_BP[2],
				BACK_BUTTON_BP[3]
			);
		this.backButton.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
System.out.println("Back clicked");
				jframe.dispose();
				Object[] options = {"Cash", "Card"};
				Object answer = JOptionPane.showOptionDialog(
						null, 
						"Choose way of paying: ",
						"Payment",
						JOptionPane.DEFAULT_OPTION, 
						JOptionPane.INFORMATION_MESSAGE, 
						null, 
						options, 
						null
					);
				if (answer.equals(0)) {
					// Pay in cash
System.out.println("Pay in cash");
					CashPayView cashPayView = new CashPayView(model, controller, jframe);
					cashPayView.launchWindow();

				} else {
					// Pay with card
System.out.println("Pay in card");
					CardPayView cardPayView = new CardPayView(model, controller, jframe);
					cardPayView.launchWindow();

				}

			}
		});
		this.jpanel.add(this.backButton);

		// Confirm
		this.confirmButton.setText("Confirm");
		this.confirmButton.setBounds(
				CONFIRM_BUTTON_BP[0],
				CONFIRM_BUTTON_BP[1],
				CONFIRM_BUTTON_BP[2],
				CONFIRM_BUTTON_BP[3]
			);
		this.confirmButton.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				String name = cardNameTextField.getText();
				String number = cardNumberField.getText();
				Boolean inCardInfoMap = false;
				String numberToMatch = null;
				for (String n: model.getCardInfoMap().keySet()) {
					if (n.equals(name)) {
						inCardInfoMap = true;
						numberToMatch = model.getCardInfoMap().get(n);
						break;
					}
				}
				if (inCardInfoMap) {
					if (number.equals(numberToMatch)) {
						if (!controller.ifHasCard(name)) {
							Object[] options = {"No", "Yes"};
							Object answer = JOptionPane.showOptionDialog(
									null, 
									"Do you want to save card info?", 
									"Warning", 
									JOptionPane.DEFAULT_OPTION, 
									JOptionPane.WARNING_MESSAGE, 
									null, 
									options, 
									options[0]
								);
							if (answer.equals(0)) {
		System.out.println("No");
							} else {
		System.out.println("Yes");
								controller.updateCardInDB(name, number);	
								JOptionPane.showMessageDialog(null, "Card info added successfully!");
							}
						}
						JOptionPane.showMessageDialog(null, "Payment Successful!");
						controller.confirmCardPay();
						restart();	

					} else {
						JOptionPane.showMessageDialog(null, "Invalid card name or number!");
					}
				} else {
					JOptionPane.showMessageDialog(null, "Invalid card name or number!");
				}
			}
		});
		this.jpanel.add(this.confirmButton);

		// Cancel
		this.cancelButton.setText("Cancel Order");
		this.cancelButton.setBounds(
				CANCEL_BUTTON_BP[0],
				CANCEL_BUTTON_BP[1],
				CANCEL_BUTTON_BP[2],
				CANCEL_BUTTON_BP[3]
			);
		this.cancelButton.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
System.out.println("Cancel clicked");
				Object[] options = {"No", "Yes"};
				Object answer = JOptionPane.showOptionDialog(
							null, 
							"Are you sure to cancel the order?", 
							"Warning", 
							JOptionPane.DEFAULT_OPTION, 
							JOptionPane.WARNING_MESSAGE, 
							null, 
							options, 
							options[0]
						);
					if (answer.equals(0)) {
System.out.println("No");
					} else {
System.out.println("Yes");
						restart();	
					}

			}
		});
		this.jpanel.add(this.cancelButton);


		this.jframe.setVisible(true);
	}


	/**
	 * ########################
	 * ### HELPER FUNCTIONS ###
	 * ########################
	 * */
	private void restart() {
		defaultPageViewJFrame.dispose();
		jframe.dispose();
		this.controller.restart();
	}
}

