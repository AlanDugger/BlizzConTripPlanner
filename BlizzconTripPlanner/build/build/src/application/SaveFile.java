package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import datamodels.BudgetItem;
import datamodels.CostItem;
import datamodels.TodoItem;
import javafx.collections.ObservableList;

public class SaveFile 
{
	private static SaveFile instance = null;	
	private static int runCount = 0;
	private static String leftNotes = "";
	private static String rightNotes = "";
	private static String todoNotes = "";
	private static ArrayList<BudgetItem> budgetItems = new ArrayList<BudgetItem>();
	private static ArrayList<CostItem> costItems = new ArrayList<CostItem>();
	private static ArrayList<TodoItem> todoItems = new ArrayList<TodoItem>();
	final static String fileName = "blizzconapp.txt";
	
	
	
	//Avoids instantiation
	protected SaveFile()
	{		
	}
	
	public static SaveFile instanceOf()
	{		
		if (instance == null)
			instance = new SaveFile();
		
		return instance;
	}
	
	public static void loadData()
	{
		try
		{	
			// File reading variables
			FileReader fileReader = new FileReader(fileName);
			BufferedReader reader = new BufferedReader(fileReader);
			String line = null;
			
			// Tag interpretation variables
			boolean runDataTag = false;
			boolean leftNoteTag = false;
			boolean rightNoteTag = false;
			boolean budgetItemTag = false;
			boolean costItemTag = false;
			boolean todoItemTag = false;
			
			// Clear any current data to prepare for loading which can be called 
			// more than once per execution of application
			leftNotes = "";
			rightNotes = "";
			todoNotes = "";
			budgetItems.clear();
			costItems.clear();
			todoItems.clear();
			
			// Variables used for reading components of each entry
			int currBudgetPart = 0;
			int currCostPart = 0;
			int currTodoPart = 0;
			String[] costParts = new String[3];
			String[] parts = new String[4];
			String[] todoParts = new String[3];
			
			
			while ( (line = reader.readLine()) != null)
			{
				
				
				if (line.equals("<RunData>"))
				{
					runDataTag = true;
					continue;
				}
				else if (line.equals("</RunData>"))
				{
					runDataTag = false;
					continue;
				}
				else if (line.equals("<LeftNote>"))
				{
					leftNoteTag = true;
					continue;
				}
				else if (line.equals("</LeftNote>"))
				{
					leftNoteTag = false;
					continue;
				}
				else if (line.equals("<RightNote>"))
				{
					rightNoteTag = true;
					continue;
				}
				else if (line.equals("</RightNote>"))
				{
					rightNoteTag = false;
					continue;
				}
				else if (line.equals("<BudgetItem>"))
				{
					budgetItemTag = true;
					continue;
				}
				else if (line.equals("</BudgetItem>"))
				{
					budgetItemTag = false;
					continue;
				}
				else if (line.equals("<CostItem>"))
				{
					costItemTag = true;
					continue;
				}
				else if (line.equals("</CostItem>"))
				{
					costItemTag = false;
					continue;
				}
				else if (line.equals("<TodoItem>"))
				{
					todoItemTag = true;
					continue;
				}
				else if (line.equals("</TodoItem>"))
				{
					LocalDate date = LocalDate.parse(todoParts[0], DateTimeFormatter.ofPattern("MM/d/yyyy"));
					String name = todoParts[1];																				
					TodoItem tmpItem;				 		
					tmpItem = new TodoItem(name, todoNotes, date);
					todoItems.add(tmpItem);
					
					currTodoPart = 0;
					todoNotes = "";					
					todoItemTag = false;
					continue;
				}
				
				if (runDataTag)
				{
					String[] runParts = line.split(" ");
					runCount = Integer.parseInt(runParts[1]);
				}
				
				if (budgetItemTag)
				{
					
					if (currBudgetPart < 4)
					{
						parts[currBudgetPart] = line;
						currBudgetPart++;
					}
					
					if (currBudgetPart == 4)
					{
						String name = parts[0];
						double amount = Double.parseDouble(parts[1]);
						boolean paid = false;
						boolean seeNotes = false;
						if (parts[2].equals("yes"))
							paid = true;
						if (parts[3].equals("yes"))
							seeNotes = true;
					
					
						BudgetItem tmpItem;						
						tmpItem = new BudgetItem(name, amount, paid, seeNotes);
						budgetItems.add(tmpItem);
						currBudgetPart = 0;
					}
				}
				else if (costItemTag)
				{
					
					if (currCostPart < 3)
					{
						costParts[currCostPart] = line;
						currCostPart++;
					}
					
					if (currCostPart == 3)
					{
						LocalDate date = LocalDate.parse(costParts[0], DateTimeFormatter.ofPattern("MM/d/yyyy"));
						String name = costParts[1];						
						double amount = Double.parseDouble(costParts[2]);					
						CostItem tmpItem;				 		
						tmpItem = new CostItem(date, name, amount);
						costItems.add(tmpItem);
						currCostPart = 0;						
					}
				}
				else if (todoItemTag)
				{					
					if (currTodoPart < 2)
					{
						todoParts[currTodoPart] = line;
						currTodoPart++;
					}
					else
					{
						if (line.equals("~"))
						{
							todoNotes += System.getProperty("line.separator");						
						}
						else if (todoNotes.equals(""))
							todoNotes += line;
					}
														
				}
				else if (leftNoteTag)
				{					
					if (line.equals("~"))
					{
						leftNotes += System.getProperty("line.separator");						
					}
					else if (leftNotes.equals(""))
						leftNotes += line;
					else
					{
						leftNotes += System.getProperty("line.separator");
						leftNotes += line;
					}				
					
				}
				else if (rightNoteTag)
				{
					if (line.equals("~"))
					{
						rightNotes += System.getProperty("line.separator");						
					}
					else if (rightNotes.equals(""))
						rightNotes += line;
					else
					{
						rightNotes += System.getProperty("line.separator");
						rightNotes += line;
					}									
				}
				else
					continue;				
			}	
			reader.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}			
	}
	
