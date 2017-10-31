package application;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class CostController 
{

	@FXML
	private Button btnExitCost;
	@FXML
	private TableView<CostItem> costTableView;	
	@FXML
	private DatePicker datePicker;
	@FXML
	private TableColumn<CostItem, String> columnDate;
	@FXML
	private TableColumn<CostItem, String> columnEntry;
	@FXML
	private TableColumn<CostItem, BigDecimal> columnCost;
	@FXML
	private TextField textfieldPaid;
	@FXML
	private TextField textfieldAmount;
	@FXML
	private TextField textfieldEntryName;
	@FXML
	private ChoiceBox<String> entryBox;
	@FXML
	private TextField textfieldGas;
	@FXML
	private TextField textfieldFood;
	@FXML
	private TextField textfieldLodging;
	@FXML
	private TextField textfieldMerchandise;
	@FXML
	private TextField textfieldOther;
	
	public void initialize()
	{
		datePicker.setValue(LocalDate.now());
		
		try
		{
			CostData.getInstance().loadCostItems();			
			costTableView.setItems(CostData.getInstance().getCostItems());
			columnDate.setCellValueFactory(new PropertyValueFactory<CostItem, String>("entryDate"));
			columnEntry.setCellValueFactory(new PropertyValueFactory<CostItem, String>("entryName"));
			columnCost.setCellValueFactory(new PropertyValueFactory<CostItem, BigDecimal>("costAmount"));				
			updateTextFields();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void updateTextFields()
	{
		textfieldPaid.textProperty().bind(CostData.getInstance().getTotalCost());
		textfieldGas.textProperty().bind(CostData.getInstance().getSubtotal("Gas"));
		textfieldFood.textProperty().bind(CostData.getInstance().getSubtotal("Food"));
		textfieldLodging.textProperty().bind(CostData.getInstance().getSubtotal("Lodging"));
		textfieldMerchandise.textProperty().bind(CostData.getInstance().getSubtotal("Merchandise"));
		textfieldOther.textProperty().bind(CostData.getInstance().getSubtotal("Other"));
	}
	
	@FXML
	public void exitCostMenu()
	{
		CostData.getInstance().saveCostItems();		
		Stage stage = (Stage) btnExitCost.getScene().getWindow();				
		stage.close();
	}
	
	@FXML
	public void handleKeyPressed(KeyEvent keyEvent)
	{
		CostItem selectedItem = costTableView.getSelectionModel().getSelectedItem();
		
		if (selectedItem != null) 
		{
			if (keyEvent.getCode().equals(KeyCode.DELETE)) {
				deleteItem(selectedItem);
			}
		}
	}
	
	@FXML
	public void handleAddEntry()
	{
		LocalDate date = datePicker.getValue();
		boolean validSelection = false;
		
		if (entryBox.getValue() == null || date == null)
			validSelection = false;
		else if (entryBox.getValue().toString().equals("Other") && textfieldEntryName.getText().isEmpty())
			validSelection = false;
		else if (textfieldAmount.getText().isEmpty() || entryBox.getValue().toString().equals(""))
			validSelection = false;
		else
			validSelection = true;
		
		if (!validSelection)
		{
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Invalid Entry");
			alert.setHeaderText(null);
			alert.setContentText("Please select an entry amount (specify name if Other) and include an amount paid.");
					
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);
			stage.toFront();
			alert.showAndWait();
		}
		else
		{
			
			try
			{
				
				String entryName;
				if (entryBox.getValue().toString().equals("Other"))
					entryName = textfieldEntryName.getText();
				else
					entryName = entryBox.getValue().toString();
				double amount = Double.parseDouble(textfieldAmount.getText());				
				CostItem tmp = new CostItem(date, entryName, amount);
				CostData.getInstance().addCostItem(tmp);
				
				textfieldEntryName.setText("");
				textfieldAmount.setText("");								
				updateTextFields();	
				
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
				e.printStackTrace();
			}						
		}
	}
	
	@FXML
	public void handleDeleteEntry()
	{
		CostItem selectedItem = costTableView.getSelectionModel().getSelectedItem();
		
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
	
	private void deleteItem(CostItem item) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setHeaderText("Delete item: " + item.getEntryName());
		alert.setTitle("Delete Cost Item");
		alert.setContentText("Are you sure?  Press OK to confirm.");
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.setAlwaysOnTop(true);
		stage.toFront();
		Optional<ButtonType> result = alert.showAndWait();
		
		if (result.isPresent() && result.get() == ButtonType.OK)
		{
			CostData.getInstance().deleteCostItem(item);
		}
		updateTextFields();		
	}
}
