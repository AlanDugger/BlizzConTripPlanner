package application;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class Controller 
{
	final String dataFile = "blizzconapp.txt";	
	
	@FXML
	private Button btnTodoListMenu;	
	@FXML
	private Button btnNotesMenu;
	@FXML
	private Button btnBudgetMenu;
	@FXML
	private Button btnCostMenu;
	@FXML
	private Button btnMainExit;
	
	@FXML
	private GridPane mainPane;
	
	@FXML
	private Label runCount;
	
	public void initialize()
	{
		
		SaveFile.loadData();				
		SaveFile.incrementRunCounter();
		runCount.setText("Application launched " + SaveFile.getRunCount() + (SaveFile.getRunCount() == 1 ? " time." : " times."));
		addDraggableNode(mainPane);
	}
	
	@FXML
	public void exitApplication()
	{
		SaveFile.saveData();
		Platform.exit();
	}
	
	/* Create the Todo stage */
	@FXML
	public void initTodoListWindow()
	{	
		try
		{
			toggleButton(btnTodoListMenu);
			Parent root = FXMLLoader.load(getClass().getResource("/views/TodoWindow.fxml"));
			Stage stage = new Stage();
			Scene scene = new Scene(root, 1000, 560);
			scene.getStylesheets().addAll(this.getClass().getResource("/views/style.css").toExternalForm());
			stage.setAlwaysOnTop(true);			
			stage.centerOnScreen();
			stage.initStyle(StageStyle.UNDECORATED);													
			stage.setScene(scene);
			stage.show();
			addDraggableNode(root);
			stage.setOnHiding(new EventHandler<WindowEvent>()
			{
				@Override
				public void handle(final WindowEvent arg0)
				{
					toggleButton(btnTodoListMenu);
				}
			});
			
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() 
			{
				@Override
				public void handle(final WindowEvent arg0) 
				{					
					toggleButton(btnTodoListMenu);						
				}
			
			});
			
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/* Create the budget stage */
	@FXML
	public void initBudgetWindow()
	{	
		try
		{
			toggleButton(btnBudgetMenu);
			Parent root = FXMLLoader.load(getClass().getResource("/views/BudgetWindow.fxml"));
			Stage stage = new Stage();
			Scene scene = new Scene(root, 1000, 560);
			scene.getStylesheets().addAll(this.getClass().getResource("/views/style.css").toExternalForm());
			stage.setAlwaysOnTop(true);			
			stage.centerOnScreen();
			stage.initStyle(StageStyle.UNDECORATED);													
			stage.setScene(scene);
			stage.show();
			addDraggableNode(root);
			stage.setOnHiding(new EventHandler<WindowEvent>()
			{
				@Override
				public void handle(final WindowEvent arg0)
				{
					toggleButton(btnBudgetMenu);
				}
			});
			
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() 
			{
				@Override
				public void handle(final WindowEvent arg0) 
				{					
					toggleButton(btnBudgetMenu);						
				}
			
			});
			
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/* Create the cost stage */
	@FXML
	public void initCostWindow()
	{	
		try
		{
			toggleButton(btnCostMenu);
			Parent root = FXMLLoader.load(getClass().getResource("/views/CostWindow.fxml"));
			Stage stage = new Stage();
			Scene scene = new Scene(root, 1000, 500);
			scene.getStylesheets().addAll(this.getClass().getResource("/views/style.css").toExternalForm());
			stage.setAlwaysOnTop(true);			
			stage.centerOnScreen();
			stage.initStyle(StageStyle.UNDECORATED);													
			stage.setScene(scene);
			stage.show();
			addDraggableNode(root);
			stage.setOnHiding(new EventHandler<WindowEvent>()
			{
				@Override
				public void handle(final WindowEvent arg0)
				{
					toggleButton(btnCostMenu);
				}
			});
			
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() 
			{
				@Override
				public void handle(final WindowEvent arg0) 
				{					
					toggleButton(btnCostMenu);						
				}
			
			});
			
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/* Create the Notes stage */
	@FXML
	public void initNotesWindow()
	{
		try
		{				
			toggleButton(btnNotesMenu);			
			Parent root = FXMLLoader.load(getClass().getResource("/views/NotesWindow.fxml"));
			Stage stage = new Stage();
			Scene scene = new Scene(root, 1000, 500);
			scene.getStylesheets().addAll(this.getClass().getResource("/views/style.css").toExternalForm());
			stage.setAlwaysOnTop(true);			
			stage.centerOnScreen();
			stage.initStyle(StageStyle.UNDECORATED);													
			stage.setScene(scene);
			stage.show();
			addDraggableNode(root);
			stage.setOnHiding(new EventHandler<WindowEvent>()
			{
				@Override
				public void handle(final WindowEvent arg0)
				{
					toggleButton(btnNotesMenu);
				}
			});
			
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() 
			{
				@Override
				public void handle(final WindowEvent arg0) 
				{					
					toggleButton(btnNotesMenu);						
				}
			
			});
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}		
	}
	
	/* Method to move window node so we can move stages w/o headers */
	private void addDraggableNode(final Node node) 
	{
		DragDelta dragDelta = new DragDelta(); 
	    node.setOnMousePressed(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent me) {
	            if (me.getButton() != MouseButton.MIDDLE) {
	                dragDelta.x = me.getSceneX();
	                dragDelta.y = me.getSceneY();
	            }
	        }
	    });

	    node.setOnMouseDragged(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent me) {
	            if (me.getButton() != MouseButton.MIDDLE) {
	                node.getScene().getWindow().setX(me.getScreenX() - dragDelta.x);
	                node.getScene().getWindow().setY(me.getScreenY() - dragDelta.y);
	            }
	        }
	    });
	}
	
	/* Class for moveable window screens w/o headers */
	class DragDelta
	{
		double x;
		double y;
	}
	
	/* Deactivates the menu button, but leaves it visible when the corresponding menu is already opened 
	 * Also disables main Exit (which calls the main save method) until all other windows are closed */
	private void toggleButton(Button button)
	{
		if (button.isDisabled())
			button.setDisable(false);
		else
			button.setDisable(true);
		
		if (btnTodoListMenu.isDisabled() || btnNotesMenu.isDisabled() || btnBudgetMenu.isDisabled() || btnCostMenu.isDisabled())
			btnMainExit.setDisable(true);
		else
			btnMainExit.setDisable(false);
	}
	
}
