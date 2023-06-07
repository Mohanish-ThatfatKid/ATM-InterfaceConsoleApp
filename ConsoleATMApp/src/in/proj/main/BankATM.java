package in.proj.main;

import java.util.List;
import java.util.Scanner;

import in.proj.model.AccountHolder;
import in.proj.model.Transaction;
import in.proj.service.IuserService;
import in.proj.service.UserServiceImpl;

public class BankATM {

	
	public static void main(String[] args) {
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		String userId = "";
		Integer pin = null; 
		Double balance = null;
		
		AccountHolder acc = null;
		IuserService usi = new UserServiceImpl();
		System.out.println("Welcome to ATM!!!");
		
		for (int i = 1; i < 4; i++) {
			System.out.println("Enter your UserId: ");
			userId = sc.next();
			System.out.println("Enter your Pin: ");
			pin = sc.nextInt();
			acc = usi.login(userId, pin);
			if (acc!= null) {
				break;
			}else {
				if (i==3) {
					System.err.println("You have Entered Wrong data 3 times!!!");
					System.exit(0);
				}
				System.out.println("Wrong Credentials Entered Please try again You have total 3 chances . Chance "+i );
				
			}	
		}
		String accNumber = acc.getAccount().getAccNumber();
		
		System.out.println("Welcome "+ acc.getAccholName());
		while(true) {
			System.out.println("1. Check Balance");
			System.out.println("2. Deposite");
			System.out.println("3. Withdraw");
			System.out.println("4. Show Transactions");
			System.out.println("5. Transfer Money");
			System.out.println("6. Chnage PIN");
			System.out.println("7. Logout");
			System.out.println("Enter Your Choice [1,2,3,4,5,6,7]: ");
			int option = sc.nextInt();
			
			switch (option) {
			case 1:{
			String accnumber = acc.getAccount().getAccNumber();
			balance = usi.checkBalance(accnumber);
			System.out.println("Available Balance:: "+balance);
			System.out.println();
			}
				break;
			case 2:
			{
				System.out.println("Enter the Amount to for Deposite: ");
				Double money = sc.nextDouble();
			balance = usi.deposite(acc, money);
				System.out.println("Available Balance:: "+balance);
			}
				
				break;
			case 3:
				System.out.println("Enter Amount to Withdraw: ");
				Double money = sc.nextDouble();
				
				String result =  usi.withdraw(acc, money);
				balance = usi.checkBalance(accNumber);
				if (result.equalsIgnoreCase("success")) {
					System.out.println("Withdraw Success! Available Balance:: "+balance);
					System.out.println();
				}
				else if (result.equalsIgnoreCase("not Enough")) {
					System.out.println("not Enough Money in Account!!! Available Balance :: "+balance);
				}
				else if (result.equalsIgnoreCase("Fail")) {
					System.out.println("Transaction Failed.");
					
				}else {
					System.out.println("Something Went Wrong !!! Try again ");
				}
	
				break;
			case 4:
				List<Transaction> transactions = usi.getTransactions(acc);
				if (transactions!=null) {
					for (Transaction tran : transactions) {
						System.out.println(tran);
					}
				}
				else {
					System.out.println("No Transaction to SHOW !!!");
				}
	
				break;
			case 5:
				System.out.println("Enter Amount to transfer :");
				Double amount =sc.nextDouble();
				System.out.println("Enter Account Number to Transfer the Money: ");
				String transferAccNumber = sc.next();
				balance=  usi.transferMoney(acc, transferAccNumber, amount);
				System.out.println("Transfer Successfull. Available Balance: "+ balance);
				
	
				break;
			case 6:
				System.out.println("Enter new 4 digit pin: ");
				int newPin = sc.nextInt();
				Boolean flag =  usi.changePin(userId, newPin);
				if (flag) {
					System.out.println("Pin Changed Successfully Please Not Down your Pin: "+newPin);
				}
				else {
					System.err.println("Updatation Failed...");
				}
				
				break;
			case 7:
			{
				System.out.println("Thank you for using this Application...");
				System.exit(0);
			}
	
				break;

			default:
				break;
			}
			
			
		}
	}

}
