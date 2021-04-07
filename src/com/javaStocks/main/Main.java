package com.javaStocks.main;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main { //@DEFINE: main class to manage over-arching workings of the program. 'Front-end' console structure.

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Would you like to start the program with live data? (Y/N)");
		String choice = scan.next();
		
		if (choice.equalsIgnoreCase("Y")) {
			try {startOnline();}  //compressed try/catch block for the sake of readability and minimalism
			catch (FileNotFoundException e) {e.printStackTrace();}
			
		} else if (choice.equalsIgnoreCase("N")) {
			try {startOffline();}
			catch (FileNotFoundException e) {e.printStackTrace();}
			
		} else {
			System.err.println("Invalid input!");
			scan.close();
		}
		
	}
	
	public static void startOnline() throws FileNotFoundException {
		//grabber will start in this method (possibly DOSing the WebServer a bit) and take awhile so it can load live stock info into the sp500 hashmap.
		//TODO Use barchart technical analysis in order to grab current RSI & SMA data. You can tell whether incr/decr and how close they are (subtraction).
		
		System.out.print("Loading");
		Grabber get = new Grabber();
		StockScanner scan = new StockScanner(get);
		Notifier n = new Notifier();
		
		Scanner inputScan = new Scanner(System.in);
		String choice;
		boolean alive = true; //variable used to decide if the program still runs
		System.out.println("Loaded!\n");
		
		while (alive) {
			System.out.println("What would you like to do?");
			System.out.println("1)View/Open account (unsupported)\n2)Scan\n3)View Stock Info\n4)Daily Scan\n5)Exit");
			choice = inputScan.next();
			
			switch (choice) {
			case "1":
				//something
				break;
				
			case "2": //TODO outline different scan options
			    double max;
			    double min;
			    
				System.out.println("Input your bounds for the RSI scan.");
				
				System.out.print("Max: ");
				max = inputScan.nextDouble();
				
				System.out.print("Min: ");
				min = inputScan.nextDouble();
				
				System.out.print("Scanning");
				ArrayList<String> rsiStocks = scan.scanForRSI(max, min);
				System.out.println("Scan done!");
				
				String msgRSI = "";
				
				for (String s : rsiStocks) {
				  msgRSI += s + "\n";
				  
				}
				
				System.out.println(msgRSI);
				
				System.out.println("Notify? (Y/N)");
                choice = inputScan.next();
                
                if (choice.equals("y")) {
                    n.sendMessage("2017854112@txt.att.net", " Stocks", "\nToday's Low RSI Watchlist:\n" + msgRSI + "Period: Day"); //Liam Ramsey
                    n.sendMessage("2013985939@vtext.com", " Stocks", "\nToday's Low RSI Watchlist:\n" + msgRSI + "Period: Day"); //Will Cunningham
                    n.sendMessage("2016759036@txt.att.net", " Stocks", "\nToday's Low RSI Watchlist:\n" + msgRSI + "Period: Day"); //Matthew Allen
                    
                }
				
				break;
				
			case "3":
				String choice1 = "";
				while (!choice1.equalsIgnoreCase("EXIT")) {
					System.out.println("\nType a stock ticker or 'exit' below:");
					choice1 = inputScan.next().toUpperCase();
					
					Stock s = Grabber.SP500.get(choice1);
					if (s == null) {continue;}
					
					System.out.println(s.getCompany() + "\n$" + s.getPrice() + "\n" + s.getSector() + " | " + s.getIndustry());
					
				}
				break;
				
			case "4":
				//performs daily scan with notification to those on the list
				System.out.print("Scanning");
				ArrayList<String> stocks = scan.scan(.1, 50, 0);
				System.out.println("Scan done!");
				
				String msg = "";
				
				for (String s : stocks) {
					msg += s + "\n";
					
				}
				
				System.out.println(msg);
				
				System.out.println("Notify? (Y/N)");
				choice = inputScan.next();
				
				if (choice.equals("y")) {
					n.sendMessage("2017854112@txt.att.net", " Stocks", "\nToday's Watchlist:\n" + msg + "Period: Day"); //Liam Ramsey
					n.sendMessage("2013985939@vtext.com", " Stocks", "\nToday's Watchlist:\n" + msg + "Period: Day"); //Will Cunningham
					n.sendMessage("2016759036@txt.att.net", " Stocks", "\nToday's Watchlist:\n" + msg + "Period: Day"); //Matthew Allen
					
				}
				break;
				
			case "5" :
				System.out.println("Goodbye!");
				alive = false;
				break;
				
			default:
				System.out.println("Invalid input!");
				break;
			
			}
			
		}
		
		inputScan.close();
		
	}
	
	public static void startOffline() throws FileNotFoundException {
		//TODO grabber will NOT start. much faster startup (maybe read outdated version of sp500 hashmap from a text file??) and does not impost heavy network traffic.
		//@NOTE: don't bother reading from a cache if (a) you can't find a use for it or (b) it's a pain in the ass. offline mode can be used primarily for opening new accounts.
		
	}
	
}
