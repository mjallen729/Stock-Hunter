package com.javaStocks.support;

public class MovingAvgRSI {
	private double fiftyDayMoving;
	private double twohunDayMoving;
	private double rsi;
	
	public MovingAvgRSI(double fiftyDay, double twohunDay, double RSI) {
		this.fiftyDayMoving = fiftyDay;
		this.twohunDayMoving = twohunDay;
		this.rsi = RSI;
		
	}
	
	public double getFifty() {
		return fiftyDayMoving;
		
	}
	
	public double getTwoHundred() {
		return twohunDayMoving;
		
	}
	
	public double getRSI() {
		return rsi;
		
	}
	
}
