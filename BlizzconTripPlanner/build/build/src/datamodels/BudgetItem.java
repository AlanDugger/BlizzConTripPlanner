package datamodels;

import java.math.BigDecimal;
import java.text.NumberFormat;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class BudgetItem 
{
	private SimpleStringProperty entryName = new SimpleStringProperty();
	private SimpleObjectProperty<BigDecimal> budgetAmount = new SimpleObjectProperty<BigDecimal>();
	private SimpleStringProperty alreadyPaid = new SimpleStringProperty();
	private SimpleStringProperty seeNotes = new SimpleStringProperty();
	
	public BudgetItem(String entryName, double budgetAmount, boolean alreadyPaid, boolean seeNotes) 
	{		
		this.entryName.set(entryName);
		this.budgetAmount.set(new BigDecimal(budgetAmount));
		if (alreadyPaid)
			this.alreadyPaid.set("yes");
		else
			this.alreadyPaid.set("no");
		if (seeNotes)
			this.seeNotes.set("yes");
		else
			this.seeNotes.set("no");
	}

	public String getEntryName() {
		return entryName.getValue();
	}

	public String getBudgetAmount() {
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		return formatter.format(budgetAmount.getValue()); 
	}
	
	public BigDecimal getBudgetAmountValue() {
		return budgetAmount.getValue();
	}

	public String getAlreadyPaid() {
		return alreadyPaid.getValue();
	}

	public String getSeeNotes() {
		return seeNotes.getValue();
	}
	
	@Override
	public String toString()
	{
		return entryName.getValue();
	}

	public void setEntryName(String entryName) {
		this.entryName.set(entryName);
	}

	public void setBudgetAmount(double budgetAmount) {
		this.budgetAmount.set(new BigDecimal(budgetAmount));
	}

	public void setAlreadyPaid(boolean alreadyPaid) {
		if (alreadyPaid)
			this.alreadyPaid.set("yes");
		else
			this.alreadyPaid.set("no");
	}

	public void setSeeNotes(boolean seeNotes) 
	{
		if (seeNotes)
			this.seeNotes.set("yes");
		else
			this.seeNotes.set("no");		
	}	
		
}
