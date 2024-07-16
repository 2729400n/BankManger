package ui.bank;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AccountManager {
	private List<Customer> customers = new ArrayList<>();
	private static final AccountManager INSTANCE = new AccountManager();

	public AccountManager() {
		readCustomerDetails();
	}

	private void readCustomerDetails() {
		Map<String, Customer> customerMap = new HashMap<>();
		try (Scanner sc = new Scanner(new FileReader("Customers.csv"))) {
			sc.nextLine();
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] parts = line.split(",");
				Customer customer = new Customer(parts[0], parts[1], CustomerType.valueOf(parts[2]));
				customerMap.put(parts[0], customer);
			}

		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		}
		customers.addAll(customerMap.values());

		try (Scanner sc = new Scanner(new FileReader("CustomerAccounts.csv"))) {
			sc.nextLine();
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] parts = line.split(",");
				int accountNumber = Integer.valueOf(parts[0]);
				String accountName = parts[1];
				int sortCode = Integer.valueOf(parts[2]);
				int balance = Integer.valueOf(parts[3]);
				String accountType = parts[4];
				String customerName = parts[5];

				if ("BasicAccount".equals(accountType)) {
					BasicAccount account = new BasicAccount(accountName,sortCode,balance);
					if (customerMap.containsKey(customerName)) {
						customerMap.get(customerName).getAccounts().add(account);
					}
				} else if ("RewardAccount".equals(accountType)) {
					RewardAccount account = new RewardAccount(accountName,sortCode,balance);
					account.accountNumber = accountNumber;
					account.sortCode = sortCode;
					if (customerMap.containsKey(customerName)) {
						customerMap.get(customerName).getAccounts().add(account);
					}
				} else if ("ISA".equals(accountType)) {
					ISA account = new ISA(accountName);
					account.accountNumber = accountNumber;
					account.sortCode = sortCode;
					if (customerMap.containsKey(customerName)) {
						customerMap.get(customerName).getAccounts().add(account);
					}
				}

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public boolean addCustomers(Customer customer) {
		if (!customers.contains(customer)) {
			customers.add(customer);
			return true;
		}
		return false;
	}

	public boolean removeCustomers(Customer customer) {
		if (!customers.contains(customer)) {
			customers.remove(customer);
			return true;
		}
		return false;
	}

	public void applyCustomerSave() {
		try{
			File f = new File("CustomerAccounts.csv");
			FileWriter fw = new FileWriter(f);
			for (Customer customer : customers) {
				
			}

		}catch(IOException ex){

		}catch(IOError ierr){

		}
	}

	public List<Customer> getCustomers() {
		return customers;
	}

	public static AccountManager getInstance() {
		return INSTANCE;
	}

}
