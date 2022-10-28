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

	private JLabel recentProductsLabel;
	private JTable recentProductsTable;
	private JScrollPane recentProductsJScrollPane;

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
	private final int[] RECENT_PRODUCTS_SCROLL_PANEL_BP = {18, 100, 564, 100};

	private final int[] GROUPED_PRODUCTS_TYPE_BOX_BP = {180, 221, 200, 32};

	private final int[] GROUPED_PRODUCTS_LABEL_BP = {18, 220, 200, 32};
	private final int[] GROUPED_PRODUCTS_SCROLL_PANEL_BP = {18, 260, 564, 120};


	public DefaultPageView() {
		this.model = null;
		this.controller = null;

		this.jframe = new JFrame("Vending Machine");
        this.jpanel = new JPanel();

		this.userButton = new JButton();

		this.recentProductsLabel = new JLabel();
		this.recentProductsTable = null;
		this.recentProductsJScrollPane = null;

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
		String[] productsTableColumnNames = {"No.", "Type", "Name", "Price", "-", "Amount", "+"};
		Object[][] recentProductsTableData = new Object[5][7];
		this.recentProductsTable = new JTable(recentProductsTableData, productsTableColumnNames) {

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
		// Load data into recentProductsTable
		updateRecentProductsTable();
		// Set recentProductsTable size
		setColumnWidth(recentProductsTable, PRODUCTS_TABLE_COLUMN_WIDTH);
		// Set buttons for recentProductsTable
		recentProductsTable.getColumnModel().getColumn(4).setCellEditor(new IODButtonEditor(new JTextField()));
		recentProductsTable.getColumnModel().getColumn(4).setCellRenderer(new IODButtonRenderer());
		recentProductsTable.getColumnModel().getColumn(6).setCellEditor(new IODButtonEditor(new JTextField()));
		recentProductsTable.getColumnModel().getColumn(6).setCellRenderer(new IODButtonRenderer());
		recentProductsTable.setRowSelectionAllowed(false);
		// Add to a JScrollPane
		this.recentProductsJScrollPane = new JScrollPane(recentProductsTable);
		this.recentProductsJScrollPane.setBounds(
				RECENT_PRODUCTS_SCROLL_PANEL_BP[0], 
				RECENT_PRODUCTS_SCROLL_PANEL_BP[1], 
				RECENT_PRODUCTS_SCROLL_PANEL_BP[2], 
				RECENT_PRODUCTS_SCROLL_PANEL_BP[3]
			);
		// Set visible
		this.recentProductsJScrollPane.setVisible(true);
		// Add the JScrollPane to JPanel
		this.jpanel.add(this.recentProductsJScrollPane);

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
		updateGroupedProductsTable(this.currentGroupedProductsType);

		// Redraw jpanel and recentProductsTable
		this.recentProductsTable.repaint();
		this.groupedProductsScrollPane.repaint();
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
		// Update currentGroupedProductsType
		this.currentGroupedProductsType = type;
		// Load and init table data
		String[] productsTableColumnNames = {"No.", "Type", "Name", "Price", "-", "Amount", "+"};
		ArrayList<Product> productsList = this.model.getProductsByType(type);
		Object[][] productsData = new Object[productsList.size()][7];
		// Create table
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
		this.controller.updateGroupedFromTypeChange(type);
		for (int i = 0; i < productsList.size(); i++) {
			productsTable.setValueAt(i+1, i, 0);
			productsTable.setValueAt(productsList.get(i).getTypeString(), i, 1);
			productsTable.setValueAt(productsList.get(i).getName(), i, 2);
			productsTable.setValueAt(productsList.get(i).getPrice(), i, 3);
			productsTable.setValueAt("-", i, 4);
			for (Product p: this.model.getGroupedProducts().keySet()) {
				if (p.getName().equals(productsList.get(i).getName())) {
					productsTable.setValueAt(this.model.getGroupedProducts().get(p), i, 5);
				} else {
					productsTable.setValueAt(0, i, 5);
				}
			}
			productsTable.setValueAt("+", i, 6);
		}
		// Set column width
		setColumnWidth(productsTable, PRODUCTS_TABLE_COLUMN_WIDTH);
		// Set buttons
		productsTable.getColumnModel().getColumn(4).setCellEditor(new ListedButtonEditor(new JTextField()));
		productsTable.getColumnModel().getColumn(4).setCellRenderer(new ListedButtonRenderer());
		productsTable.getColumnModel().getColumn(6).setCellEditor(new ListedButtonEditor(new JTextField()));
		productsTable.getColumnModel().getColumn(6).setCellRenderer(new ListedButtonRenderer());

		// Add to scroll panel
		this.jpanel.remove(this.groupedProductsScrollPane);
		this.groupedProductsScrollPane = new JScrollPane(productsTable);
		this.groupedProductsScrollPane.setBounds(
				GROUPED_PRODUCTS_SCROLL_PANEL_BP[0], 
				GROUPED_PRODUCTS_SCROLL_PANEL_BP[1], 
				GROUPED_PRODUCTS_SCROLL_PANEL_BP[2], 
				GROUPED_PRODUCTS_SCROLL_PANEL_BP[3]
			);
		this.groupedProductsScrollPane.setVisible(true);
		this.jpanel.add(this.groupedProductsScrollPane);
	}

	private void updateRecentProductsTable() {
		HashMap<Product, Integer> recentProducts = this.model.getRecentProducts();
		ArrayList<Product> recentProductsList = new ArrayList<Product>();
		for (Product p: recentProducts.keySet()) {
			recentProductsList.add(p);
		}
		for (int i = 0; i < 5; i++) {
			this.recentProductsTable.setValueAt(i+1, i, 0);
			this.recentProductsTable.setValueAt(recentProductsList.get(i).getTypeString(), i, 1);
			this.recentProductsTable.setValueAt(recentProductsList.get(i).getName(), i, 2);
			this.recentProductsTable.setValueAt(recentProductsList.get(i).getPrice(), i, 3);
			this.recentProductsTable.setValueAt("-", i, 4);
			this.recentProductsTable.setValueAt(recentProducts.get(recentProductsList.get(i)), i, 5);
			this.recentProductsTable.setValueAt("+", i, 6);
		}
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

		public ListedButtonEditor(JTextField textField) {
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


