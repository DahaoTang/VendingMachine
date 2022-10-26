package vendingmachine;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DefaultPageView {

	private Model model;
	private Controller controller;

	private JFrame jframe;
	private JPanel jpanel;

	private JButton userButton;
	private JLabel recentProductLabel;
	private JTable recentProductsTable;

	public DefaultPageView() {
		this.model = null;
		this.controller = null;

		this.jframe = new JFrame("Vending Machine");
        this.jpanel = new JPanel();

		this.userButton = null;
		this.recentProductLabel = null;
		this.recentProductsTable = null;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public void setModel(Model model) {
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

		// Set JFrame
		this.jframe.setSize(600, 800);
		this.jframe.setResizable(true);
		this.jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.jframe.setLocationRelativeTo(null);

		// Set JPanel
        this.jpanel.setLayout(null);
        this.jframe.add(this.jpanel);

		// Create JButton for logged in user
		if (loggedin) {
			this.userButton = new JButton(this.model.getCurrentUser().getName());
		} else {
			this.userButton = new JButton("Login");
		}
		this.userButton.setBounds(10, 10, 100, 32);
		this.jpanel.add(userButton);
		if (loggedin) {
			// TO-DO: Show User info page; be able to change password
			this.userButton.addActionListener(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent ae) {



				}
			});

		} else {
			// TO-DO: login page
			this.userButton.addActionListener(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent ae) {
					System.out.println("Login clicked");

				}
			});
		}
	
		// JLabel for recent products
		this.recentProductLabel = new JLabel("Top 5 Recent Products");
		this.recentProductLabel.setFont(new Font("Arial", Font.PLAIN, 20));	
		this.recentProductLabel.setBounds(18, 60, 400, 32);
		this.jpanel.add(this.recentProductLabel);

		// JTable for recent Products
		// Init data
		String[] recentProductsTableColumnNames = {"No.", "Type", "Name", "Price", "-", "Amount", "+"};
		HashMap<Product, Integer> recentProducts = this.model.getRecentProducts();
		ArrayList<Product> recentProductsList = new ArrayList<Product>();
		for (Product p: recentProducts.keySet()) {
			recentProductsList.add(p);
		}
		Object[][] recentProductsTableData = {
			{1, recentProductsList.get(0).getTypeString(), recentProductsList.get(0).getName(), 
				recentProductsList.get(0).getPrice(), "-", recentProducts.get(recentProductsList.get(0)), "+"},
			{2, recentProductsList.get(1).getTypeString(), recentProductsList.get(1).getName(), 
				recentProductsList.get(1).getPrice(), "-", recentProducts.get(recentProductsList.get(1)), "+"},
			{3, recentProductsList.get(2).getTypeString(), recentProductsList.get(2).getName(), 
				recentProductsList.get(2).getPrice(), "-", recentProducts.get(recentProductsList.get(2)), "+"},
			{4, recentProductsList.get(3).getTypeString(), recentProductsList.get(3).getName(), 
				recentProductsList.get(3).getPrice(), "-", recentProducts.get(recentProductsList.get(3)), "+"},
			{5, recentProductsList.get(4).getTypeString(), recentProductsList.get(4).getName(), 
				recentProductsList.get(4).getPrice(), "-", recentProducts.get(recentProductsList.get(4)), "+"},
		};
		this.recentProductsTable = new JTable(recentProductsTableData, recentProductsTableColumnNames) {

			@Override
			public Object getValueAt(int row, int column) {
				return recentProductsTableData[row][column];
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				if (column == 4 || column == 6) return true;
				else return false;
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
		recentProductsTable.getColumnModel().getColumn(4).setCellRenderer(new IODButtonRenderer());
		recentProductsTable.getColumnModel().getColumn(6).setCellEditor(new IODButtonEditor(new JTextField()));
		recentProductsTable.getColumnModel().getColumn(6).setCellRenderer(new IODButtonRenderer());
		recentProductsTable.setRowSelectionAllowed(false);
		// Add to a JScrollPane
		JScrollPane recentProductsJScrollPane = new JScrollPane(recentProductsTable);
		recentProductsJScrollPane.setBounds(18, 100, 564, 100);
		// Add JScrollPane to JPanel
		this.jpanel.add(recentProductsJScrollPane);
		recentProductsJScrollPane.setVisible(true);

     
        this.jframe.setVisible(true);
	}

	public void updateView() {
		// Retrieve data from model
		loadDataFromModleToRecentProductsTable();

		// Redraw jpanel and recentProductsTable
		this.jpanel.revalidate();
		this.jpanel.repaint();
		this.recentProductsTable.repaint();
	}


	/**
	 * ########################
	 * ### HELPER FUNCTIONS ###
	 * ########################
	 * */
	private void loadDataFromModleToRecentProductsTable() {
		HashMap<Product, Integer> recentProducts = this.model.getRecentProducts();
		ArrayList<Product> recentProductsList = new ArrayList<Product>();
		for (Product p: recentProducts.keySet()) {
			recentProductsList.add(p);
		}
		// Set data to recentProductsTable
		this.recentProductsTable.setValueAt(recentProducts.get(recentProductsList.get(0)), 0, 5);
		this.recentProductsTable.setValueAt(recentProducts.get(recentProductsList.get(1)), 1, 5);
		this.recentProductsTable.setValueAt(recentProducts.get(recentProductsList.get(2)), 2, 5);
		this.recentProductsTable.setValueAt(recentProducts.get(recentProductsList.get(3)), 3, 5);
		this.recentProductsTable.setValueAt(recentProducts.get(recentProductsList.get(4)), 4, 5);
	}


	/**
	 * ######################
	 * ### HELPER CLASSES ###
	 * ######################
	 * */

	/**
	 * Increase or decrease button renderer
	 * */
	class IODButtonRenderer extends JButton implements TableCellRenderer {

		public IODButtonRenderer() {
			setOpaque(true);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, 
				boolean isSelected, boolean hasFocus, int row, int column) {
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

	/**
	 * Increase or decrease button editor
	 * */
	class IODButtonEditor extends DefaultCellEditor {

		protected JButton button;
		private String label;
		private boolean isPushed;
		private JTable jtable;

		public IODButtonEditor(JTextField textField) {
			super(textField);
			this.button = new JButton();
			this.setClickCountToStart(1);
			this.button.setOpaque(true);
			this.button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					fireEditingStopped();
				}
			});
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, 
				boolean isSelected, int row, int column) {
			if (isSelected) {
				this.button.setForeground(table.getSelectionForeground());
				this.button.setBackground(table.getSelectionBackground());
			} else {
				this.button.setForeground(table.getForeground());
				this.button.setBackground(table.getBackground());
			}
			this.label = (value == null) ? "" : value.toString();
			this.button.setText(label);
			this.isPushed = true;
			this.jtable = table;
			return this.button;
		}

		@Override
		public Object getCellEditorValue() {
			if (isPushed) {
				// Get data from JTable
				int row = jtable.getSelectedRow();
				int column = jtable.getSelectedColumn();
				int value = Integer.parseInt(jtable.getValueAt(row, 5).toString());
				String productName = jtable.getValueAt(row, 2).toString();	
				// Parse to Controller to update
				controller.updateRecentAmount(productName, value, column);
			}
			this.isPushed = false;
			return new String(label);
		}

		@Override
		public boolean stopCellEditing() {
			this.isPushed = false;
			return super.stopCellEditing();
		}

		@Override
		protected void fireEditingStopped() {
			super.fireEditingStopped();
		}
	}

}


