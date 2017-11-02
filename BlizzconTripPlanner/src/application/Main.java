package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application 
{
	@Override
	public void start(Stage primaryStage) 
	{					
		try 
		{
			Parent root = FXMLLoader.load(getClass().getResource("/views/MainWindow.fxml"));			
			Scene scene = new Scene(root,600,600);
			primaryStage.initStyle(StageStyle.UNDECORATED);	
			scene.getStylesheets().add(getClass().getResource("/views/style.css").toExternalForm());						
			primaryStage.setScene(scene);						
			primaryStage.setResizable(false);
			primaryStage.show();				
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
		
	
	public static void main(String[] args) 
	{
		launch(args);
	}
}
