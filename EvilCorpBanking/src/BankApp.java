import java.util.Scanner;
import java.util.ArrayList;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.text.DecimalFormat;

public class BankApp {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		ArrayList<Account> accounts = new ArrayList<Account>();
		Account accountNew = new Account();
		String accountNo = "", accountName = "";
		double accountBal = 0.0;
		
		TransactionList transactionList = new TransactionList();
		String transactionType = "";
		double transactionAmount = 0.0;
		SimpleDateFormat formatter = new SimpleDateFormat("M/d/yyyy");
		
		System.out.println("Welcome to Evil Corp Savings and Loan");
		System.out.println("Please create the user account(s)");
		
		System.out.print("Enter an account # or -1 to stop entering accounts : ");
		
		try {
			accountNo = sc.nextLine();
			while (!accountNo.equals("-1")) {
				accountNew.setNo(accountNo);
				System.out.print("Enter the name for acct # " + accountNo + " : ");
				accountName = sc.nextLine();
				accountNew.setName(accountName);
				System.out.print("Enter the balance for acct # " + accountNo + " : ");
				accountBal = sc.nextDouble();
				accountNew.setBal(accountBal);
				accounts.add(accountNew);
				sc.nextLine();
				System.out.println("\n\n\n");
				System.out.print("Enter an account # or -1 to stop entering accounts : ");
				accountNo = sc.nextLine();
			}
			System.out.println();
			
			System.out.print("Enter a transaction type (Check, Debit card, Deposit or Withdrawal) or -1 to finish : ");
			transactionType = sc.nextLine();
			while (!validateType(transactionType)) {
				System.out.println("Invalid transaction type. Please try again.");
				transactionType = sc.nextLine();
			}
			while (!transactionType.equals("-1")) {
				Transaction transaction = new Transaction();
				transaction.setType(transactionType);
				System.out.print("Enter the account # : ");
				accountNo = sc.nextLine();
				transaction.setNo(accountNo);
				
				System.out.print("Enter the amount of the " + transaction.getType() + ": ");
				
				transactionAmount = sc.nextDouble();
				transaction.setAmount(transactionAmount);
				sc.nextLine();
				
				System.out.print("Enter the date of the " + transaction.getType() + ": ");
				
				Date transactionDate = formatter.parse(sc.nextLine());
				transaction.setDate(transactionDate);
				transactionList.addTransaction(transaction);
				
				System.out.println();
				System.out.print("Enter a transaction type (Check, Debit card, Deposit or Withdrawal) or -1 to finish : ");
				transactionType = sc.nextLine();
				while (!validateType(transactionType)) {
					System.out.println("Invalid transaction type. Please try again.");
					transactionType = sc.nextLine();
				}
			}
			
			transactionList.sort();
			for (int i = 0; i < transactionList.size(); i++) {
				for (int j = 0; j < accounts.size(); j++) {
					if (transactionList.getTransaction(i).getNo().equals(accounts.get(j).getNo())) {
						double balance = accounts.get(j).getBal();
						
						if (transactionList.getTransaction(i).getType().equals("check") || transactionList.getTransaction(i).getType().equals("debit card") || transactionList.getTransaction(i).getType().equals("withdrawal")) {
							balance -= transactionList.getTransaction(i).getAmount();
							if (balance < 0) {
								balance -= 35;
							}
						} else {
							balance += transactionList.getTransaction(i).getAmount();
						}
						accounts.get(j).setBal(balance);
					}
				}
			}
			System.out.println();
			
			HashMap<String, Double> accountSum = new HashMap<String, Double>();
			for (int i = 0; i < accounts.size(); i++) {
				accountSum.put(accounts.get(i).getNo(), accounts.get(i).getBal());
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
			System.out.println("==================================================");
			System.out.println("Transaction summary: ");
			System.out.println();
			SimpleDateFormat date = new SimpleDateFormat("M/d/yyyy");
			System.out.printf("%-14s%-11s%-14s%s%n", "Date", "Account #", "Type", "Amount");
			System.out.println("--------------------------------------------------");
			for (int i = 0; i < transactionList.size(); i++) {
				System.out.printf("%-14s%-11s%-14s%s%n", date.format(transactionList.getTransaction(i).getDate()), transactionList.getTransaction(i).getNo(), transactionList.getTransaction(i).getType(), getFormattedCurrency(transactionList.getTransaction(i).getAmount()));
			}
			System.out.println("==================================================");
		} catch (ParseException e) {
			System.err.println(e.getMessage());
		} catch (NullPointerException e) {
			System.err.println(e.getMessage());
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	public static boolean validateType(String transactionType) {
		if (transactionType.length() < 3) {
			if (transactionType.toUpperCase().charAt(0) == 'C' || transactionType.toUpperCase().charAt(0) == 'W' || transactionType.equals("-1")) {
				return true;
			} else {
				return false;
			}
		} else {
			if (transactionType.substring(0, 3).equalsIgnoreCase("DEB") || transactionType.substring(0, 3).equalsIgnoreCase("DEP")) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public static String getFormattedCurrency(double amount) {
		DecimalFormat format = new DecimalFormat("$#,##0.00;-$#,##0.00");
		String formatted = format.format(amount);
		return formatted;
	}

}
