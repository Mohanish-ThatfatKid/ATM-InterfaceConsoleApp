package in.proj.main;

import in.proj.model.Account;
import in.proj.model.AccountHolder;
import in.proj.model.Bank;
import in.proj.utils.IdGenerator;

public class UserRegister {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		Account acc= new Account();
		acc.setAccType("Saving");
		acc.setAccNumber("98521476285");
		acc.setAccBalance(30000.00);
		
		AccountHolder accholder = new AccountHolder();
		String userId =  IdGenerator.genrateUserId();
		accholder.setAccholId(userId);
		accholder.setAccholName("mohanish");
		accholder.setAccholContact(9821729465l);
		accholder.setAccholPassword(8633);
		accholder.setAccount(acc);
		
		Bank bank = new Bank();
		bank.setAccountHolder(accholder);
		bank.setBankName("sbi");

	}

}
