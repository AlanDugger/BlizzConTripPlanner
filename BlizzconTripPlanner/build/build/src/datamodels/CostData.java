package datamodels;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;

import application.SaveFile;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CostData 
{
	private static CostData instance = new CostData();
	private ObservableList<CostItem> costItems = FXCollections.observableArrayList();
		
	public static CostData getInstance() {
		return instance;
	}
	
	protected CostData() {
				
	}
	
	public ObservableList<CostItem> getCostItems()
	{
		return costItems;
	}

	public void loadCostItems()
	{
		ArrayList<CostItem> loadedCostItems = SaveFile.getCostItems();
		
		costItems.clear();
		
		if (loadedCostItems == null || loadedCostItems.isEmpty())
			return;
		
		for (CostItem item : loadedCostItems)		
			costItems.add(item);		
	}
	
	public void saveCostItems()
	{
		SaveFile.updateCostItems(costItems);
	}
	
	public void deleteCostItem(CostItem item)
	{
		costItems.remove(item);
	}
	
	public void addCostItem(CostItem item) 
	{
		costItems.add(item);
	}
	
	public SimpleStringProperty getSubtotal(String type)
	{
		BigDecimal total = new BigDecimal("0.00");
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		
		if (costItems == null)
			return new SimpleStringProperty(formatter.format(total.toString()));
		
		for (CostItem c : costItems)
		{
			if (type.equals("Other"))
			{
				if (!c.getEntryName().equals("Gas") && !c.getEntryName().equals("Food") && !c.getEntryName().equals("Lodging") && !c.getEntryName().equals("Merchandise"))
					total = total.add(c.getCostAmountValue());
			}
			else
			{
				if (type.equals(c.getEntryName()))
					total = total.add(c.getCostAmountValue());		
			}					
		}
		
		return new SimpleStringProperty(formatter.format(Double.parseDouble(total.toString())));
	}
	
	public SimpleStringProperty getTotalCost()
	{
		BigDecimal total = new BigDecimal("0.00");		
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		
		if (costItems == null)		
			return new SimpleStringProperty(formatter.format(total.toString()));			
				
		for (CostItem c : costItems)
			total = total.add(c.getCostAmountValue());	
		
		return new SimpleStringProperty(formatter.format(Double.parseDouble(total.toString())));
	}

}
