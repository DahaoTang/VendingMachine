package vendingmachine;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import java.util.ArrayList;
import java.util.HashMap;

public class CashPayView {

	private Model model;
	private Controller controller;

	private JFrame defaultPageViewJFrame;

	private JFrame jframe;
	private JPanel jpanel;

	private JLabel instructionLabel;

	private JTable cashTable;
	private JScrollPane cashScrollPane;

	private JLabel totalPriceLabel;
	private JLabel currentPriceLabel;
	private JLabel leftPriceLabel;

	private JButton cancelButton;
	private JButton confirmButton;

	// DATA
	
	private final int[] WINDOW_SIZE = {300, 500};

	private final int LABEL_FONT_SIZE = 16;
	private final String LABEL_FONT = "Arial";
	private final int LABEL_FONT_MODE = Font.PLAIN;

	private final int[] INSTRUCTION_LABEL_BP = {20, 10, 200, 32};

	private final int[] CASH_TABLE_COLUMN_WIDTH = {100, 30, 80, 30};
	private final int[] CASH_SCROLL_PANE_BP = {20, 40, 260, 230};

	private final int[] TOTAL_PRICE_BP = {20, 270, 200, 20};
	private final int[] CURRENT_PRICE_BP = {20, 290, 200, 20};
	private final int[] LEFT_PRICE_BP = {20, 310, 200, 20};

	private final int[] CANCEL_BUTTON_BP = {20, 340, 100, 32};
	private final int[] CONFIRM_BUTTON_BP = {20, 370, 100, 32};


	public CashPayView(Model model, Controller controller, JFrame defaultPageViewJFrame) {
		this.model = model;
		this.controller = controller;
		this.controller.setCashPayView(this);
		this.defaultPageViewJFrame = defaultPageViewJFrame;

		this.jframe = new JFrame();
		this.jpanel = new JPanel();

		this.instructionLabel = new JLabel();

		this.cashTable = null;
		this.cashScrollPane = new JScrollPane();

		this.totalPriceLabel = new JLabel();
		this.currentPriceLabel = new JLabel();
		this.leftPriceLabel = new JLabel();

		this.cancelButton = new JButton();
		this.confirmButton = new JButton();
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
		 * ===================
		 * ### Instruction ###
		 * ===================
		 * */
		this.instructionLabel.setText("Please insert cash: ");
		this.instructionLabel.setFont(new Font(LABEL_FONT, LABEL_FONT_MODE, LABEL_FONT_SIZE));
		this.instructionLabel.setBounds(
				INSTRUCTION_LABEL_BP[0],
				INSTRUCTION_LABEL_BP[1],
				INSTRUCTION_LABEL_BP[2],
				INSTRUCTION_LABEL_BP[3]
			);
		this.jpanel.add(this.instructionLabel);

		/**
		 * ==================
		 * ### Cash Table ###
		 * ==================
		 * */
		buildCashTable();
		loadCashTableData();
		drawCashTable();


		/**
		 * =============
		 * ### PRICE ###
		 * =============
		 * */
		updatePrice();

		/**
		 * =========================
		 * ### Confirm or Cancel ###
		 * =========================
		 * */

		this.cancelButton.setText("Cancel");
		this.cancelButton.setBounds(
				CANCEL_BUTTON_BP[0],		
				CANCEL_BUTTON_BP[1],		
				CANCEL_BUTTON_BP[2],		
				CANCEL_BUTTON_BP[3]
			);


		this.jframe.setVisible(true);
	}

	public void updateView() {
		loadCashTableData();
		drawCashTable();
		updatePrice();
	}

	/**
	 * ########################
	 * ### HELPER FUNCTIONS ###
	 * ########################
	 * */

	private void buildCashTable() {
		// Loead init data from model
		int cashDataLength = 0;
		for (Cash c: this.model.getCashMap().keySet()) {
			if (c != null && c.getName() != null) cashDataLength++;
		}
		Object[][] cashData = new Object[cashDataLength][7];
System.out.println("CashPayView: buildCashTable: table row length: " + cashData.length);
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
		this.cashTable.getColumnModel().getColumn(1).setCellEditor(new CashButtonEditor(new JTextField(), this.controller));
		this.cashTable.getColumnModel().getColumn(1).setCellRenderer(new CashButtonRenderer());
		this.cashTable.getColumnModel().getColumn(3).setCellEditor(new CashButtonEditor(new JTextField(), this.controller));
		this.cashTable.getColumnModel().getColumn(3).setCellRenderer(new CashButtonRenderer());
	}

