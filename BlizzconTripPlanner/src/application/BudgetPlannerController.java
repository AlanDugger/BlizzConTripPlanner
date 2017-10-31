package application;

import java.math.BigDecimal;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class BudgetPlannerController 
{
	@FXML
	private GridPane budgetPane;
	@FXML
	private Button btnExitBudget;
	@FXML
	private TableView<BudgetItem> budgetTableView;
	@FXML
	private TableColumn<BudgetItem, String> columnEntry;
	@FXML
	private TableColumn<BudgetItem, BigDecimal> columnAmount;
	@FXML
	private TableColumn<BudgetItem, Boolean> columnPaid;
	@FXML
	private TableColumn<BudgetItem, Boolean> columnNotes;
	@FXML
	private Button addEntryButton;
	@FXML
	private TextField textfieldEntryName;
	@FXML
	private TextField textfieldAmount;
	@FXML
	private CheckBox checkboxPaid;
	@FXML
	private CheckBox checkboxNotes;
	@FXML
	private TextField textfieldRemaining;
	@FXML
	private TextField textfieldPaid;	
	
	public void initialize()
	{
		try
		{
			BudgetData.getInstance().loadBudgetItems();			
			budgetTableView.setItems(BudgetData.getInstance().getBudgetItems());
			columnEntry.setCellValueFactory(new PropertyValueFactory<BudgetItem, String>("entryName"));
			columnAmount.setCellValueFactory(new PropertyValueFactory<BudgetItem, BigDecimal>("budgetAmount"));
			columnPaid.setCellValueFactory(new PropertyValueFactory<BudgetItem, Boolean>("alreadyPaid"));
			columnNotes.setCellValueFactory(new PropertyValueFactory<BudgetItem, Boolean>("seeNotes"));	
			textfieldPaid.textProperty().bind(BudgetData.getInstance().getTotalPaid());	
			textfieldRemaining.textProperty().bind(BudgetData.getInstance().getTotalRemaining());
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}						
	}
	
	@FXML
	public void handleAddEntry()
	{
		if (textfieldEntryName.getText().isEmpty() || textfieldAmount.getText().isEmpty())
		{
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Invalid Entry");
			alert.setHeaderText(null);
			alert.setContentText("An entry name and amount must be included.");
					
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);
			stage.toFront();
			alert.showAndWait();
		}
		else
		{
			try
			{
				
				double amount = Double.parseDouble(textfieldAmount.getText());
				boolean alreadyPaid = checkboxPaid.isSelected();
				boolean seeNotes = checkboxNotes.isSelected();
				BudgetItem tmp = new BudgetItem(textfieldEntryName.getText(), amount, alreadyPaid, seeNotes);
				BudgetData.getInstance().addBudgetItem(tmp);
				
				textfieldEntryName.setText("");
				textfieldAmount.setText("");
				checkboxPaid.setSelected(false);
				checkboxNotes.setSelected(false);
				textfieldPaid.textProperty().bind(BudgetData.getInstance().getTotalPaid());
				textfieldRemaining.textProperty().bind(BudgetData.getInstance().getTotalRemaining());
			}
			catch(Exception e)
			{
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Invalid Entry");
				alert.setHeaderText(null);
				alert.setContentText("Please only enter numbers in the Amount field.");
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.setAlwaysOnTop(true);
				stage.toFront();
				alert.showAndWait();				
			}
		}
	}
	
	@FXML
	public void handleDeleteEntry() {
		BudgetItem selectedItem = budgetTableView.getSelectionModel().getSelectedItem();
		
		if (selectedItem != null) 
			deleteItem(selectedItem);
		else
		{
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Invalid Selection");
			alert.setHeaderText(null);
			alert.setContentText("Please select an item from the table to delete.");
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);
			stage.toFront();
			alert.showAndWait();	
		}
	}
	
	private void deleteItem(BudgetItem item) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setHeaderText("Delete item: " + item.getEntryName());
		alert.setTitle("Delete Budget Item");
		alert.setContentText("Are you sure?  Press OK to confirm.");
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.setAlwaysOnTop(true);
		stage.toFront();
		Optional<ButtonType> result = alert.showAndWait();
		
		if (result.isPresent() && result.get() == ButtonType.OK)
		{
			BudgetData.getInstance().deleteBudgetItem(item);
		}
		textfieldPaid.textProperty().bind(BudgetData.getInstance().getTotalPaid());
		textfieldRemaining.textProperty().bind(BudgetData.getInstance().getTotalRemaining());
	}
	
	@FXML
	public void handleKeyPressed(KeyEvent keyEvent) {
		BudgetItem selectedItem = budgetTableView.getSelectionModel().getSelectedItem();
		
		if (selectedItem != null) 
		{
			if (keyEvent.getCode().equals(KeyCode.DELETE)) {
				deleteItem(selectedItem);
			}
		}
	}
	
	@FXML
	public void exitBudgetMenu()
	{		
		BudgetData.getInstance().saveBudgetItems();		
		Stage stage = (Stage) btnExitBudget.getScene().getWindow();				
		stage.close();
	}
		
}
