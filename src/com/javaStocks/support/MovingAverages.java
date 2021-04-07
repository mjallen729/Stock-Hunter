package com.javaStocks.support;

public class MovingAverages {
	
	private double fiftyDayMoving;
	private double twohunDayMoving;
	
	public MovingAverages(double fiftyDay, double twohunDay) {
		this.fiftyDayMoving = fiftyDay;
		this.twohunDayMoving = twohunDay;
		
	}
	
	public double getFifty() {
		return fiftyDayMoving;
		
	}
	
	public double getTwoHundred() {
		return twohunDayMoving;
		
	}

}