	private void drawCashTable() {
System.out.println("CashPayView: drawCashTable");
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

	private void loadCashTableData() {
System.out.println("CashPayView: loadCashTableData");
		HashMap<Cash, Integer> cashMap = this.model.getCashMap();
		ArrayList<String> cashNameList = new ArrayList<String>();
		cashNameList.add("$100");
		cashNameList.add("$50");
		cashNameList.add("$20");
		cashNameList.add("$10");
		cashNameList.add("$5");
		cashNameList.add("$2");
		cashNameList.add("$1");
		cashNameList.add("¢50");
		cashNameList.add("¢20");
		cashNameList.add("¢10");
		cashNameList.add("¢5");
		cashNameList.add("¢2");
		cashNameList.add("¢1");
		for (int i = 0; i < cashNameList.size(); i++) {
			Cash c = new Cash();
			for (Cash c0: cashMap.keySet()) {
				if (c0 == null || c0.getName() == null) continue;
				if (c0.getName().equals(cashNameList.get(i))) {
					c = c0;
					break;
				}
			}
			this.cashTable.setValueAt(c.getName(), i, 0);;
			this.cashTable.setValueAt("-", i, 1);;
			this.cashTable.setValueAt(cashMap.get(c), i, 2);;
			this.cashTable.setValueAt("+", i, 3);;
		}
	}

	public void updatePrice() {
		// Calcualte data
		Double totalPrice = this.model.getTotalPrice();
		Double currentPrice = this.model.getCurrentPrice();
		Double leftPrice = totalPrice - currentPrice;

		// Draw
		this.jpanel.remove(this.totalPriceLabel);
		this.totalPriceLabel.setText("Total Price: $ " + totalPrice);
		this.totalPriceLabel.setBounds(
				TOTAL_PRICE_BP[0],
				TOTAL_PRICE_BP[1],
				TOTAL_PRICE_BP[2],
				TOTAL_PRICE_BP[3]
			);
		this.jpanel.add(this.totalPriceLabel);

		this.jpanel.remove(this.currentPriceLabel);
		this.currentPriceLabel.setText("Current Value: $ " + String.format("%.2f", currentPrice));
		this.currentPriceLabel.setBounds(
				CURRENT_PRICE_BP[0],
				CURRENT_PRICE_BP[1],
				CURRENT_PRICE_BP[2],
				CURRENT_PRICE_BP[3]
			);
		this.jpanel.add(this.currentPriceLabel);

		this.jpanel.remove(this.leftPriceLabel);
		this.leftPriceLabel.setText("Value left: $ " + String.format("%.2f", leftPrice));
		this.leftPriceLabel.setBounds(
				LEFT_PRICE_BP[0],
				LEFT_PRICE_BP[1],
				LEFT_PRICE_BP[2],
				LEFT_PRICE_BP[3]
			);
		this.jpanel.add(this.leftPriceLabel);

		this.jpanel.revalidate();
		this.jpanel.repaint();
	}



	/**
	 * ######################
	 * ### HELPER CLASSES ###
	 * ######################
	 * */

	/**
	 * Increase or decrease button renderer for cash table
	 * */
	class CashButtonRenderer extends JButton implements TableCellRenderer {

		public CashButtonRenderer() {
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
	class CashButtonEditor extends DefaultCellEditor {

		protected JButton button;
		private String label;
		private boolean isPushed;
		private JTable jtable;
		private Controller controller;

		public CashButtonEditor(JTextField textField, Controller controller) {
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
			System.out.println("CashButtonEditor created");
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
				System.out.println("CashButtonEditor pushed");
				int row = jtable.getSelectedRow();
				int column = jtable.getSelectedColumn();
				int value = Integer.parseInt(jtable.getValueAt(row, 2).toString());
				String cashName = jtable.getValueAt(row, 0).toString();	
				// Parse to Controller to update
				System.out.println(cashName + ": " + value);
				this.controller.updateCashAmount(cashName, value, column);
				this.controller.updateViewCashPay();
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

