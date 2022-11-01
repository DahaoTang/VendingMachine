package vendingmachine;

import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;

public class OwnerView {

	private Model model;
	private Controller controller;

	private JFrame jframe;
	private JPanel jpanel;

	private JButton userButton;

	private JLabel usersLabel;
	private JTable  usersTable;
	private JScrollPane usersScrollPane;

	private JButton addButton;
	private JButton deleteButton;

	private JButton confirmButton;


	// WINDOW LAYNOUT DATA
	private final int[] WINDOW_SIZE = {600, 750};
	private final int[] USER_BUTTON_BP = {16, 16, 100, 36};


	private final int[] USERS_LABEL_BP = {18, 60, 300, 32};
	// private final int[] USERS_TABLE_COLUMN_WIDTH = {50, 120, 180, 120};
	private final int[] USERS_SCROLL_PANE_BP = {18, 100, 564, 300};

	private final int[] ADD_BUTTON_BP = {495, 60, 90, 30};
	private final int[] DELETE_BUTTON_BP = {405, 60, 90, 30};

	private final int[] CONFIRM_BUTTON_BP = {405, 420, 90, 30};


	public OwnerView() {
		this.model = null;
		this.controller = null;

		this.jframe = new JFrame("Owner Mode");
        this.jpanel = new JPanel();

		this.userButton = new JButton();

		this.usersLabel = new JLabel();
		this.usersTable = new JTable();
		this.usersScrollPane = new JScrollPane();

		this.addButton = new JButton();
		this.deleteButton = new JButton();
		this.confirmButton = new JButton();

	}

	public void setController(Controller controller) {
		this.controller = controller;
		this.controller.setOwnerView(this);
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public void launchWindow() {
		/**
		 * ===================
		 * ### Basic Setup ###
		 * ===================
		 * */
		// Set up JFrame
		this.jframe.setSize(WINDOW_SIZE[0], WINDOW_SIZE[1]);
		this.jframe.setResizable(false);
		this.jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.jframe.setLocationRelativeTo(null);

		// Set up JPanel
        this.jpanel.setLayout(null);
        this.jframe.add(this.jpanel);


		/**
		 * =========================
		 * ### Login / User Info ###
		 * =========================
		 * */
		// Set up JButton for logged in user
		this.userButton.setBounds(
				USER_BUTTON_BP[0], 
				USER_BUTTON_BP[1], 
				USER_BUTTON_BP[2], 
				USER_BUTTON_BP[3]
			);
		// updateUserButton();
		this.userButton.setText(this.model.getCurrentUser().getName());
		this.userButton.addActionListener(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					launchTryToLogOutWindow();	
				}
			});
		this.jpanel.add(this.userButton);


		/**
		 * ===================
		 * ### Users Table ###
		 * ===================
		 * */

		this.usersLabel.setText("Maintain Users: ");
		this.usersLabel.setBounds(
				USERS_LABEL_BP[0],
				USERS_LABEL_BP[1],
				USERS_LABEL_BP[2],
				USERS_LABEL_BP[3]
			);
		this.jpanel.add(this.usersLabel);

