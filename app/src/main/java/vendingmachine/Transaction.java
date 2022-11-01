package vendingmachine;

import java.time.*;
import java.util.HashMap;

public class Transaction {

	private LocalDateTime ldt;
	private Boolean ifCancelled;
	private HashMap<Product, Integer> itemsSold;
	private HashMap<Cash, Integer> moneyPaid;
	private HashMap<Cash, Integer> changeRetruned;
	private String paymentMethod;
	private String warningsReceived;

	public Transaction() {
		this.ldt = null;
		this.ifCancelled = false;
		this.itemsSold = new HashMap<Product, Integer>();
		this.moneyPaid = new HashMap<Cash, Integer>();
		this.changeRetruned = new HashMap<Cash, Integer>();
		this.paymentMethod = "";
		this.warningsReceived = "";
	}

	public LocalDateTime getLdt() {
		return this.ldt;
	}

	public void setLdt(LocalDateTime ldt) {
		this.ldt = ldt;
	}

	public Boolean getIfCancelled() {
		return this.ifCancelled;
	}

	public void SetIfCancelled(Boolean ifCancelled) {
		this.ifCancelled = ifCancelled;
	}

	public HashMap<Product, Integer> getItemsSold() {
		return this.itemsSold;
	}
	
	public void setItemsSold(HashMap<Product, Integer> itemsSold) {
		this.itemsSold = itemsSold;
	}

	public HashMap<Cash, Integer> getMoneyPaid() {
		return this.moneyPaid;
	}

	public void setMoneyPaid(HashMap<Cash, Integer> moneyPaid) {
		this.moneyPaid = moneyPaid;
	}

	public HashMap<Cash, Integer> getChangeReturned() {
		return this.changeRetruned;
	}

	public void setChangeReturned(HashMap<Cash ,Integer> changeRetruned) {
		this.changeRetruned = changeRetruned;
	}

	public String getPaymentMethod() {
		return this.paymentMethod;
	}

	public void setPeymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getWarningsReceived() {
		return this.warningsReceived;
	}

	public void setWarningsReceived(String warningsReceived) {
		this.warningsReceived = warningsReceived;
	}
}
