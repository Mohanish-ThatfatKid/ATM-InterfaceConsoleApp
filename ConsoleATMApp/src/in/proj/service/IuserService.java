package in.proj.service;

import java.util.List;

import in.proj.model.AccountHolder;
import in.proj.model.Transaction;

public interface IuserService {

	AccountHolder login(String userId, int pin);

	Double checkBalance(String accnumber);

	Double deposite(AccountHolder accholder, Double money);

	boolean changePin(String userId, int newPin);

	boolean saveTransaction(String action, String accnumber, Double money);

	String withdraw(AccountHolder accHolder, Double money);

	List<Transaction> getTransactions(AccountHolder accountHolder);
	
	Double transferMoney(AccountHolder accHolder, String recieverAccNumber , Double Money);

}
