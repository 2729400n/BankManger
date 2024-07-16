package ui.bank;

public class RewardAccount extends BankAccount implements CurrentAccount, SavingsAccount {

	public RewardAccount(String accountName) {
		super(accountName);
	}
	RewardAccount(String accountName,int sortCode,int balance){
		super(accountName,sortCode,balance);
	}

	@Override
	public void applyInterest(int amount) {
		deposit(amount);
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
