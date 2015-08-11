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
		AccountList accountTemp = new AccountList();
		
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
				System.out.println("The balance for account " + no + " is " + getFormattedCurrency(accountSum.get(no)));;
			}
			transactionType = Validator.getType(sc, "Enter a transaction type (Check, Debit card, Deposit, Withdrawal, Add an account or Remove an account) or -1 to finish: ");
			while (!transactionType.equals("-1")) {
				Transaction transaction = new Transaction();
				transaction.setType(transactionType);
				transactionProcess:
				if (transaction.getType().equals("add an account")) {
					System.out.println("Please create the user account(s)");
					accountNo = Validator.getString(sc, "Enter an account # or -1 to stop entering accounts: ");
					
					while (!accountNo.equals("-1")) {
						while (accountSum.containsKey(accountNo)) {
							System.out.println("Account already exists. Try again.");
							accountNo = Validator.getString(sc, "Enter an account # or -1 to stop entering accounts: ");
							if (accountNo.equals("-1")) {
								System.out.println("The balance for account " + accountNo + " is " + getFormattedCurrency(accountSum.get(accountNo)));
								break transactionProcess;
							}
						}
						Account accountNew = new Account();
						accountNew.setNo(accountNo);
						accountNew.setName(Validator.getString(sc, "Enter the name for acct # " + accountNo + ": "));
						accountBal = Validator.getDouble(sc, "Enter the balance for acct # " + accountNo + ": ", 0);
						accountNew.setBal(accountBal);
						accounts.addAccount(accountNew);
						accountSum.put(accountNew.getNo(), accountNew.getBal());
						System.out.println("\n\n\n");
						accountNo = Validator.getString(sc, "Enter an account # or -1 to stop entering accounts: ");
					}
				} else if (transaction.getType().equals("check") || transaction.getType().equals("debit card") || transaction.getType().equals("deposit") || transaction.getType().equals("withdrawal")) {
					accountNo = Validator.getString(sc, "Enter the account #: ");
					while (!accountSum.containsKey(accountNo)) {
						System.out.println("Account does not exist. Try again.");
						accountNo = Validator.getString(sc, "Enter the account #: ");
					}
					transaction.setNo(accountNo);
					transactionAmount = Validator.getDouble(sc, "Enter the amount of the \"" + transaction.getType() + "\": ", 0);
					transaction.setAmount(transactionAmount);
					transactionDate = Validator.getDate(sc, "Enter the date of the \"" + transaction.getType() + "\" (M/d/yyyy): ");
					transaction.setDate(transactionDate);
					transactionList.addTransaction(transaction);
				} else {
					accountNo = Validator.getString(sc, "Enter the account #: ");
					while (accounts.getAccountBal(accountNo) != 0.0) {	// The account can only be removed if nothing is in the account at the beginning.
						System.out.println("Transaction denied. The balance is not equal to zero. Try another account.");
						accountNo = Validator.getString(sc, "Enter the account # or -1 to stop closing the account: ");
						if (accountNo.equals("-1")) {
							break transactionProcess;
						}
					}
					transaction.setNo(accountNo);
					transaction.setAmount(0.0);
					transactionDate = Validator.getDate(sc, "Enter the date of the \"" + transaction.getType() + "\" (M/d/yyyy): ");
					transaction.setDate(transactionDate);
					transactionList.addTransaction(transaction);
					accounts.removeAccount(accountNo);
					accountSum.remove(accountNo);
				}
				
				for (int i = 0; i < accounts.getSize(); i++) {
					accountTemp.addAccount(new Account(accounts.getAccount(i)));
				}

				showBal(accountTemp, transactionList);

				transactionType = Validator.getType(sc, "Enter a transaction type (Check, Debit card, Deposit, Withdrawal, Add an account or Remove an account) or -1 to finish: ");
			}
			
			System.out.println();
			for (int i = 0; i < accounts.getSize(); i++) {
				accountTemp.addAccount(new Account(accounts.getAccount(i)));
			}

			AccountList accountsEnd = new AccountList(calculateBal(accountTemp, transactionList));

			accountSum.clear();
			for (int i = 0; i < accountsEnd.getSize(); i++) {
				accountSum.put(accountsEnd.getAccount(i).getNo(), accountsEnd.getAccount(i).getBal());
			}
			for (String no : accountSum.keySet()) {
				System.out.println("The balance for account " + no + " is " + getFormattedCurrency(accountSum.get(no)));;
			}
			
			System.out.println();
			System.out.println("=======================================================");
			System.out.println("Transaction summary: ");
			System.out.println();
			SimpleDateFormat date = new SimpleDateFormat("M/d/yyyy");
			System.out.printf("%-14s%-11s%-21s%s%n", "Date", "Account #", "Type", "Amount");
			System.out.println("-------------------------------------------------------");
			for (int i = 0; i < transactionList.size(); i++) {	// Print the transactions in the order they were processed.
				System.out.printf("%-14s%-11s%-21s%s%n", date.format(transactionList.getTransaction(i).getDate()), transactionList.getTransaction(i).getNo(), transactionList.getTransaction(i).getType(), getFormattedCurrency(transactionList.getTransaction(i).getAmount()));
			}
			System.out.println("=======================================================");
			
			accountsEnd.setAccount();
		} catch (NullPointerException e) {
			System.err.println(e.getMessage());
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	public static AccountList calculateBal(AccountList accountTemp, TransactionList transactionList) {
		transactionList.sort();
		for (int i = 0; i < transactionList.size(); i++) {
			for (int j = 0; j < accountTemp.getSize(); j++) {
				if (transactionList.getTransaction(i).getNo().equals(accountTemp.getAccount(j).getNo())) {
					double balance = accountTemp.getAccount(j).getBal();
					if (transactionList.getTransaction(i).getType().equals("add an account")) {
						break;
					} else if (transactionList.getTransaction(i).getType().equals("check") || transactionList.getTransaction(i).getType().equals("debit card") || transactionList.getTransaction(i).getType().equals("withdrawal")) {
						balance -= transactionList.getTransaction(i).getAmount();
						if (balance < 0) {
							balance -= 35;	// Allow overdraft but charge $35.
						}
						accountTemp.getAccount(j).setBal(balance);
					} else if (transactionList.getTransaction(i).getType().equals("deposit")){
						balance += transactionList.getTransaction(i).getAmount();
						accountTemp.getAccount(j).setBal(balance);
					} else {
						accountTemp.removeAccount(transactionList.getTransaction(i).getNo());
					}
				}
			}
		}
		return accountTemp;
	}
	
	public static void showBal(AccountList accountTemp, TransactionList transactionList) {
		System.out.println();
		accountTemp = calculateBal(accountTemp, transactionList);
		HashMap<String, Double> accountSum =  new HashMap<String, Double>();
		for (int i = 0; i < accountTemp.getSize(); i++) {
			accountSum.put(accountTemp.getAccount(i).getNo(), accountTemp.getAccount(i).getBal());
		}
		HashMap<String, Transaction> transactionSum = new HashMap<String, Transaction>();
		for (int i = 0; i < transactionList.size(); i++) {
			transactionSum.put(transactionList.getTransaction(i).getNo(), transactionList.getTransaction(i));
		}
		for (String no : accountSum.keySet()) {	// Print the account balance if the account is in the transaction list. Adding an account is not a transaction.
			if (transactionSum.containsKey(no)) {
				System.out.println("The balance for account " + no + " is " + getFormattedCurrency(accountSum.get(no)) + ". (Pending)");
			}
		}
	}
	
	public static String getFormattedCurrency(double amount) {
		DecimalFormat format = new DecimalFormat("$#,##0.00;-$#,##0.00");
		String formatted = format.format(amount);
		return formatted;
	}

}
