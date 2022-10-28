package vendingmachine;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class DefaultPageView {

	private Model model;
	private Controller controller;

	private JFrame jframe;
	private JPanel jpanel;

	private JButton userButton;

	private JLabel recentProductsLabel;
	private JScrollPane recentProductsScrollPane;

	private JLabel groupedProductsLabel;
	private JScrollPane groupedProductsScrollPane;

	private JComboBox<String> groupedProductsTypeBox;
	private ProductType currentGroupedProductsType;

	// WINDOW LAYNOUT DATA
	private final int[] WINDOW_SIZE = {600, 800};
	private final int[] USER_BUTTON_BP = {10, 20, 100, 32};

	private final int[] PRODUCTS_TABLE_COLUMN_WIDTH = {50, 120, 210, 70, 20, 70, 20};

	private final int LABEL_FONT_SIZE = 20;
	private final String LABEL_FONT = "Arial";
	private final int LABEL_FONT_MODE = Font.PLAIN;

	private final int[] RECENT_PRODUCTS_LABEL_BP = {18, 60, 300, 32};
	private final int[] RECENT_PRODUCTS_SCROLL_PANE_BP = {18, 100, 564, 100};

	private final int[] GROUPED_PRODUCTS_TYPE_BOX_BP = {180, 221, 200, 32};

	private final int[] GROUPED_PRODUCTS_LABEL_BP = {18, 220, 200, 32};
	private final int[] GROUPED_PRODUCTS_SCROLL_PANE_BP = {18, 260, 564, 120};


	public DefaultPageView() {
		this.model = null;
		this.controller = null;

		this.jframe = new JFrame("Vending Machine");
        this.jpanel = new JPanel();

		this.userButton = new JButton();

		this.recentProductsLabel = new JLabel();
		this.recentProductsScrollPane = new JScrollPane();

		this.groupedProductsLabel = new JLabel();
		this.groupedProductsScrollPane = new JScrollPane();

		this.groupedProductsTypeBox = new JComboBox<String>();
		this.currentGroupedProductsType = ProductType.DRINK;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public void launchWindow() {

		// Set up JFrame
		this.jframe.setSize(WINDOW_SIZE[0], WINDOW_SIZE[1]);
		this.jframe.setResizable(true);
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
		updateUserButton();
		this.userButton.setBounds(USER_BUTTON_BP[0], USER_BUTTON_BP[1], USER_BUTTON_BP[2], USER_BUTTON_BP[3]);
		this.jpanel.add(userButton);
		if (this.model.getCurrentUser().getName() != null) {
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
	
		/**
		 * =======================
		 * ### Recent Products ###
		 * =======================
		 * */
		// JLabel for recent products
		this.recentProductsLabel.setText("Top 5 Recent Products");
		this.recentProductsLabel.setFont(new Font(LABEL_FONT, LABEL_FONT_MODE, LABEL_FONT_SIZE));	
		this.recentProductsLabel.setBounds(
				RECENT_PRODUCTS_LABEL_BP[0], 
				RECENT_PRODUCTS_LABEL_BP[1], 
				RECENT_PRODUCTS_LABEL_BP[2], 
				RECENT_PRODUCTS_LABEL_BP[3]
			);
		this.jpanel.add(this.recentProductsLabel);

		// JTable for recent Products
		updateRecentProductsTable();
	
		/**
		 * ========================
		 * ### Grouped Products ###
		 * ========================
		 * */
		// JLabel for list of porducts
		this.groupedProductsLabel.setText("List of Products: ");
		this.groupedProductsLabel.setFont(new Font(LABEL_FONT, LABEL_FONT_MODE, LABEL_FONT_SIZE));
		this.groupedProductsLabel.setBounds(
				GROUPED_PRODUCTS_LABEL_BP[0], 
				GROUPED_PRODUCTS_LABEL_BP[1], 
				GROUPED_PRODUCTS_LABEL_BP[2], 
				GROUPED_PRODUCTS_LABEL_BP[3]
			);
		this.jpanel.add(this.groupedProductsLabel);
	
		// JTable for grouped products
		this.groupedProductsTypeBox.setBounds(
				GROUPED_PRODUCTS_TYPE_BOX_BP[0], 
				GROUPED_PRODUCTS_TYPE_BOX_BP[1], 
				GROUPED_PRODUCTS_TYPE_BOX_BP[2], 
				GROUPED_PRODUCTS_TYPE_BOX_BP[3]
			);
		this.groupedProductsTypeBox.addItem("Drinks");
		this.groupedProductsTypeBox.addItem("Chocolates");;
		this.groupedProductsTypeBox.addItem("Chips");
		this.groupedProductsTypeBox.addItem("Candies");
		this.groupedProductsTypeBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Object source = e.getSource();
					if (source instanceof JComboBox) {
						JComboBox<?> cb = (JComboBox<?>)source;
						Object selectedItem = cb.getSelectedItem();
						if (selectedItem.equals("Drinks")) {
							updateGroupedProductsTable(ProductType.DRINK);
						} else if (selectedItem.equals("Chocolates")) {
							updateGroupedProductsTable(ProductType.CHOCOLATE);
						} else if (selectedItem.equals("Chips")) {
							updateGroupedProductsTable(ProductType.CHIP);
						} else if (selectedItem.equals("Candies")) {
							updateGroupedProductsTable(ProductType.CANDY);
						}
					}
				}
			}
		});
		// Set visible
		this.groupedProductsTypeBox.setVisible(true);
		// Add JScrollPane to JPanel
		this.jpanel.add(this.groupedProductsTypeBox);
     
        this.jframe.setVisible(true);
	}

	public void updateView() {

		// Update login button
		updateUserButton();

		// Update recent products table
		updateRecentProductsTable();

		// Update grouped products table
		updateGroupedProductsTable(this.currentGroupedProductsType);

		// Redraw jpanel and recentProductsTable
		this.jpanel.revalidate();
		this.jpanel.repaint();
	}


	/**
	 * ########################
	 * ### HELPER FUNCTIONS ###
	 * ########################
	 * */

	private void setColumnWidth(JTable table, int[] sizeArray) {
		for (int i = 0; i < sizeArray.length; i++) {
			table.getColumnModel().getColumn(i).setMaxWidth(sizeArray[i]);
			table.getColumnModel().getColumn(i).setMinWidth(sizeArray[i]);
		}
	}

	private void updateGroupedProductsTable(ProductType type) {
		// Update the type of the current grouped products
		this.currentGroupedProductsType = type;
		// update groupedProducts
		this.controller.updateGrouped(type);

		// Load and init table data
		HashMap<Product, Integer> products = this.model.getGroupedProducts();
		ArrayList<String> productNameList = new ArrayList<String>();
		for (Product p: products.keySet()) {
			productNameList.add(p.getName());
		}
		// Sort to fix the order
		productNameList.sort(Comparator.naturalOrder());
		Object[][] productsData = new Object[productNameList.size()][7];
		// Create table
		String[] productsTableColumnNames = {"No.", "Type", "Name", "Price", "-", "Amount", "+"};
		JTable productsTable = new JTable(productsData, productsTableColumnNames) {

			@Override
			public Object getValueAt(int row, int column) {
				return productsData[row][column];

			}

			@Override
			public boolean isCellEditable(int row, int column) {
				if (column == 4 || column == 6) return true;
				else return false;
			}

			@Override
			public void setValueAt(Object value, int row, int column) {
				productsData[row][column] = value;
			}
		};
		productsTable.setRowSelectionAllowed(false);
		// Update table data
		for (int i = 0; i < productNameList.size(); i++) {
			// Maintain order
			Product p = new Product();
			for (Product p0: products.keySet()) {
				if (p0.getName().equals(productNameList.get(i))) {
					p = p0;
					break;
				}
			}
			// Update table data
			productsTable.setValueAt(i+1, i, 0);
			productsTable.setValueAt(p.getTypeString(), i, 1);
			productsTable.setValueAt(p.getName(), i, 2);
			productsTable.setValueAt(p.getPrice(), i, 3);
			productsTable.setValueAt("-", i, 4);
			productsTable.setValueAt(products.get(p), i, 5);
			productsTable.setValueAt("+", i, 6);
		}
		// Set column width
		setColumnWidth(productsTable, PRODUCTS_TABLE_COLUMN_WIDTH);
		// Set buttons
		productsTable.getColumnModel().getColumn(4).setCellEditor(new ListedButtonEditor(new JTextField(), this.controller));
		productsTable.getColumnModel().getColumn(4).setCellRenderer(new ListedButtonRenderer());
		productsTable.getColumnModel().getColumn(6).setCellEditor(new ListedButtonEditor(new JTextField(), this.controller));
		productsTable.getColumnModel().getColumn(6).setCellRenderer(new ListedButtonRenderer());
		// Add to scroll panel
		this.jpanel.remove(this.groupedProductsScrollPane);
		this.groupedProductsScrollPane = new JScrollPane(productsTable);
		this.groupedProductsScrollPane.setBounds(
				GROUPED_PRODUCTS_SCROLL_PANE_BP[0], 
				GROUPED_PRODUCTS_SCROLL_PANE_BP[1], 
				GROUPED_PRODUCTS_SCROLL_PANE_BP[2], 
				GROUPED_PRODUCTS_SCROLL_PANE_BP[3]
			);
		this.groupedProductsScrollPane.setVisible(true);
		this.groupedProductsScrollPane.repaint();
		this.jpanel.add(this.groupedProductsScrollPane);
		this.jpanel.revalidate();
		this.jpanel.repaint();
	}

	private void updateRecentProductsTable() {
		// Update recent products
		this.controller.updateRecent();

		// Re-loead data from model
		HashMap<Product, Integer> products = this.model.getRecentProducts();
		ArrayList<String> productNameList = new ArrayList<String>();
		for (Product p: products.keySet()) {
			productNameList.add(p.getName());
		}
		// Sort to fix the order
		productNameList.sort(Comparator.naturalOrder());
		Object[][] productsData = new Object[productNameList.size()][7];
		// Create table
		String[] productsTableColumnNames = {"No.", "Type", "Name", "Price", "-", "Amount", "+"};
		JTable productsTable = new JTable(productsData, productsTableColumnNames) {

			@Override
			public Object getValueAt(int row, int column) {
				return productsData[row][column];

			}

			@Override
			public boolean isCellEditable(int row, int column) {
				if (column == 4 || column == 6) return true;
				else return false;
			}

			@Override
			public void setValueAt(Object value, int row, int column) {
				productsData[row][column] = value;
			}
		};
		productsTable.setRowSelectionAllowed(false);
		// Update table data
		for (int i = 0; i < productNameList.size(); i++) {
			// Maintain order
			Product p = new Product();
			for (Product p0: products.keySet()) {
				if (p0.getName().equals(productNameList.get(i))) {
					p = p0;
					break;
				}
			}
			// Update table data
			productsTable.setValueAt(i+1, i, 0);
			productsTable.setValueAt(p.getTypeString(), i, 1);
			productsTable.setValueAt(p.getName(), i, 2);
			productsTable.setValueAt(p.getPrice(), i, 3);
			productsTable.setValueAt("-", i, 4);
			productsTable.setValueAt(products.get(p), i, 5);
			productsTable.setValueAt("+", i, 6);
		}
		// Set column width
		setColumnWidth(productsTable, PRODUCTS_TABLE_COLUMN_WIDTH);
		// Set buttons
		productsTable.getColumnModel().getColumn(4).setCellEditor(new IODButtonEditor(new JTextField(), this.controller));
		productsTable.getColumnModel().getColumn(4).setCellRenderer(new IODButtonRenderer());
		productsTable.getColumnModel().getColumn(6).setCellEditor(new IODButtonEditor(new JTextField(), this.controller));
		productsTable.getColumnModel().getColumn(6).setCellRenderer(new IODButtonRenderer());
		// Add to scroll panel
		this.jpanel.remove(this.recentProductsScrollPane);
		this.recentProductsScrollPane = new JScrollPane(productsTable);
		this.recentProductsScrollPane.setBounds(
				RECENT_PRODUCTS_SCROLL_PANE_BP[0], 
				RECENT_PRODUCTS_SCROLL_PANE_BP[1], 
				RECENT_PRODUCTS_SCROLL_PANE_BP[2], 
				RECENT_PRODUCTS_SCROLL_PANE_BP[3]
			);
		this.recentProductsScrollPane.setVisible(true);
		this.recentProductsScrollPane.repaint();
		this.jpanel.add(this.recentProductsScrollPane);
		this.jpanel.revalidate();
		this.jpanel.repaint();
	}

	private void updateUserButton() {
		if (this.model.getCurrentUser().getName() == null) {
			this.userButton.setText("Login");;
		} else {
			this.userButton.setText(this.model.getCurrentUser().getName());
		}
	}


	/**
	 * ######################
	 * ### HELPER CLASSES ###
	 * ######################
	 * */

	/**
	 * Increase or decrease button renderer for recentProductsTable
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
	 * Increase or decrease button editor for recentProductsTable
	 * */
	class IODButtonEditor extends DefaultCellEditor {

		protected JButton button;
		private String label;
		private boolean isPushed;
		private JTable jtable;
		private Controller controller;

		public IODButtonEditor(JTextField textField, Controller controller) {
			super(textField);
			this.button = new JButton();
			this.setClickCountToStart(1);
			this.button.setOpaque(true);
			this.button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					fireEditingStopped();
				}
			});
			this.controller = controller;
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
				System.out.println("update recent: " + productName + " " + value);
				this.controller.updateRecentAmount(productName, value, column);
				updateView();
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

	/**
	 * Increase or decrease button renderer for groupedProductsTable
	 * */
	class ListedButtonRenderer extends JButton implements TableCellRenderer {

		public ListedButtonRenderer() {
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
	 * Increase or decrease button editor for groupedProductsTable
	 * */
	class ListedButtonEditor extends DefaultCellEditor {

		protected JButton button;
		private String label;
		private boolean isPushed;
		private JTable jtable;
		private Controller controller;

		public ListedButtonEditor(JTextField textField, Controller controller) {
			super(textField);
			this.button = new JButton();
			this.setClickCountToStart(1);
			this.button.setOpaque(true);
			this.button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					fireEditingStopped();
				}
			});
			this.controller = controller;
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
				System.out.println("update grouped: " + productName + " " + value);
				this.controller.updateGroupedAmount(productName, value, column);
				updateView();
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


