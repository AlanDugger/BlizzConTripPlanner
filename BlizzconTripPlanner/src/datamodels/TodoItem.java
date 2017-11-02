package datamodels;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TodoItem {
	
	private String shortDesc;
	private String details;
	private LocalDate deadline;
	
	public TodoItem(String shortDesc, String details, LocalDate deadline) 
	{		
		this.shortDesc = shortDesc;
		this.details = details;
		this.deadline = deadline;
	}

	public String getShortDesc() {
		return shortDesc;
	}

	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public LocalDate getDeadline() {
		return deadline;
	}

	public void setDeadline(LocalDate deadline) {
		this.deadline = deadline;
	}
	
	public String getDeadlineString() {		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/d/yyyy");
		String date = formatter.format(deadline);
		return date;
	}
	
	@Override
	public String toString() {
		return shortDesc;
	}
}
