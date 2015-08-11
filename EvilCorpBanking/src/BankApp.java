import java.util.Scanner;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.text.DecimalFormat;

public class BankApp {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		HashMap<String, Double> accountSum = new HashMap<String, Double>();
		AccountList accounts = new AccountList();
		
		TransactionList transactionList = new TransactionList();
		String accountNo = "", transactionType = "";
		double accountBal = 0.0;
		double transactionAmount = 0.0;
		Date transactionDate = new Date();
		
		System.out.println("Welcome to Evil Corp Savings and Loan");
		
		try {
			accounts.readAccount();
			
			for (int i = 0; i < accounts.getSize(); i++) {
				accountSum.put(accounts.getAccount(i).getNo(), accounts.getAccount(i).getBal());
			}
			
			System.out.println();
			System.out.println();
			
			for (String no : accountSum.keySet()) {
				System.out.println("Account " + no + "'s Balance: " + getFormattedCurrency(accountSum.get(no)));;
			}
			transactionType = Validator.getString(sc, "Enter a transaction type (Check, Debit card, Deposit, Withdrawal, Add an account or Remove an account) or -1 to finish : ");
			while (!transactionType.equals("-1")) {
				Transaction transaction = new Transaction();
				transaction.setType(transactionType);
				if (transactionType.equals("add an account")) {
					System.out.println("Please create the user account(s)");
					accountNo = Validator.getString(sc, "Enter an account # or -1 to stop entering accounts : ");
					
					while (!accountNo.equals("-1")) {
						while (accountSum.containsKey(accountNo)) {
							System.out.println("Account already exists. Try again.");
							accountNo = Validator.getString(sc, "Enter an account # or -1 to stop entering accounts : ");
						}
						Account accountNew = new Account();
						accountNew.setNo(accountNo);
						transaction.setNo(accountNo);
						accountNew.setName(Validator.getString(sc, "Enter the name for acct # " + accountNo + " : "));
						accountBal = Validator.getDouble(sc, "Enter the balance for acct # " + accountNo + " : ", 0);
						accountNew.setBal(accountBal);
						accounts.addAccount(accountNew);
						transaction.setAmount(accountBal);
						transactionDate = Validator.getDate(sc, "Enter the date of the " + transaction.getType() + ": ");
						transaction.setDate(transactionDate);
						transactionList.addTransaction(transaction);
						System.out.println("\n\n\n");
						accountNo = Validator.getString(sc, "Enter an account # or -1 to stop entering accounts : ");
					}
				} else if (transactionType.equals("check") || transactionType.equals("debit card") || transactionType.equals("deposit") || transactionType.equals("withdrawal")) {
					accountNo = Validator.getString(sc, "Enter the account # : ");
					while (!accountSum.containsKey(accountNo)) {
						System.out.println("Account does not exist. Try again.");
						accountNo = Validator.getString(sc, "Enter the account # : ");
					}
					transaction.setNo(accountNo);
					transactionAmount = Validator.getDouble(sc, "Enter the amount of the " + transaction.getType() + ": ", 0);
					transaction.setAmount(transactionAmount);
					transactionDate = Validator.getDate(sc, "Enter the date of the " + transaction.getType() + ": ");
					transaction.setDate(transactionDate);
					transactionList.addTransaction(transaction);
				} else {
					accountNo = Validator.getString(sc, "Enter the account # : ");
					while (accounts.getAccountBal(accountNo) != 0.0) {
						System.out.println("Transaction denied. The balance is not equal to zero. Try again.");
						accountNo = Validator.getString(sc, "Enter the account # : ");
					}
					transaction.setNo(accountNo);
					transaction.setAmount(0.0);
					transactionDate = Validator.getDate(sc, "Enter the date of the " + transaction.getType() + ": ");
					transaction.setDate(transactionDate);
					transactionList.addTransaction(transaction);
				}
				transactionType = Validator.getString(sc, "Enter a transaction type (Check, Debit card, Deposit, Withdrawal, Add an account or Remove an account) or -1 to finish : ");
			}
			
			transactionList.sort();
			for (int i = 0; i < transactionList.size(); i++) {
				for (int j = 0; j < accounts.getSize(); j++) {
					if (transactionList.getTransaction(i).getNo().equals(accounts.getAccount(j).getNo())) {
						double balance = accounts.getAccount(j).getBal();
						if (transactionList.getTransaction(i).getType().equals("add an account")) {
							break;
						} else if (transactionList.getTransaction(i).getType().equals("check") || transactionList.getTransaction(i).getType().equals("debit card") || transactionList.getTransaction(i).getType().equals("withdrawal")) {
							balance -= transactionList.getTransaction(i).getAmount();
							if (balance < 0) {
								balance -= 35;
							}
							accounts.getAccount(j).setBal(balance);
						} else if (transactionList.getTransaction(i).getType().equals("deposit")){
							balance += transactionList.getTransaction(i).getAmount();
							accounts.getAccount(j).setBal(balance);
						} else {
							accounts.removeAccount(transactionList.getTransaction(i).getNo());
							j--;
						}
					}
				}
			}
			System.out.println();
			for (int i = 0; i < accounts.getSize(); i++) {
				accountSum.put(accounts.getAccount(i).getNo(), accounts.getAccount(i).getBal());
			}
			HashMap<String, Transaction> transactionSum = new HashMap<String, Transaction>();
			for (int i = 0; i < transactionList.size(); i++) {
				transactionSum.put(transactionList.getTransaction(i).getNo(), transactionList.getTransaction(i));
			}
			for (String no : accountSum.keySet()) {
				if (transactionSum.containsKey(no)) {
					System.out.println("The balance for account " + no + " is " + getFormattedCurrency(accountSum.get(no)));
				}
			}
			
			System.out.println();
			System.out.println("=======================================================");
			System.out.println("Transaction summary: ");
			System.out.println();
			SimpleDateFormat date = new SimpleDateFormat("M/d/yyyy");
			System.out.printf("%-14s%-11s%-21s%s%n", "Date", "Account #", "Type", "Amount");
			System.out.println("-------------------------------------------------------");
			for (int i = 0; i < transactionList.size(); i++) {
				System.out.printf("%-14s%-11s%-21s%s%n", date.format(transactionList.getTransaction(i).getDate()), transactionList.getTransaction(i).getNo(), transactionList.getTransaction(i).getType(), getFormattedCurrency(transactionList.getTransaction(i).getAmount()));
			}
			System.out.println("=======================================================");
			
			accounts.setAccount();
		} catch (NullPointerException e) {
			System.err.println(e.getMessage());
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	public static String getFormattedCurrency(double amount) {
		DecimalFormat format = new DecimalFormat("$#,##0.00;-$#,##0.00");
		String formatted = format.format(amount);
		return formatted;
	}

}
