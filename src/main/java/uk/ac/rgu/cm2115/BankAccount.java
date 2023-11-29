package uk.ac.rgu.cm2115;

public abstract class BankAccount {
    protected int accountNumber;
    protected int sortCode;
    protected int balance;
    protected final String accountName;

    public BankAccount(String accountName) {
        this.accountName = accountName;
    }

    public int getAccountNumber() {
		return accountNumber;
	}

	public int getSortCode() {
		return sortCode;
	}

	public int getBalance() {
		return balance;
	}

	public String getAccountName() {
		return accountName;
	}

	public abstract boolean deposit(int amount);

    public abstract boolean withdraw(int amount);

	@Override
	public String toString() {
		return accountName;
	}
    
}
