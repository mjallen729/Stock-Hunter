package com.javaStocks.main;

import java.util.ArrayList;
import com.javaStocks.support.MovingAverages;
import com.javaStocks.support.MovingAvgRSI;

public class StockScanner {
	//@NOTE: All bounds of limits (price, rsi, etc.) are INCLUSIVE for all values!
	private Grabber get;
	
	public StockScanner (Grabber g) {
		this.get = g;
		
	}
	
	/*@param:
	 * maxDistance: max distance between fiftyday and twohundredday moving averages
	 * upperlimit: maximum rsi to scan for
	 * lowerlimit: minimum rsi to scan for
	 */
	public ArrayList<String> scan(double maxDistance, double upperLimit, double lowerLimit) { //Main scanner. Scans for bounded rsi & golden crosses
		ArrayList<String> bestStocks = new ArrayList<String>();
		
		int i = 0;
		for(String s : Grabber.tickers) {
			MovingAvgRSI all = get.movingRSI(s);
			double fDay = all.getFifty();
			double thDay = all.getTwoHundred();
			double rsi = all.getRSI();
			
			double N = thDay - fDay;
			double threshold = maxDistance * thDay;
			
			if (fDay > 0 && N >= 0 && N <= threshold) { //moving average check
				if (rsi >= lowerLimit && rsi <= upperLimit) { //rsi check
					bestStocks.add(s);
					
				}
				
			}
			if (++i % 10 == 0) {
				System.out.print(".");
			}
			
		}
		
		
		return bestStocks;
		
	}
	
	public ArrayList<String> scanWithPrice(double maxDistance, double upperLimit, double lowerLimit, double priceAbove, double priceBelow) { //Main scanner with price limits
		ArrayList<String> bestStocks = new ArrayList<String>();
		
		for(String s : Grabber.tickers) {
			MovingAvgRSI all = get.movingRSI(s);
			double fDay = all.getFifty();
			double thDay = all.getTwoHundred();
			double rsi = all.getRSI();
			
			double N = thDay - fDay;
			double threshold = maxDistance * thDay;
			
			if (fDay > 0 && N >= 0 && N <= threshold) { //moving average check
				if (rsi >= lowerLimit && rsi <= upperLimit) { //rsi check
					if (Grabber.SP500.get(s).getPrice() >= priceAbove && Grabber.SP500.get(s).getPrice() <= priceBelow) { //price check
						bestStocks.add(s);
						
					}
					
				}
				
			}
			
		}
		
		
		return bestStocks;
		
	}
	
	
	// For Golden Cross: Slope of 50 day must be positive; 200d - 50d >= N, N >= 0, N <= maxDistance.
	
	public ArrayList<String> scanForGC(double maxDistance) { //scans for golden crosses with a certain distance (maxDistance)(%) between the two lines
		ArrayList<String> goodStocks = new ArrayList<String>();
		
		for (String s : get.tickers) {
			MovingAverages both = get.avgDayMoving(s);
			double fDay = both.getFifty();
			double thDay = both.getTwoHundred();
			
			double N = thDay - fDay;
			double threshold = maxDistance * thDay;
			
			if (fDay > 0 && N >= 0 && N <= threshold) { 
				goodStocks.add(s);
				
			}
			
		}
		
		return goodStocks;
		
	}
	
	public ArrayList<String> scanForRSI(double upperLimit, double lowerLimit) { //scans for bounded RSI
		ArrayList<String> goodStocks = new ArrayList<String>();
		
		int i = 0;
		for (String s : Grabber.tickers) {
			double rsi = get.RSI(s);
			
			if (rsi >= lowerLimit && rsi <= upperLimit) {
				goodStocks.add(s);
				
			}
			
			if (++i % 10 == 0) {
              System.out.print(".");
            }
			
		}
		
		return goodStocks;
		
	}

}
