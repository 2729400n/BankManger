package ui.bank;

import java.util.ArrayList;
import java.util.List;

public class Customer {
	private String name;
	private String address;
	private List<BankAccount> accounts;
	private CustomerType customerType;

	public Customer(String name, String address, CustomerType customerType) {
		super();
		this.name = name;
		this.address = address;
		this.customerType = customerType;
		accounts=new ArrayList<>();
	}

	public boolean openAccount(BankAccount account) {
		if (!accounts.contains(account)) {

		}
		return false;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<BankAccount> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<BankAccount> accounts) {
		this.accounts = accounts;
	}

	public CustomerType getCustomerType() {
		return customerType;
	}

	public void setCustomerType(CustomerType customerType) {
		this.customerType = customerType;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}

}