	public static void saveData()
	{		
		try
		{
			FileWriter fileWriter = new FileWriter(fileName);
			PrintWriter writer = new PrintWriter(fileWriter);
			BufferedReader leftNoteReader = new BufferedReader(new StringReader(leftNotes));
			BufferedReader rightNoteReader = new BufferedReader(new StringReader(rightNotes));
						
			writer.flush();
			writer.println("<RunData>");			
			
			writer.println("runCount " + runCount);
			
			writer.println("</RunData>");
			
			writer.println("<LeftNote>");
			
			leftNoteReader.lines().forEach(line -> {if (line.length() > 0) writer.println(line); else writer.println("~");});
			
			writer.println("</LeftNote>");
			
			writer.println("<RightNote>");
			
			rightNoteReader.lines().forEach(line -> {if (line.length() > 0) writer.println(line); else writer.println("~");});
			
			
			writer.println("</RightNote>");
			
			if (budgetItems != null && !budgetItems.isEmpty())
			{
				for (BudgetItem item : budgetItems)
				{
					writer.println("<BudgetItem>");
					writer.println(item.getEntryName());
					
					writer.println(item.getBudgetAmountValue());
					
					writer.println(item.getAlreadyPaid());
					
					writer.println(item.getSeeNotes());
					writer.println("</BudgetItem>");
				}
			}
			
			if (costItems != null && !costItems.isEmpty())
			{
				for (CostItem item : costItems)
				{
					writer.println("<CostItem>");
					writer.println(item.getEntryDate());
					
					writer.println(item.getEntryName());
					
					writer.println(item.getCostAmountValue());										
					
					writer.println("</CostItem>");
				}
			}
			
			if (todoItems != null && !todoItems.isEmpty())
			{
				for (TodoItem item : todoItems)
				{
					BufferedReader detailsReader = new BufferedReader(new StringReader(item.getDetails()));
					writer.println("<TodoItem>");
					writer.println(item.getDeadlineString());
					writer.println(item.getShortDesc());					
					detailsReader.lines().forEach(line -> {if (line.length() > 0) writer.println(line); else writer.println("~");});
					writer.println("</TodoItem>");
				}
			}
			
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static ArrayList<BudgetItem> getBudgetItems()
	{
		return budgetItems;
	}
	
	public static ArrayList<TodoItem> getTodoItems()
	{
		return todoItems;
	}
	
	public static void updateTodoItems(ObservableList<TodoItem> items)
	{
		todoItems.clear();
		if (items == null || items.isEmpty())
			return;
		
		todoItems.addAll(items);				
	}
	
	public static void updateBudgetItems(ObservableList<BudgetItem> items)
	{
		budgetItems.clear();		
		
		if (items == null || items.isEmpty())
			return;
		
		budgetItems.addAll(items);

	}
	
	public static ArrayList<CostItem> getCostItems()
	{
		return costItems;
	}
	
	public static void updateCostItems(ObservableList<CostItem> items)
	{
		costItems.clear();		
		
		if (items == null || items.isEmpty())
			return;
		
		costItems.addAll(items);

	}
	
	public static void writeNotes(String leftNoteToAdd, String rightNoteToAdd)
	{
		leftNotes = leftNoteToAdd;
		rightNotes = rightNoteToAdd;
	}
	
	public static int getRunCount()
	{
		return runCount;
	}
	
	public static String getLeftNotes()
	{
		return leftNotes;
	}
	
	public static String getRightNotes()
	{
		return rightNotes;
	}
	
	public static void incrementRunCounter()
	{
		runCount++;
	}
	
}
