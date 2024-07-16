package ui.bank;

import java.lang.reflect.Field;

public abstract class BankAccount {
    protected int accountNumber;
    protected int sortCode;
    protected int balance;
    protected final String accountName;

    public BankAccount(String accountName) {
		this.accountName=accountName;
	}
	public BankAccount(String accountName,int sortCcode) {
		this(accountName);
		this.sortCode=sortCcode;
		
	}
	public BankAccount(String accountName,int sortCode,int balance) {
		this(accountName,sortCode);
		this.balance = balance;
		
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

	public String getInfoString(String... infoToget) {
		StringBuilder infoBuilder  = new StringBuilder();
		Class<? extends BankAccount> myClass = this.getClass();
		for (String request : infoToget) {
			for (Field info: myClass.getFields()) {
				if(info.getName().toUpperCase().contains(request.toUpperCase())){
					try{
					infoBuilder.append(String.format("%s:%s;",request,info.get(this).toString()));
					}catch(IllegalAccessException ex){
						ex.printStackTrace();
					}
				}

			}
		}
		return infoBuilder.toString();
	}
	public String[] getInfo(String... infoToget) {
		return getInfoString(infoToget).split(";");
	}
	public String[] getInfo() {
		Class<? extends BankAccount> bankAccountClass = this.getClass();
		Field[] infos = bankAccountClass.getFields();
		String[] infoStrings = new String[infos.length];
		for (int i=0;i<infos.length ;i++) {
			infoStrings[i]=infos[i].getName();
			
		}
		return getInfo((String[])infoStrings);
	}
    
}
