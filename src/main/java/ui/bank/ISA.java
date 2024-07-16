package ui.bank;

public class ISA extends BankAccount implements SavingsAccount {

	public ISA(String accountName) {
		super(accountName);
	}

	@Override
	public void applyInterest(int amount) {
		deposit(amount);
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
