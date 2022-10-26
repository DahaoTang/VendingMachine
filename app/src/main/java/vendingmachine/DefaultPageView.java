package vendingmachine;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.HashMap;

public class DefaultPageView {

	private Model model;

	public DefaultPageView(Model model) {
		this.model = model;
	}

	public void launchWindow() {

		// Check if loggedin
		boolean loggedin = false;
		if (this.model.getCurrentUser() == null) {
			loggedin = false;
		} else {
			loggedin = true;
		}

		// Create a JFrame
		JFrame jframe = new JFrame("Vending Machine");
		jframe.setSize(600, 800);
		jframe.setResizable(true);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setLocationRelativeTo(null);

		// Create a JPanel
        JPanel jpanel = new JPanel();
        jpanel.setLayout(null);
        jframe.add(jpanel);

		// Create JButton for logged in user
		JButton userButton = null;
		if (loggedin) {
			userButton = new JButton("Login");
		} else {
			userButton = new JButton(this.model.getCurrentUser().getName());
		}
		userButton.setBounds(10, 10, 100, 32);
		jpanel.add(userButton);
		userButton.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {

				if (loggedin) {
					// TO-DO: Show User info page; be able to change password

				} else {
					// TO-DO: login page

				}

			}
		});

		// JLabel for recent products
		JLabel recentProductLabel = new JLabel("Top 5 Recent Products");
		recentProductLabel.setFont(new Font("Arial", Font.PLAIN, 20));	
		recentProductLabel.setBounds(18, 60, 400, 32);
		jpanel.add(recentProductLabel);

		// JTable for recent Products
		String[] recentProductsTableColumnNames = {
			"No.s", "Type", "Name", "Price", "", "", ""
		};
		HashMap<Product, Integer> recentProducts = this.model.getRecentProducts();
		JTable recentProductsTable = new JTable();
		jpanel.add(recentProductsTable);
		recentProductsTable.setValueAt("hello", 1, 1);

     
        jframe.setVisible(true);
	}



	
}
