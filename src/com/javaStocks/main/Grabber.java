package com.javaStocks.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.javaStocks.support.MovingAverages;
import com.javaStocks.support.MovingAvgRSI;

public class Grabber { //@DEFINE: creates S&P list via dataSet.txt, works with stock object to calculate data, grabs live data from web servers for stock obj
	
	public static HashMap<String,Stock> SP500 = new HashMap<String,Stock>();
	public static ArrayList<String> tickers = new ArrayList<String>();
	
	public Grabber() throws FileNotFoundException {
		File data = new File("dataSet.txt");
		Scanner scan = new Scanner(data);
		
		int i = 0;
		while (scan.hasNextLine()) {
			String ticker = scan.next();
			Stock stk = new Stock(ticker);
			Document doc = null;
			
			try {
				doc = Jsoup.connect("https://finviz.com/quote.ashx?t=" + ticker).userAgent("Mozilla/17.0").get();
				
			} catch (IOException e) {
				System.err.println("ERROR: GRABBER.JAVA FAILED TO CONNECT TO URL ~/quote.ashx?t=" + ticker);
				continue;
				
			}
			
			Elements temp = doc.select("table.fullview-title"); //sets company, sector, and industry
			
			for (Element e : temp) {
				stk.setCompany(e.getElementsByClass("tab-link").get(0).text());
				stk.setSector(e.getElementsByClass("tab-link").get(1).text());
				stk.setIndustry(e.getElementsByClass("tab-link").get(2).text());
				
			}
			
			temp = doc.select("table.snapshot-table2"); //sets price
			
			for (Element e : temp) {
				stk.setPrice(Double.valueOf(e.getElementsByClass("table-dark-row").get(10).getElementsByClass("snapshot-td2").get(5).text()));
				
			}
			
			SP500.put(ticker, stk);
			tickers.add(ticker);
			if (++i % 10 == 0) {
				System.out.print(".");
			}
			
		}
		
	}
	
	public MovingAverages avgDayMoving(String ticker) { //returns object storing 50 day & 200 day moving average. Will return negative if slope is negative.
		Document doc = null;
		MovingAverages returner;
		
		try {
			doc = Jsoup.connect("https://www.barchart.com/stocks/quotes/" + ticker + "/technical-analysis").userAgent("Mozilla/17.0").get();
			
		} catch (IOException e) {
			System.err.println("ERROR: GRABBER.JAVA.AVGDAYMOVING() FAILED TO CONNECT TO URL ~/stocks/quotes/" + ticker + "/technical-analysis");
			return new MovingAverages(-1.0,-1.0);
			
		}
		
		Elements temp = doc.getElementsByTag("table").get(0).getElementsByTag("tbody").get(0).getElementsByTag("td");
		
		boolean next = false;
		double ret = 0.0;
		
		for (Element e : temp) {
			
			if (next) {
				if (e.className().equals("down")) {
					try {ret = 0 - Double.parseDouble(e.text().replace(",", ""));}
					catch (NumberFormatException n) {return new MovingAverages(-1.0,-1.0);}
					
				} else {
					try {ret = Double.parseDouble(e.text().replace(",",""));}
					catch (NumberFormatException n) {return new MovingAverages(-1.0,-1.0);}
					
				}
				
				break;
				
			} else if (e.text().equals("50-Day")) {
				next = true;
				
			}
			
		}
		
		boolean next2 = false;
		double ret2 = 0.0;
		
		for (Element e : temp) {
			
			if (next2) {
				if (e.className().equals("down")) {
					try {ret2 = 0 - Double.parseDouble(e.text().replace(",", ""));}
					catch (NumberFormatException n) {return new MovingAverages(-1.0,-1.0);}
					
				} else {
					try {ret2 = Double.parseDouble(e.text().replace(",",""));}
					catch (NumberFormatException n) {return new MovingAverages(-1.0,-1.0);}
				}
				
				break;
				
			} else if (e.text().equals("200-Day")) {
				next2 = true;
				
			}
			
		}
		
		returner = new MovingAverages(ret,ret2);
		return returner;
		
	}
	
