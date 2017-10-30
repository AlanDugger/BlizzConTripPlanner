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
			Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));			
			Scene scene = new Scene(root,600,600);
			primaryStage.initStyle(StageStyle.UNDECORATED);
			root.setId("mainPane");			
			System.out.println(getClass().getResource("style.css").toExternalForm());
			scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());						
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
