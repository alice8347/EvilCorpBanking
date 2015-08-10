import java.util.ArrayList;

public class TransactionList {
	ArrayList<Transaction> transactionList;
	
	public TransactionList() {
		transactionList = new ArrayList<Transaction>();
	}
	
	public Transaction getTransaction(int index) {
		return transactionList.get(index);
	}
	
	public void addTransaction(Transaction transaction) {
		transactionList.add(transaction);
	}
	
	public int size() {
		return transactionList.size();
	}
	
	public ArrayList<Transaction> sort() {
		for (int i = 0; i < transactionList.size(); i++) {
			for (int j = i; j < transactionList.size(); j++) {
				if (transactionList.get(i).getDate().compareTo(transactionList.get(j).getDate()) > 0) {
					Transaction temp = transactionList.get(i);
					transactionList.set(i, transactionList.get(j));
					transactionList.set(j, temp);
				}
			}
		}
		return transactionList;
	}
	
}
