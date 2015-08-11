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
					Transaction temp3 = transactionList.get(i);
					transactionList.set(i, transactionList.get(j));
					transactionList.set(j, temp3);
				}
				if (transactionList.get(i).getDate().compareTo(transactionList.get(j).getDate()) == 0) {
					if ((!transactionList.get(i).getType().equals("deposit")) && transactionList.get(j).getType().equals("deposit")) {
						continue;
					} else if (transactionList.get(i).getType().equals("deposit") && (!transactionList.get(j).getType().equals("deposit"))){
						Transaction temp1 = transactionList.get(i);
						transactionList.set(i, transactionList.get(j));
						transactionList.set(j, temp1);
					} else if (transactionList.get(i).getAmount() < transactionList.get(j).getAmount()) {
						Transaction temp2 = transactionList.get(i);
						transactionList.set(i, transactionList.get(j));
						transactionList.set(j, temp2);
					}
				}
			}
		}
		return transactionList;
	}
	
}
