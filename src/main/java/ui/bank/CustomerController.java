package ui.bank;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class CustomerController extends Controller<Customer> implements EventHandler<ActionEvent> {
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

	@FXML
	private Button viewDetails;

	@FXML
	VBox viewDetailsHolder;

	List<HBox> accBtns = new ArrayList<HBox>(); 

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
		
		this.addNewAccountButtons();
	}
	
	
	public  void setRootView(Parent rootView){
        super.setRootView(rootView);
		AnchorPane root= (AnchorPane)rootView;
		BorderPane bane =  (BorderPane)root.getChildren().get(0);

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
		Button reference = viewDetails;
		this.addNewAccountButtons();
	}

	/**
	 * Persist Customer details
	 */
	private void persistCustomer() {
		String customersCSVHeaders = "Customer Name,Customer Address,Customer Type";
		String customersAccountCSVHeaders = "Account Number,Account Name,Sort Code, Balance,Account Type,Customer Name";
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		List<Customer> custs = accountManager.getCustomers();
		for(Customer customer : custs ) {
			sb1.append(customer.getName() + "," + customer.getAddress() + "," + customer.getCustomerType() + "\n");
			List<BankAccount> accs = customer.getAccounts();
			if (null != accs  ) {
				for (BankAccount account : accs) {
					sb2.append(String.format("%d, %s, %d, %d, %s, %s\r\n",account.getAccountNumber(), account.getAccountName(), account.getSortCode(),account.getBalance() ,account.getClass().getSimpleName() ,customer.getName()));
				}
			}
		};
		try (BufferedWriter bw = new BufferedWriter(new FileWriter("Customers.csv"))) {
			bw.write(customersCSVHeaders);
			bw.newLine();
			bw.write(sb1.toString());
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try (BufferedWriter bw = new BufferedWriter(new FileWriter("CustomerAccounts.csv"))) {
			bw.write(customersAccountCSVHeaders);
			bw.newLine();
			bw.write(sb2.toString());
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void backtoAccountManager(Event event) {
		try {
			MainApp.goBack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void addAccount(Event event) {
		String category = categories.getSelectionModel().getSelectedItem();
		String type = types.getSelectionModel().getSelectedItem();
		String n = name.getText();
		int scode;
		List<BankAccount> bankAccounts = model.getAccounts();
		Alert errAler = new Alert(AlertType.ERROR, "[Error]:", ButtonType.OK),
		warningAlert = new Alert(AlertType.WARNING, "[Warning]:", ButtonType.OK),
		infoAlert = new Alert(AlertType.INFORMATION, "[INFO]:", ButtonType.OK);
		try{
			category = categories.getSelectionModel().getSelectedItem();
			type = types.getSelectionModel().getSelectedItem();
			n = name.getText();
			scode = Integer.parseInt(sordCode.getText());
			bankAccounts = model.getAccounts();
		}catch(NullPointerException e){
			errAler.setContentText(String.format("[ERROR] %s","Must Fill out all Info!\r\n"));
			return;
		}catch(NumberFormatException e){
			errAler.setContentText(String.format("[ERROR] %s","Must Fill out all Info!\r\n"));
			return;
		}
		BankAccount account = null;
		try{
		synchronized(bankAccounts){
		if ("Current Account".equals(category)) {
			if ("Basic Account".equals(type)) {
				account = new BasicAccount(n);
				account.accountNumber = new Random(System.currentTimeMillis()).nextInt(Integer.MAX_VALUE);
				account.sortCode = Integer.valueOf(scode);
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Success");
				alert.setContentText("Account is added successfully!");
				alert.show();
				name.setText("");
				sordCode.setText("");
			} else if ("Reward Account".equals(type)) {
				account = new RewardAccount(n);
				account.accountNumber = new Random(System.currentTimeMillis()).nextInt(Integer.MAX_VALUE);
				account.sortCode = Integer.valueOf(scode);
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
				return;
			}
		} else if ("Savings Account".equals(category)) {
			if ("Reward Account".equals(type)) {
				account = new RewardAccount(n);
				account.accountNumber = new Random(System.currentTimeMillis()).nextInt(Integer.MAX_VALUE);
				account.sortCode = Integer.valueOf(scode);
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Success");
				alert.setContentText("Account is added successfully!");
				alert.show();

				name.setText("");
				sordCode.setText("");
			} else if ("ISA".equals(type)) {
				account = new ISA(n);
				account.accountNumber = new Random(System.currentTimeMillis()).nextInt(Integer.MAX_VALUE);
				account.sortCode = Integer.valueOf(scode);
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
				return;
			}
		}
		}
		bankAccounts.add(account);
		accounts.getItems().clear();
		accounts.getItems().addAll(bankAccounts);
		accounts1.getItems().clear();
		accounts1.getItems().addAll(bankAccounts);
		this.addNewAccountButtons();}
		catch(NullPointerException e){
			errAler.setContentText("Need to fill Info!");
			errAler.show();
		}
	}

	@FXML
	public void deleteAccount(BankAccount account) {
		List<BankAccount> bankAccounts = model.getAccounts();

		Alert errAlert = new Alert(AlertType.ERROR, "[Error]:", ButtonType.OK),
		warningAlert = new Alert(AlertType.WARNING, "[Warning]:", ButtonType.OK),
		infoAlert = new Alert(AlertType.INFORMATION, "[INFO]:", ButtonType.OK);
		
		infoAlert.setTitle("Info");
		errAlert.setTitle("Error");
		warningAlert.setTitle("Warning");
		name.setText("");
		sordCode.setText("");
		synchronized(bankAccounts){
				if(bankAccounts.remove(account)){
					infoAlert.setContentText(String.format("Account %s was removed successfully!",account.accountName));
				}else{
					errAlert.setContentText(String.format("Account %s was not ",account.accountName));
				}
		}
		this.persistCustomer();
		accounts.getItems().clear();
		accounts.getItems().addAll(bankAccounts);
		accounts1.getItems().clear();
		accounts1.getItems().addAll(bankAccounts);
		this.addNewAccountButtons();
	}

	public void addNewAccountButtons(){
		persistCustomer();
		Button reference = viewDetails;

		for (HBox btn : accBtns) {
			viewDetailsHolder.getChildren().remove(btn);
			
		}

		accBtns.clear();
		viewDetailsHolder.requestLayout();

		for(BankAccount acc : model.getAccounts()){
			
			Button oenButton = new Button(String.format("Open %s",acc.getAccountName())), delButton =new Button(String.format("Delete %s",acc.getAccountName()));
	
			double x =reference.getTranslateX();
			double y =reference.getTranslateY();

			List<Node> children= viewDetailsHolder.getChildren();

			HBox buttonHolder = new HBox(20.0);
			List<Node> btnChild = buttonHolder.getChildren();
			buttonHolder.setTranslateX(x);
			buttonHolder.setTranslateY(y+reference.getHeight());
			
			Alert alt = new Alert(AlertType.INFORMATION,"Hello",ButtonType.OK);
			alt.setContentText(String.format("BankAccount:%s\r\nAccount Number %d\r\nSortCode:%d\r\nBalance:%d\r\n",acc.getAccountName(),acc.getAccountNumber(),acc.getSortCode(),acc.getBalance()));
			
			oenButton.setOnAction(this);
			oenButton.setUserData((InvokableAction)()->{alt.show();});
			btnChild.add(oenButton);
			viewDetails.requestLayout();

			delButton.setOnAction(this);
			x =oenButton.getTranslateX()+oenButton.getWidth();
			Alert alt2 = new Alert(AlertType.INFORMATION,(String.format("Removed Account \r\nBankAccount:%s\r\nAccount Number %d\r\nSortCode:%d\r\nBalance:%d\r\n",acc.getAccountName(),acc.getAccountNumber(),acc.getSortCode(),acc.getBalance())),ButtonType.OK);
			delButton.setTranslateX(x);
			delButton.setUserData((InvokableAction)()->{alt2.show();this.deleteAccount(acc);});
			viewDetails.requestLayout();
			btnChild.add(delButton);
			children.add(buttonHolder);
			accBtns.add(buttonHolder);
			viewDetails.requestLayout();

		}
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

	@Override
	public void handle(ActionEvent event) {
		Object obj =event.getSource();
		if(obj instanceof Button){
			Button firer = (Button)obj;
			Object bundle =  firer.getUserData();
			if(bundle instanceof InvokableAction){
				InvokableAction stuffToDo = (InvokableAction)bundle;
				stuffToDo.apply();
				return;
			}
			return;
		}
		return;
	}


	@Override
	public Customer createModel() {
		return this.model = new Customer("", "null", CustomerType.BUSINESS);
	}


	public Customer createModel(String name,String address,CustomerType ct) {
		return this.model = new Customer("", "null", CustomerType.BUSINESS);

	}
}
