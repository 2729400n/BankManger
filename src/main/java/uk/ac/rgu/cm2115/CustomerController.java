package uk.ac.rgu.cm2115;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class CustomerController extends Controller<Customer> {
	@FXML
	private TextField custName;
	@FXML
	private TextArea custAddress;
	@FXML
	private ComboBox<String> categories;
	@FXML
	private ComboBox<String> types;
	@FXML
	private TextField name;
	@FXML
	private TextField sordCode;
	@FXML
	private ComboBox<BankAccount> accounts;
	private AccountManager accountManager = AccountManager.getInstance();

	@FXML
	private ComboBox<CustomerType> custTypes;

	@FXML
	private ComboBox<String> categories1;
	@FXML
	private ComboBox<String> types1;
	@FXML
	private ComboBox<BankAccount> accounts1;
	@FXML
	private ComboBox<String> operations;
	@FXML
	private TextField amount;

	@Override
	public void setModel(Customer model) {
		this.model = model;
		custName.setText(model.getName());
		custAddress.setText(model.getAddress());
		custTypes.getItems().addAll(CustomerType.values());
		custTypes.getSelectionModel().select(model.getCustomerType());
		categories.getItems().add("Current Account");
		categories.getItems().add("Savings Account");
		types.getItems().add("Basic Account");
		types.getItems().add("Reward Account");
		types.getItems().add("ISA");
		accounts.getItems().addAll(model.getAccounts());
		accounts1.getItems().addAll(accounts.getItems());
		categories1.getItems().addAll(categories.getItems());
		types1.getItems().addAll(types.getItems());
		operations.getItems().add("Deposit");
		operations.getItems().add("Withdraw");
		operations.getItems().add("Apply Interest");
		operations.getItems().add("Process Card");
	}

	@FXML
	public void updateCustomer(Event Event) {
		String name = custName.getText();
		String address = custAddress.getText();
		CustomerType customerType = custTypes.getSelectionModel().getSelectedItem();
		Customer customer = new Customer(name, address, customerType);
		int index = 0;
		for (Customer customer2 : accountManager.getCustomers()) {
			if (customer2.getName().equals(name)) {
				break;
			}
			index++;
		}
		accountManager.getCustomers().set(index, customer);
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Success");
		alert.setContentText("Customer is updated successfully!");
		alert.show();
		persistCustomer();
		custName.setText("");
		custAddress.setText("");
		custTypes.getSelectionModel().select(0);
	}

	/**
	 * Persist Customer details
	 */
	private void persistCustomer() {
		String customersCSVHeaders = "Customer Name,Customer Address,Customer Type";
		String customersAccountCSVHeaders = "Account Number,Account Name,Sort Code, Balance,Account Type,Customer Name";
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		accountManager.getCustomers().stream().forEach(customer -> {
			sb1.append(customer.getName() + "," + customer.getAddress() + "," + customer.getCustomerType() + "\n");
			if (null != customer.getAccounts() && !customer.getAccounts().isEmpty()) {
				customer.getAccounts().stream().forEach(account -> {
					sb2.append(account.getAccountNumber() + "," + account.getAccountName() + "," + account.getSortCode()
							+ "," + account.getBalance() + "," + account.getClass().getSimpleName() + ","
							+ customer.getName() + "\n");
				});
			}
		});

		try (BufferedWriter bw = new BufferedWriter(new FileWriter("Customers.csv"))) {
			bw.write(customersCSVHeaders);
			bw.newLine();
			bw.write(sb1.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		try (BufferedWriter bw = new BufferedWriter(new FileWriter("CustomerAccounts.csv"))) {
			bw.write(customersAccountCSVHeaders);
			bw.newLine();
			bw.write(sb2.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void backtoAccountManager(Event event) {
		try {
			MainApp.setScene("AccountManager", accountManager);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void addAccount(Event event) {
		String category = categories.getSelectionModel().getSelectedItem();
		String type = types.getSelectionModel().getSelectedItem();
		String n = name.getText();
		String scode = sordCode.getText();
		if ("Current Account".equals(category)) {
			if ("Basic Account".equals(type)) {
				BasicAccount account = new BasicAccount(n);
				account.accountNumber = new Random(System.currentTimeMillis()).nextInt(Integer.MAX_VALUE);
				account.sortCode = Integer.valueOf(scode);
				model.getAccounts().add(account);
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Success");
				alert.setContentText("Account is added successfully!");
				alert.show();
				name.setText("");
				sordCode.setText("");
			} else if ("Reward Account".equals(type)) {
				RewardAccount account = new RewardAccount(n);
				account.accountNumber = new Random(System.currentTimeMillis()).nextInt(Integer.MAX_VALUE);
				account.sortCode = Integer.valueOf(scode);
				model.getAccounts().add(account);
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Success");
				alert.setContentText("Account is added successfully!");
				alert.show();
				name.setText("");
				sordCode.setText("");
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setContentText("Invalid Choice");
				alert.show();
			}
		} else if ("Savings Account".equals(category)) {
			if ("Reward Account".equals(type)) {
				RewardAccount account = new RewardAccount(n);
				account.accountNumber = new Random(System.currentTimeMillis()).nextInt(Integer.MAX_VALUE);
				account.sortCode = Integer.valueOf(scode);
				model.getAccounts().add(account);
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Success");
				alert.setContentText("Account is added successfully!");
				alert.show();

				name.setText("");
				sordCode.setText("");
			} else if ("ISA".equals(type)) {
				ISA account = new ISA(n);
				account.accountNumber = new Random(System.currentTimeMillis()).nextInt(Integer.MAX_VALUE);
				account.sortCode = Integer.valueOf(scode);
				model.getAccounts().add(account);
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Success");
				alert.setContentText("Account is added successfully!");
				alert.show();
				name.setText("");
				sordCode.setText("");
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setContentText("Invalid Choice");
				alert.show();
			}
		}
		persistCustomer();
		accounts.getItems().clear();
		accounts.getItems().addAll(model.getAccounts());
	}

	@FXML
	public void getAccountDetails(Event event) {
		BankAccount account = accounts.getSelectionModel().getSelectedItem();
		if (null != account) {
			String accountDetails = "Account Name:" + account.getAccountName() + "\nAccount Number:"
					+ account.getAccountNumber() + "\n Sort Code:" + account.getSortCode() + "\n Balance:"
					+ account.getBalance() + "\nAccount type:" + account.getClass().getSuperclass().getSimpleName()
					+ "\nAccount sub type:" + account.getClass().getSimpleName();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Success");
			alert.setContentText(accountDetails);
			alert.show();

		}
	}

	@FXML
	public void perform(Event event) {
		BankAccount account = accounts1.getSelectionModel().getSelectedItem();
		if (null != account) {
			String operation = operations.getSelectionModel().getSelectedItem();
			if ("Deposit".equals(operation)) {
				if (account.deposit(Integer.valueOf(amount.getText()))) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Success");
					alert.setContentText("Amount is deposited successfully");
					alert.show();
				}else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setContentText("Failed to deposit");
					alert.show();
				}
			} else if ("Withdraw".equals(operation)) {
				if (account.withdraw(Integer.valueOf(amount.getText()))) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Success");
					alert.setContentText("Amount is withdrawn successfully");
					alert.show();
				}else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setContentText("Failed to withdraw");
					alert.show();
				}
			} else if ("Apply Interest".equals(operation) && account instanceof SavingsAccount) {
				((SavingsAccount) account).applyInterest(Integer.valueOf(amount.getText()));
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Success");
				alert.setContentText("Interest is applied successfully");
				alert.show();

			} else if ("Process Card".equals(operation)) {
				((CurrentAccount) account).processCardTransactions(Integer.valueOf(amount.getText()));
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Success");
				alert.setContentText("Card is processed successfully");
				alert.show();
			}
		}
		persistCustomer();
	}
}
