package vendingmachine;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DefaultPageView {

	private Model model;

	public DefaultPageView(Model model) {
		this.model = model;
	}

	public void launchWindow() {

		// Check if loggedin
		boolean loggedin = false;
		if (this.model.getCurrentUser().getName() == null) {
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
			userButton = new JButton(this.model.getCurrentUser().getName());
		} else {
			userButton = new JButton("Login");
		}
		userButton.setBounds(10, 10, 100, 32);
		jpanel.add(userButton);
		if (loggedin) {
			// TO-DO: Show User info page; be able to change password
			userButton.addActionListener(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent ae) {

				}
			});

		} else {
			// TO-DO: login page
			userButton.addActionListener(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					System.out.println("Login clicked");

				}
			});
		}
	
		// JLabel for recent products
		JLabel recentProductLabel = new JLabel("Top 5 Recent Products");
		recentProductLabel.setFont(new Font("Arial", Font.PLAIN, 20));	
		recentProductLabel.setBounds(18, 60, 400, 32);
		jpanel.add(recentProductLabel);

		// JTable for recent Products
		// Init data
		String[] recentProductsTableColumnNames = {"No.", "Type", "Name", "Price", "-", "Amount", "+"};
		HashMap<Product, Integer> recentProducts = this.model.getRecentProducts();
		ArrayList<Product> recentProductsList = new ArrayList<Product>();
		for (Product p: recentProducts.keySet()) {
			recentProductsList.add(p);
		}
		Object[][] recentProductsTableData = {
			{1, recentProductsList.get(0).getTypeString(), recentProductsList.get(0).getName() ,recentProductsList.get(0).getPrice(), "-", recentProducts.get(recentProductsList.get(0)), "+"},
			{2, recentProductsList.get(1).getTypeString(), recentProductsList.get(1).getName() ,recentProductsList.get(1).getPrice(), "-", recentProducts.get(recentProductsList.get(1)), "+"},
			{3, recentProductsList.get(2).getTypeString(), recentProductsList.get(2).getName() ,recentProductsList.get(2).getPrice(), "-", recentProducts.get(recentProductsList.get(2)), "+"},
			{4, recentProductsList.get(3).getTypeString(), recentProductsList.get(3).getName() ,recentProductsList.get(3).getPrice(), "-", recentProducts.get(recentProductsList.get(3)), "+"},
			{5, recentProductsList.get(4).getTypeString(), recentProductsList.get(4).getName() ,recentProductsList.get(4).getPrice(), "-", recentProducts.get(recentProductsList.get(4)), "+"},
		};
		JTable recentProductsTable = new JTable(recentProductsTableData, recentProductsTableColumnNames) {
			@Override
			public Object getValueAt(int row, int column) {
				return recentProductsTableData[row][column];
			}
			@Override
			public boolean isCellEditable(int row, int column) {
				// if (column == 5) return true;
				// else return false;
				return true;
			}
			@Override
			public void setValueAt(Object value, int row, int column) {
				recentProductsTableData[row][column] = value;
			}
		};
		// Set size
		recentProductsTable.getColumnModel().getColumn(0).setPreferredWidth(34);
		recentProductsTable.getColumnModel().getColumn(1).setPreferredWidth(150);
		recentProductsTable.getColumnModel().getColumn(2).setPreferredWidth(200);
		recentProductsTable.getColumnModel().getColumn(3).setPreferredWidth(70);
		recentProductsTable.getColumnModel().getColumn(4).setPreferredWidth(5);
		recentProductsTable.getColumnModel().getColumn(5).setPreferredWidth(70);
		recentProductsTable.getColumnModel().getColumn(6).setPreferredWidth(5);
		recentProductsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		// Set buttons
		recentProductsTable.getColumnModel().getColumn(4).setCellEditor(new IODButtonEditor(new JTextField()));
		// recentProductsTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JCheckBox()));
		// recentProductsTable.getColumn("-").setCellEditor(new ButtonEditor(new JCheckBox()));
		recentProductsTable.getColumnModel().getColumn(4).setCellRenderer(new IODButtonRenderer());
		recentProductsTable.getColumnModel().getColumn(6).setCellEditor(new IODButtonEditor(new JTextField()));
		// recentProductsTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JCheckBox()));
		// recentProductsTable.getColumn("+").setCellEditor(new ButtonEditor(new JCheckBox()));
		recentProductsTable.getColumnModel().getColumn(6).setCellRenderer(new IODButtonRenderer());
		recentProductsTable.setRowSelectionAllowed(false);
		// Add to a JScrollPane
		JScrollPane recentProductsJScrollPane = new JScrollPane(recentProductsTable);
		recentProductsJScrollPane.setBounds(18, 100, 564, 100);
		// Add JScrollPane to JPanel
		jpanel.add(recentProductsJScrollPane);
		recentProductsJScrollPane.setVisible(true);
     
        jframe.setVisible(true);
	}

	/**
	 * ########################
	 * ### HELPER FUNCTIONS ###
	 * ########################
	 * */

	/**
	 * Increase or decrease button renderer
	 * */
	class IODButtonRenderer extends JButton implements TableCellRenderer {

		public IODButtonRenderer() {
			setOpaque(true);
		}
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			if (isSelected) {
				setForeground(table.getSelectionForeground());
				setBackground(table.getSelectionBackground());
			} else {
				setForeground(table.getForeground());
				setBackground(UIManager.getColor("Button.background"));
			}
			setText((value == null) ? "" : value.toString());
			return this;
		}
	}

	class IODButtonEditor extends DefaultCellEditor {

		protected JButton button;
		private String label;
		private boolean isPushed;

		public IODButtonEditor(JTextField textField) {
			super(textField);
			this.button = new JButton();
			this.button.setOpaque(true);
			this.button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					fireEditingStopped();
				}
			});
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			if (isSelected) {
				button.setForeground(table.getSelectionForeground());
				button.setBackground(table.getSelectionBackground());
			} else {
				button.setForeground(table.getForeground());
				button.setBackground(table.getBackground());
			}
			label = (value == null) ? "" : value.toString();
			button.setText(label);
			isPushed = true;
			return button;
		}

		@Override
		public Object getCellEditorValue() {
			if (isPushed) {
				JOptionPane.showMessageDialog(button, label + ": Ouch!");
			}
			isPushed = false;
			return new String(label);
		}

		@Override
		public boolean stopCellEditing() {
			isPushed = false;
			return super.stopCellEditing();
		}

		@Override
		protected void fireEditingStopped() {
			super.fireEditingStopped();
		}
		
	}
}


