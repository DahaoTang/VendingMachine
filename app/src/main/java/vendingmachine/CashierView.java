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

	private final int[] CASH_TABLE_COLUMN_WIDTH = {50, 120, 180, 70, 20, 70, 20};

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

		this.jframe = new JFrame("Seller Mode");
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


		this.jframe.setVisible(true);
	}
}
