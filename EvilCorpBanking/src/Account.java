
public class Account {
	String no;
	String name;
	double bal;
	
	public Account() {
		no = "";
		name = "";
		bal = 0.0;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String accountNo) {
		no = accountNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String accountName) {
		name = accountName;
	}

	public double getBal() {
		return bal;
	}

	public void setBal(double accountBal) {
		bal = accountBal;
	}

}