		this.addButton.setText("Add");
		this.addButton.setBounds(
				ADD_BUTTON_BP[0],
				ADD_BUTTON_BP[1],
				ADD_BUTTON_BP[2],
				ADD_BUTTON_BP[3]
			);
		this.addButton.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				launchTryToAddUserWindow();
				System.out.println(usersTable.getValueAt(1, 2));
			}
		});
		this.jpanel.add(this.addButton);

		this.deleteButton.setText("Delete");
		this.deleteButton.setBounds(
				DELETE_BUTTON_BP[0],
				DELETE_BUTTON_BP[1],
				DELETE_BUTTON_BP[2],
				DELETE_BUTTON_BP[3]
			);
		this.deleteButton.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				launchTryToDeleteUserWindow();
			}
		});
		this.jpanel.add(this.deleteButton);

		// JTable
		updateUserTable();	

		/**
		 * ======================
		 * ### Confirm Button ###
		 * ======================
		 * */

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
				launchTryToConfirmWindow();	
			}
		});
		this.jpanel.add(this.confirmButton);

		this.jframe.setVisible(true);
	}

	public void updateView() {
		updateUserTable();
	}



	/**
	 * ########################
	 * ### HELPER FUNCTIONS ###
	 * ########################
	 * */

	public void buildUsersTable() {
		String[] userTableColumnNames = {"User Type", "Name", "Password", "Card Name"};
		Object[][] userData = new Object[this.model.getUserAllFromDB().size()][userTableColumnNames.length];
System.out.println("user in database: "  +this.model.getUserAllFromDB().size());
System.out.println("get all users once");
		this.usersTable = new JTable(userData, userTableColumnNames) {

			@Override
			public Object getValueAt(int row, int column) {
				return userData[row][column];
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				if (column == 1) return false;
				else return true;
			}

			@Override
			public void setValueAt(Object value, int row, int column) {
				userData[row][column] = value;
			}
		};
	}

	private void loadUsersTableData() {
		ArrayList<User> userListInDB = this.model.getUserAllFromDB();
		ArrayList<String> userNameList = new ArrayList<String>();
		for (User u: userListInDB) {
			userNameList.add(u.getName());
		}
		userNameList.sort(Comparator.naturalOrder());
		for (int i = 0; i < userNameList.size(); i++) {
			// Maintain order
			User u = new User();
			for (User u0: userListInDB) {
				if (u0 == null || u0.getName() == null) continue;
				if (u0.getName().equals(userNameList.get(i))) {
					u = u0;
					break;
				}
			}
			// Set table data
			this.usersTable.setValueAt(u.getTypeString(), i, 0);
			this.usersTable.setValueAt(u.getName(), i, 1);
			this.usersTable.setValueAt(u.getPassword(), i, 2);
			this.usersTable.setValueAt(u.getCardName(), i, 3);
			System.out.println("data loaded: " + u.toString());
		}
	}

	private void drawUserstable() {
		this.jpanel.remove(this.usersScrollPane);
		this.usersScrollPane = new JScrollPane(this.usersTable);
		this.usersScrollPane.setBounds(
				USERS_SCROLL_PANE_BP[0],
				USERS_SCROLL_PANE_BP[1],
				USERS_SCROLL_PANE_BP[2],
				USERS_SCROLL_PANE_BP[3]
			);
		this.usersScrollPane.repaint();
		this.usersScrollPane.setVisible(true);
		this.jpanel.add(this.usersScrollPane);
		this.jpanel.revalidate();
		this.jpanel.repaint();
	}

	public void launchTryToAddUserWindow() {
		String userDataString = JOptionPane.showInputDialog("Enter user data in the following format: \n" + 
					"<TYPE>;<NAME>;<PASSWORD>"
				);
		if (userDataString == null) return;
		String[] userData = userDataString.split(";");
		if (userData.length != 3) {
			JOptionPane.showMessageDialog(null,"Wrong format!");
			return;
		}

		UserType type = null;
		if (userData[0].toUpperCase().equals("NORMAL"))type = UserType.NORMAL;
		if (userData[0].toUpperCase().equals("SELLER")) type = UserType.SELLER; 
		if (userData[0].toUpperCase().equals("CASHIER")) type = UserType.CASHIER; 
		if (userData[0].toUpperCase().equals("OWNER")) type = UserType.OWNER; 
		if (type == null) {
			JOptionPane.showMessageDialog(null,"Wrong TYPE format!");
			return;
		}
		String name = userData[1];
		if (this.controller.ifHasUserInDB(name)) {
			JOptionPane.showMessageDialog(null,"Already have a user with the same name.");
			return;
		}

		ArrayList<Product> recentProducts = new ArrayList<Product>();
		recentProducts.add(new Product());
		recentProducts.add(new Product());
		recentProducts.add(new Product());
		recentProducts.add(new Product());
		recentProducts.add(new Product());
		this.controller.insertUserToDB(new User(name, userData[2], recentProducts, type, null));
		JOptionPane.showMessageDialog(null,"User added successfully!");
		updateView();
	}

	public void launchTryToDeleteUserWindow() {
		String userName = JOptionPane.showInputDialog(null, "Enter the name of the user to delete: ");
		if (this.controller.ifHasUserInDB(userName)) {
			if (userName.equals("Owner")) {
				JOptionPane.showMessageDialog(null, "Owner cannot be deleted!");
			} else {
				this.controller.deleteUserInDB(userName);
				JOptionPane.showMessageDialog(null, userName + " deleted successfully.");
				this.controller.updateViewOwner();
			}
		} else if (userName == null) {
			return;
		} else {
			JOptionPane.showMessageDialog(null, "No such user!");
		}

	}

	private void launchTryToLogOutWindow() {
		Object[] options = {"OK", "Log Out"};
		Object answer = JOptionPane.showOptionDialog(
				null, 
				"Current User: " + model.getCurrentUser().getName(), 
				"User Info", 
				JOptionPane.DEFAULT_OPTION, 
				JOptionPane.INFORMATION_MESSAGE, 
				null, 
				options, 
				options[0]
			);
		if (answer.equals(1)) {
			System.out.println("Log Out");
			jframe.dispose();
			controller.restart();
		} else {
			System.out.println("OK");
		}
	}

	private void launchTryToConfirmWindow() {
		for (int row = 0; row < this.model.getUserAllFromDB().size(); row++) {
			System.out.println("row: " + row);
			// Get new data
			String newTypeString = (String)this.usersTable.getValueAt(row, 0);
			newTypeString = newTypeString.toUpperCase();
			UserType newType = null;
			String newPassword = (String)this.usersTable.getValueAt(row, 2);
			String newCardName = (String)this.usersTable.getValueAt(row, 3);
			// Get data from database
			String userName = (String)this.usersTable.getValueAt(row, 1);
			User user = this.model.getUserFromDB(userName);
			// Cannot give seller, cashier and owner cards
			if (!user.getType().equals(UserType.NORMAL)) newCardName = null;

			// Set User Type
			if (newTypeString.equals("NORMAL")) newType = UserType.NORMAL;
			else if (newTypeString.equals("SELLER")) newType = UserType.SELLER;
			else if (newTypeString.equals("CASHIER")) newType = UserType.CASHIER;
			else if (newTypeString.equals("OWNER")) newType = UserType.OWNER;
			else {
				JOptionPane.showMessageDialog(null, "Invalid user type!");
				return;
			}
			user.setType(newType);

			// Set Password
			user.setPassword(newPassword);

			// Set Card Name
			if (newCardName == null || controller.ifHasCardGlobal(newCardName)) {
				user.setCardName(newCardName);
			} else {
				JOptionPane.showMessageDialog(null, "Invalid Card Name!");
				return;
			}

			this.controller.updateUserToDB(user);
		}
		JOptionPane.showMessageDialog(null, "Updated Successfully!");
	}

	public void updateUserTable() {
		buildUsersTable();
		loadUsersTableData();
		drawUserstable();
	}
}

