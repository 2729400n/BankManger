package ui.bank;

public class BasicAccount extends BankAccount implements CurrentAccount {

	public BasicAccount(String accountName) {
		super(accountName);
	}
	public BasicAccount(String accountName,int sortCcode) {
		this(accountName);
		this.sortCode=sortCcode;
		
	}
	public BasicAccount(String accountName,int sortCode,int balance) {
		this(accountName,sortCode);
		this.balance = balance;
		
	}
	

	@Override
	public void processCardTransactions(int amount) {
		withdraw(amount);
	}

	@Override
	public boolean deposit(int amount) {
		if (amount > 0) {
			this.balance += amount;
			return true;
		}
		return false;
	}

	@Override
	public boolean withdraw(int amount) {
		if (this.balance > 0 && amount > 0 && this.balance >= amount) {
			this.balance -= amount;
			return true;
		}
		return false;
	}

}
