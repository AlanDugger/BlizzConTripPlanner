package application;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BudgetData 
{
	private static BudgetData instance = new BudgetData();
	private ObservableList<BudgetItem> budgetItems = FXCollections.observableArrayList();
		
	public static BudgetData getInstance() {
		return instance;
	}
	
	protected BudgetData() {
				
	}
	
	public ObservableList<BudgetItem> getBudgetItems()
	{
		return budgetItems;
	}

	public void loadBudgetItems()
	{
		ArrayList<BudgetItem> loadedBudgetItems = SaveFile.getBudgetItems();
		
		budgetItems.clear();
		
		if (loadedBudgetItems == null || loadedBudgetItems.isEmpty())
			return;
		
		for (BudgetItem item : loadedBudgetItems)		
			budgetItems.add(item);		
	}
	
	public void saveBudgetItems()
	{
		SaveFile.updateBudgetItems(budgetItems);
	}
	
	public void deleteBudgetItem(BudgetItem item)
	{
		budgetItems.remove(item);
	}
	
	public void addBudgetItem(BudgetItem item) 
	{
		budgetItems.add(item);
	}
	
	
	public SimpleStringProperty getTotalPaid()
	{
		BigDecimal total = new BigDecimal("0.00");		
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		
		if (budgetItems == null)
		{
			return new SimpleStringProperty(formatter.format(total.toString()));			
		}
		
		for (BudgetItem b : budgetItems)
		{
			if (b.getAlreadyPaid().equals("yes"))						
				total = total.add(b.getBudgetAmountValue());
			
		}		
		
		return new SimpleStringProperty(formatter.format(Double.parseDouble(total.toString())));
	}
	
	public SimpleStringProperty getTotalRemaining()
	{
		BigDecimal total = new BigDecimal("0.00");		
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		
		if (budgetItems == null)
		{
			return new SimpleStringProperty(formatter.format(total.toString()));			
		}
		
		for (BudgetItem b : budgetItems)
		{
			if (b.getAlreadyPaid().equals("no"))						
				total = total.add(b.getBudgetAmountValue());
			
		}		
		
		return new SimpleStringProperty(formatter.format(Double.parseDouble(total.toString())));
	}
}
