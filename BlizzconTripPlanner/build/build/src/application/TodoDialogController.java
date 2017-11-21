package application;

import java.time.LocalDate;

import datamodels.TodoData;
import datamodels.TodoItem;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class TodoDialogController {
	@FXML
	private TextField shortDescr;
	@FXML
	private TextArea detailsArea;
	@FXML
	private DatePicker deadlinePicker;

	public TodoItem processResults() 
	{
		String shortDesc = shortDescr.getText().trim();
		String details = detailsArea.getText().trim();
		
		LocalDate deadlineValue = deadlinePicker.getValue();
		
		TodoItem newItem = new TodoItem(shortDesc, details, deadlineValue);
		TodoData.getInstance().addTodoItem(newItem);
		return newItem;
	}
}
