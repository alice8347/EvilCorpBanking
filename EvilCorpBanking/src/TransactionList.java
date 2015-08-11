import java.util.ArrayList;

public class TransactionList {
	private ArrayList<Transaction> transactionList;
	
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
				// Sort the date first.
				if (transactionList.get(i).getDate().compareTo(transactionList.get(j).getDate()) > 0) {
					Transaction temp3 = transactionList.get(i);
					transactionList.set(i, transactionList.get(j));
					transactionList.set(j, temp3);
				}
				
				// If the dates are the same, compare the transaction types and amounts.
				if (transactionList.get(i).getDate().compareTo(transactionList.get(j).getDate()) == 0) {
					// If the second transaction type is "add an account", swap the order.
					if (transactionList.get(j).getType().equals("add an account")) {
						Transaction temp1 = transactionList.get(i);
						transactionList.set(i, transactionList.get(j));
						transactionList.set(j, temp1);
						
					// If the second transaction type is "deposit" and the first one is any of the other types, keep the same order.
					} else if ((!transactionList.get(i).getType().equals("deposit")) && transactionList.get(j).getType().equals("deposit")) {
						continue;
					
					// if the first transaction type is "deposit" and the second one is any of the other types, swap the order.
					} else if (transactionList.get(i).getType().equals("deposit") && (!transactionList.get(j).getType().equals("deposit"))){
						Transaction temp2 = transactionList.get(i);
						transactionList.set(i, transactionList.get(j));
						transactionList.set(j, temp2);
						
					// If both of the transactions are not "deposit", put the one with larger amount first.
					} else if (transactionList.get(i).getAmount() < transactionList.get(j).getAmount()) {
						Transaction temp3 = transactionList.get(i);
						transactionList.set(i, transactionList.get(j));
						transactionList.set(j, temp3);
					}
				}
			}
		}
		return transactionList;
	}
	
}
