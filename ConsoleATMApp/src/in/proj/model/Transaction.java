package in.proj.model;

public class Transaction {
	
	private String transactiontype;
	private Double money;
	public String getTransactiontype() {
		return transactiontype;
	}
	public void setTransactiontype(String transactiontype) {
		this.transactiontype = transactiontype;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	
	@Override
	public String toString() {
		return "Transaction [transactiontype=" + transactiontype + ", money=" + money
				+ "]";
	}
	
	
	
	
	

}
