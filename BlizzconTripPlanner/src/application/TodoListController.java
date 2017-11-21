package application;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;

import datamodels.TodoData;
import datamodels.TodoItem;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

public class TodoListController 
{

	@FXML
	private Button btnExitTodo;
	@FXML
	private Button btnNewEntry;
	@FXML
	private ListView<TodoItem> todoListView;
	@FXML
	private TextArea textareaDetails;	
	@FXML
	private Label labelDeadline;
	@FXML	
	private ContextMenu listContextMenu;
	
	public void initialize()
	{
		//datePicker.setValue(LocalDate.now());
		try
		{
			listContextMenu = new ContextMenu();
			TodoData.getInstance().loadTodoItems();
		
			todoListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TodoItem>() 
			{
				@Override
				public void changed(ObservableValue<? extends TodoItem> observable, TodoItem oldValue, TodoItem newValue) 
				{
					if (newValue != null) 
					{
						TodoItem item = todoListView.getSelectionModel().getSelectedItem();
						textareaDetails.setText(item.getDetails());
						DateTimeFormatter df = DateTimeFormatter.ofPattern("MMMM d, yyyy");
						String deadlineText = "TARGET DATE: " + df.format(item.getDeadline()); 
						labelDeadline.setText(deadlineText);
					}
					else
					{
						textareaDetails.clear();
						labelDeadline.setText("");
					}
				}
			});
									
			SortedList<TodoItem> sortedList = new SortedList<TodoItem>(TodoData.getInstance().getTodoItems(), new Comparator<TodoItem>() {
			
				@Override
				public int compare (TodoItem o1, TodoItem o2) 
				{				
					return o1.getDeadline().compareTo(o2.getDeadline());
				}
			});
								
			todoListView.setItems(sortedList);
			todoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			todoListView.getSelectionModel().selectFirst();
		
			todoListView.setCellFactory(new Callback<ListView<TodoItem>, ListCell<TodoItem>>() 
			{
			
				@Override
				public ListCell<TodoItem> call(ListView<TodoItem> param) 
				{
					ListCell<TodoItem> cell = new ListCell<TodoItem>() 
					{
						@Override
						protected void updateItem(TodoItem item, boolean empty) 
						{
							super.updateItem(item, empty);
							if (empty) 
							{
								setText(null);
							}
							else 
							{
								setText(item.getShortDesc());
								if (item.getDeadline().isBefore(LocalDate.now().plusDays(1))) 
								{
									setTextFill(Color.RED);								
								
								} 
								else if (item.getDeadline().isBefore(LocalDate.now().plusDays(7))) 
								{
									setTextFill(Color.ORANGE);
								}							
							}
						}
					};
				
				cell.emptyProperty().addListener
				(
						(obs, wasEmpty, isNowEmpty) -> 
						{ 
							if (isNowEmpty) 
							{ 
								cell.setContextMenu(null); 
							}
							else 
							{ 
								cell.setContextMenu(listContextMenu); 
							}
						});
				
					return cell;
				}
			});
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@FXML
	public void exitTodoMenu()
	{
		TodoData.getInstance().saveTodoItems();		
		Stage stage = (Stage) btnExitTodo.getScene().getWindow();				
		stage.close();
	}
	
	@FXML
	public void handleKeyPressed(KeyEvent keyEvent)
	{
		TodoItem selectedItem = todoListView.getSelectionModel().getSelectedItem();
		
		if (selectedItem != null) 
		{
			if (keyEvent.getCode().equals(KeyCode.DELETE)) {
				deleteItem(selectedItem);
			}
		}
	}
		
	
	@FXML
	public void handleDeleteEntry()
	{
		TodoItem selectedItem = todoListView.getSelectionModel().getSelectedItem();
		
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
	
	private void deleteItem(TodoItem item) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setHeaderText("Delete entry: " + item.getShortDesc());
		alert.setTitle("Delete Cost Item");
		alert.setContentText("Are you sure?  Press OK to confirm.");
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.setAlwaysOnTop(true);
		stage.toFront();
		Optional<ButtonType> result = alert.showAndWait();
		
		if (result.isPresent() && result.get() == ButtonType.OK)
		{
			TodoData.getInstance().deleteTodoItem(item);
		}			
	}
	
	@FXML
	public void showNewItemDialog() {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.initOwner(btnNewEntry.getScene().getWindow());
		dialog.setTitle("Add New Todo Item");
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/todoItemDialog.fxml"));				
		
		try 
		{			
			dialog.getDialogPane().setContent(fxmlLoader.load());
		}
		catch (IOException e) 
		{			
			e.printStackTrace();			
		}	
		
		dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
		dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);			
		Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
		stage.setAlwaysOnTop(true);
		stage.toFront();
		Optional<ButtonType> result = dialog.showAndWait();
		
		if (result.isPresent() && result.get() == ButtonType.OK) 
		{
			TodoDialogController controller = (TodoDialogController) fxmlLoader.getController();
			TodoItem newItem = controller.processResults();						
			todoListView.getSelectionModel().select(newItem);
		}
		else
		{
			
		}
	}
}
