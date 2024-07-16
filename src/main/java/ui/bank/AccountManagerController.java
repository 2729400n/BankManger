package ui.bank;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AccountManagerController extends Controller<AccountManager> {
	private AccountManager model;
	@FXML
	private ComboBox<Customer> customersCombox;
	@FXML
	private Button viewCustomerBtn;
	@FXML
	private Button addCustomerBtn;
	@FXML
	private TextField custName;
	@FXML
	private TextArea custAddress;

	@FXML
	private ComboBox<CustomerType> custTypes;
	public AccountManager createModel(){
		return new AccountManager();
	}


	public AccountManagerController(){
		
	}

	@Override
	public void setRootView(Parent rootView) {
		super.setRootView(rootView);
	}

	@Override
	public void setModel(AccountManager model) {
		this.model = model;
		customersCombox.getItems().addAll(model.getCustomers());
		custTypes.getItems().addAll(CustomerType.values());
	}

	@FXML
	public void viewCustomerDetails(Event event) {
		Customer selectedCustomer = customersCombox.getSelectionModel().getSelectedItem();
		if (null != selectedCustomer) {
			try {
				MainApp.setScene("Customer", selectedCustomer);
			} catch (IOException e) {
				e.printStackTrace();
			}catch(URISyntaxException ue){

			}
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setContentText("No Customer Selected");
			alert.show();
		}
	}

	@FXML
	public void addNewCustomer(Event event) {

		String name = custName.getText();
		String address = custAddress.getText();
		CustomerType customerType = custTypes.getSelectionModel().getSelectedItem();
		Customer customer = new Customer(name, address, customerType);
		if (model.addCustomers(customer)) {
			customersCombox.getItems().clear();
			customersCombox.getItems().addAll(model.getCustomers());
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Success");
			alert.setContentText("Customer is added successfully!");
			alert.show();
			persistCustomer();
			custName.setText("");
			custAddress.setText("");
			custTypes.getSelectionModel().select(0);
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setContentText("Failed to add customer is added successfully!");
			alert.show();
		}
	}

	@FXML
	public void deleteCustomer(Event event) {

		String name = custName.getText();
		String address = custAddress.getText();
		CustomerType customerType = custTypes.getSelectionModel().getSelectedItem();
		Customer customer = new Customer(name, address, customerType);
		if (model.removeCustomers(customer)) {
			customersCombox.getItems().clear();
			customersCombox.getItems().addAll(model.getCustomers());
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Success");
			alert.setContentText("Customer has been removed successfully!");
			alert.show();
			persistCustomer();
			custName.setText("");
			custAddress.setText("");
			custTypes.getSelectionModel().select(0);
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setContentText("Failed to remove customer!");
			alert.show();
		}
		return;
	}

	

	/**
	 * Persist Customer details
	 */
	private void persistCustomer() {
		String customersCSVHeaders = "Customer Name,Customer Address,Customer Type";
		String customersAccountCSVHeaders = "Account Number,Account Name,Sort Code, Balance,Account Type,Customer Name";
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		model.getCustomers().stream().forEach(customer -> {
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
}