	public double RSI(String ticker) { //returns 14 day RSI for a particular stock
		Document doc = null;
		double ret = 0.0;
		
		try {
			doc = Jsoup.connect("https://www.barchart.com/stocks/quotes/" + ticker + "/technical-analysis").userAgent("Mozilla/17.0").get();
			
		} catch (IOException e) {
			System.err.println("ERROR: GRABBER.JAVA.RSI() FAILED TO CONNECT TO URL ~/stocks/quotes/" + ticker + "/technical-analysis");
			return 0.0;
			
		}
		
		Elements temp = doc.getElementsByTag("table").get(2).getElementsByTag("tbody").get(0).getElementsByTag("td");
		
		boolean next = false;
		
		for (Element e : temp) {
			
			if (next) {
				try {ret = Double.parseDouble(e.text().replace("%", ""));}
				catch (NumberFormatException n) {return -1.0;}
				
				break;
				
			} else if (e.text().equals("14-Day")) {
				next = true;
				
			}
			
		}
		
		return ret;
	}
	
	
	public MovingAvgRSI movingRSI(String ticker) { //this method returns both moving averages and rsi
		Document doc = null;
		MovingAvgRSI returner;
		
		try {
			doc = Jsoup.connect("https://www.barchart.com/stocks/quotes/" + ticker + "/technical-analysis").userAgent("Mozilla/17.0").get();
			
		} catch (IOException e) {
			System.err.println("ERROR: GRABBER.JAVA.MOVINGRSI() FAILED TO CONNECT TO URL ~/stocks/quotes/" + ticker + "/technical-analysis");
			return new MovingAvgRSI(-1.0,-1.0,-1.0);
			
		}
		
		Elements temp = doc.getElementsByTag("table").get(0).getElementsByTag("tbody").get(0).getElementsByTag("td");
		
		//50 day moving average
		boolean next = false;
		double ret = 0.0;
		
		for (Element e : temp) {
			
			if (next) {
				if (e.className().equals("down")) {
					try {ret = 0 - Double.parseDouble(e.text().replace(",", ""));}
					catch (NumberFormatException n) {return new MovingAvgRSI(-1.0,-1.0, -1.0);}
					
				} else {
					try {ret = Double.parseDouble(e.text().replace(",",""));}
					catch (NumberFormatException n) {return new MovingAvgRSI(-1.0,-1.0, -1.0);}
					
				}
				
				break;
				
			} else if (e.text().equals("50-Day")) {
				next = true;
				
			}
			
		}
		
		//200 day moving average
		boolean next2 = false;
		double ret2 = 0.0;
		
		for (Element e : temp) {
			
			if (next2) {
				if (e.className().equals("down")) {
					try {ret2 = 0 - Double.parseDouble(e.text().replace(",", ""));}
					catch (NumberFormatException n) {return new MovingAvgRSI(-1.0,-1.0,-1.0);}
					
				} else {
					try {ret2 = Double.parseDouble(e.text().replace(",",""));}
					catch (NumberFormatException n) {return new MovingAvgRSI(-1.0,-1.0,-1.0);}
				}
				
				break;
				
			} else if (e.text().equals("200-Day")) {
				next2 = true;
				
			}
			
		}
		
		//14 day rsi
		boolean next1 = false;
		double ret1 = 0.0;
		
		temp = doc.getElementsByTag("table").get(2).getElementsByTag("tbody").get(0).getElementsByTag("td");
		
		for (Element e : temp) {
			
			if (next1) {
				try {ret1 = Double.parseDouble(e.text().replace("%", ""));}
				catch (NumberFormatException n) {return new MovingAvgRSI(-1.0,-1.0,-1.0);}
				
				break;
				
			} else if (e.text().equals("14-Day")) {
				next1 = true;
				
			}
			
		}
		
		returner = new MovingAvgRSI(ret,ret2,ret1);
		return returner;
		
	}
	
}