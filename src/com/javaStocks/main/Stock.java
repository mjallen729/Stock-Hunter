package com.javaStocks.main;

public class Stock { //@DEFINE: basic stock object used to manage stock information

	private String ticker;
	private String coName;
	private double price;
	private String industry;
	private String sector;
	
	public Stock(String ticker) {
		this.ticker = ticker;
		
	}
	
	public void setCompany(String s) {
		this.coName = s;
		
	}
	
	public void setPrice(double d) {
		this.price = d;
		
	}
	
	public String getTicker() {
		return this.ticker;
		
	}
	
	public String getCompany() {
		return this.coName;
		
	}
	
	public double getPrice() {
		return this.price;
		
	}
	
	public void setIndustry(String s) {
		this.industry = s;
		
	}
	
	public String getIndustry() {
		return this.industry;
		
	}
	
	public void setSector(String s) {
		this.sector = s;
		
	}
	
	public String getSector() {
		return this.sector;
		
	}
	
}
