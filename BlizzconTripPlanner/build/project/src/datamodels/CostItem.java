package datamodels;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class CostItem 
{
	private SimpleObjectProperty<LocalDate> entryDate = new SimpleObjectProperty<LocalDate>();
	private SimpleStringProperty entryName = new SimpleStringProperty();
	private SimpleObjectProperty<BigDecimal> costAmount = new SimpleObjectProperty<BigDecimal>();	
	
	public CostItem(LocalDate date, String entryName, double costAmount) 
	{		
		this.entryDate.set(date);
		this.entryName.set(entryName);
		this.costAmount.set(new BigDecimal(costAmount));
	}
	
	public String getEntryName() {
		return entryName.getValue();
	}

	public String getCostAmount() {
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		return formatter.format(costAmount.getValue()); 
	}
	
	public String getEntryDate() {
		LocalDate dateToParse = entryDate.getValue();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/d/yyyy");
		String date = formatter.format(dateToParse);
		return date;		
	}
	
	public void setEntryName(String entryName) {
		this.entryName.set(entryName);
	}

	public void setCostAmount(double costAmount) {
		this.costAmount.set(new BigDecimal(costAmount));
	}
	
	public void setEntryDate(LocalDate date) {
		this.entryDate.set(date);
	}
	
	public BigDecimal getCostAmountValue() {
		return costAmount.getValue();
	}
}
