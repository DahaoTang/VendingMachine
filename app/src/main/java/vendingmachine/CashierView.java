package vendingmachine;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.io.File;
import java.io.FileWriter;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class CashierView {

	private Model model;
	private Controller controller;

	private JFrame jframe;
	private JPanel jpanel;

	private JButton userButton;

	private JLabel cashLabel;
	private JTable cashTable;
	private JScrollPane cashScrollPane;


	// WINDOW LAYNOUT DATA
	private final int[] WINDOW_SIZE = {600, 750};
	private final int[] USER_BUTTON_BP = {16, 16, 100, 36};

	private final int[] CASH_TABLE_COLUMN_WIDTH = {100, 30, 80, 30};

	private final int LABEL_FONT_SIZE = 20;
	private final String LABEL_FONT = "Arial";
	private final int LABEL_FONT_MODE = Font.PLAIN;

	private final int[] CASH_LABEL_BP = {18, 60, 300, 32};
	private final int[] CASH_SCROLL_PANE_BP = {18, 100, 564, 300};

	private final int[] ADD_BUTTON_BP = {495, 60, 90, 30};
	private final int[] DELETE_BUTTON_BP = {405, 60, 90, 30};


	public CashierView() {
		this.model = null;
		this.controller = null;

		this.jframe = new JFrame("Cashier Mode");
        this.jpanel = new JPanel();

		this.userButton = new JButton();

		this.cashLabel = new JLabel();
		this.cashTable = new JTable();
		this.cashScrollPane = new JScrollPane();
	}

	public void setController(Controller controller) {
		this.controller = controller;
		this.controller.setCashierView(this);
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
		 * ==================
		 * ### Cash Table ###
		 * ==================
		 * */

		// JLabel for cash table
		this.cashLabel.setText("Maitain Cashes: ");
		this.cashLabel.setBounds(
				CASH_LABEL_BP[0],
				CASH_LABEL_BP[1],
				CASH_LABEL_BP[2],
				CASH_LABEL_BP[3]
			);
		this.jpanel.add(this.cashLabel);

		System.out.println("Try to load table");

		// JTable
		updateCashTable();

		obtainReports();

		this.jframe.setVisible(true);
	}

	public void updateView() {
		updateCashTable();
	}



	/**
	 * ########################
	 * ### HELPER FUNCTIONS ###
	 * ########################
	 * */

	private void buildCashTable() {
		Object[][] cashData = new Object[this.model.getCashAllFromDB().size()][7];
		// Create table
		String[] cashTableColumnNames = {"Name", "-", "Amount", "+"};
		this.cashTable = new JTable(cashData, cashTableColumnNames) {

			@Override
			public Object getValueAt(int row, int column) {
				return cashData[row][column];
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				if (column == 1 || column == 3) return true;
				else return false;
			}

			@Override
			public void setValueAt(Object value, int row, int column) {
				cashData[row][column] = value;
			}
		};
		// Set column width
		for (int i = 0; i < CASH_TABLE_COLUMN_WIDTH.length; i++) {
			this.cashTable.getColumnModel().getColumn(i).setMaxWidth(CASH_TABLE_COLUMN_WIDTH[i]);
			this.cashTable.getColumnModel().getColumn(i).setMinWidth(CASH_TABLE_COLUMN_WIDTH[i]);
		}
		this.cashTable.setRowSelectionAllowed(false);
		// Set buttons
		this.cashTable.getColumnModel().getColumn(1).setCellEditor(new CashierTableButtonEditor(new JTextField(), this.controller));
		this.cashTable.getColumnModel().getColumn(1).setCellRenderer(new CashierTableButtonRenderer());
		this.cashTable.getColumnModel().getColumn(3).setCellEditor(new CashierTableButtonEditor(new JTextField(), this.controller));
		this.cashTable.getColumnModel().getColumn(3).setCellRenderer(new CashierTableButtonRenderer());
	}

	private void drawCashTable() {
		this.jpanel.remove(this.cashScrollPane);
		this.cashScrollPane = new JScrollPane(this.cashTable);
		this.cashScrollPane.setBounds(
				CASH_SCROLL_PANE_BP[0],
				CASH_SCROLL_PANE_BP[1],
				CASH_SCROLL_PANE_BP[2],
				CASH_SCROLL_PANE_BP[3]
			);
		this.cashScrollPane.repaint();
		this.cashScrollPane.setVisible(true);
		this.jpanel.add(this.cashScrollPane);
		this.jpanel.revalidate();
		this.jpanel.repaint();
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

	private void loadCashTableData() {
		ArrayList<Cash> cashListInDB = this.model.getCashAllFromDB();
		String[] cashNameList = {
			"$100", "$50", "$20", "$10", 
			"$5", "$2", "$1", "¢50", "¢20",
			"¢10", "¢5", "¢2", "¢1"
		};
		for (int i = 0; i < cashNameList.length; i++) {
			Cash c = new Cash();
			for (Cash c0: cashListInDB) {
				if (c0 == null || c0.getName() == null) continue;
				if (c0.getName().equals(cashNameList[i])) {
					c = c0;
					break;
				}
			}
			this.cashTable.setValueAt(c.getName(), i, 0);;
			this.cashTable.setValueAt("-", i, 1);;
			this.cashTable.setValueAt(c.getAmount(), i, 2);;
			this.cashTable.setValueAt("+", i, 3);;
		}
	}

	private void obtainReports() {
		LocalDateTime ldt = LocalDateTime.now();
		DateTimeFormatter ldtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String reportTail = ldtf.format(ldt);
		try {
			File current = new File("CashReport_Current" + reportTail + ".txt");
			FileWriter fw = new FileWriter(current);
			fw.write("From Cashier: " + this.model.getCurrentUser().getName() + "\n");
			for (Cash c: this.model.getCashAllFromDB()) {
				if (c.getAmount() == 0) continue;
				String msg = "";
				msg += "Name: " + c.getName() + ";\t";
				msg += "Amount left: " + c.getAmount() + ".\n";
				fw.write(msg);
			}
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateCashTable() {
		buildCashTable();
		loadCashTableData();
		drawCashTable();
	}


	/**
	 * ######################
	 * ### HELPER CLASSES ###
	 * ######################
	 * */

	/**
	 * Increase or decrease button renderer for cash table
	 * */
	class CashierTableButtonRenderer extends JButton implements TableCellRenderer {

		public CashierTableButtonRenderer() {
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
	 * Increase or decrease button editor for cash table
	 * */
	class CashierTableButtonEditor extends DefaultCellEditor {

		protected JButton button;
		private String label;
		private boolean isPushed;
		private JTable jtable;
		private Controller controller;

		public CashierTableButtonEditor(JTextField textField, Controller controller) {
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
				int value = Integer.parseInt(jtable.getValueAt(row, 2).toString());
				String cashName = jtable.getValueAt(row, 0).toString();	
				// Parse to Controller to update
				this.controller.updateCashAmountToDB(cashName, value, column);
				this.controller.updateViewCashier();
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

		@Override
		protected void fireEditingCanceled() {
			super.fireEditingCanceled();
		}
	}


}
