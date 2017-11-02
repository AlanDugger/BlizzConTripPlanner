package datamodels;

import java.util.ArrayList;

import application.SaveFile;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TodoData {
	private static TodoData instance = new TodoData();		
	private ObservableList<TodoItem> todoItems = FXCollections.observableArrayList();	
	
	public static TodoData getInstance() {
		return instance;
	}
	
	private TodoData() {
				
	}
	
	public ObservableList<TodoItem> getTodoItems(){
		return todoItems;
	}
	
	public void deleteTodoItem(TodoItem item)
	{
		todoItems.remove(item);
	}
	
	public void addTodoItem(TodoItem item) 
	{
		todoItems.add(item);
	}
	
	public void saveTodoItems()
	{
		SaveFile.updateTodoItems(todoItems);
	}
	
	public void loadTodoItems()
	{
		ArrayList<TodoItem> loadedTodoItems = SaveFile.getTodoItems();
		
		todoItems.clear();
		
		if (loadedTodoItems == null || loadedTodoItems.isEmpty())
			return;
		
		for (TodoItem item : loadedTodoItems)		
			todoItems.add(item);		
	}
}