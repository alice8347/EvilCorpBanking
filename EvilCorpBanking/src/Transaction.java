import java.util.Date;

public class Transaction {
	String type;
	String no;
	double amount;
	Date date;
	
	public Transaction() {
		type = "";
		no = "";
		amount = 0.0;
		date = new Date();
	}

	public String getType() {
		return type;
	}

	public void setType(String transactionType) {
		if (transactionType.length() < 3) {
			if (transactionType.toUpperCase().charAt(0) == 'A') {
				type = "add an account";
			} else if (transactionType.toUpperCase().charAt(0) == 'C') {
				type = "check";
			} else if (transactionType.toUpperCase().charAt(0) == 'R') {
				type = "remove an account";
			} else {
				type = "withdrawal";
			}
		} else {
			if (transactionType.substring(0, 3).equalsIgnoreCase("DEB")) {
				type = "debit card";
			} else {
				type = "deposit";
			}
		}
	}

	public String getNo() {
		return no;
	}

	public void setNo(String accountNo) {
		no = accountNo;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double transactionAmount) {
		amount = transactionAmount;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date transactionDate) {
		date = transactionDate;
	}
	
}
