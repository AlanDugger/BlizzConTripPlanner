package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;

import javafx.collections.ObservableList;

public class SaveFile 
{
	private static SaveFile instance = null;	
	private static int runCount = 0;
	private static String leftNotes = "";
	private static String rightNotes = "";
	private static ArrayList<BudgetItem> budgetItems = new ArrayList<BudgetItem>();
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
			FileReader fileReader = new FileReader(fileName);
			BufferedReader reader = new BufferedReader(fileReader);
			String line = null;			
			boolean runDataTag = false;
			boolean leftNoteTag = false;
			boolean rightNoteTag = false;
			boolean budgetItemTag = false;
			leftNotes = "";
			rightNotes = "";
			budgetItems.clear();
			int currBudgetPart = 0;
			String[] parts = new String[4];
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
	
	public static void updateBudgetItems(ObservableList<BudgetItem> items)
	{
		budgetItems.clear();		
		
		if (items == null || items.isEmpty())
			return;
		
		budgetItems.addAll(items);

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
