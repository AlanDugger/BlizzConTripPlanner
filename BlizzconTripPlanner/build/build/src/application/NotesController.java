package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class NotesController 
{		
	@FXML
	private Button btnNotesExit;
	@FXML
	private TextArea leftNotesTextArea;	
	@FXML
	private TextArea rightNotesTextArea;
	@FXML
	private GridPane notesPane;
	
	public void initialize()
	{
		readNotesData();
	}
	
	
	@FXML
	public void exitNoteMenu()
	{
		SaveFile.writeNotes(leftNotesTextArea.getText(), rightNotesTextArea.getText());		
		Stage stage = (Stage) btnNotesExit.getScene().getWindow();				
		stage.close();
	}
	
	public void readNotesData()
	{
		leftNotesTextArea.setText(SaveFile.getLeftNotes());
		rightNotesTextArea.setText(SaveFile.getRightNotes());
	}
		
}
