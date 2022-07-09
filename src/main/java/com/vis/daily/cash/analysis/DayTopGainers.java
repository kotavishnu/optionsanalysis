package com.vis.daily.cash.analysis;

import java.io.BufferedInputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vis.daily.cash.domain.Equity;
import com.vis.daily.cash.domain.ThreeDayPND;

public class DayTopGainers {
	Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private static String NIFTY_BHAVA_URL = "https://archives.nseindia.com/products/content/sec_bhavdata_full_%s.csv";
	private static String[] niftyArr={"ACC","ADANIENT","ADANIGREEN","ADANIPORTS","ADANITRANS","AMBUJACEM","APOLLOHOSP",
	                                  "ASIANPAINT","DMART","AXISBANK","BAJAJ-AUTO","BAJFINANCE","BAJAJFINSV","BAJAJHLDNG",
	                                  "BANDHANBNK","BANKBARODA","BERGEPAINT","BPCL","BHARTIARTL","BIOCON","BOSCHLTD","BRITANNIA","CHOLAFIN","CIPLA","COALINDIA","COLPAL","DLF","DABUR","DIVISLAB","DRREDDY","EICHERMOT","NYKAA","GAIL","GLAND","GODREJCP","GRASIM","HCLTECH","HDFCAMC","HDFCBANK","HDFCLIFE","HAVELLS","HEROMOTOCO","HINDALCO","HINDUNILVR","HDFC","ICICIBANK","ICICIGI","ICICIPRULI","ITC","IOC","INDUSTOWER","INDUSINDBK","NAUKRI","INFY","INDIGO","JSWSTEEL","JUBLFOOD","KOTAKBANK","LTI","LT","LUPIN","M&M","MARICO","MARUTI","MINDTREE","MUTHOOTFIN","NMDC","NTPC","NESTLEIND","ONGC","PAYTM","PIIND","PIDILITIND","PEL","POWERGRID","PGHH","PNB","RELIANCE","SBICARD","SBILIFE","SRF","SHREECEM","SIEMENS","SBIN","SAIL","SUNPHARMA","TCS","TATACONSUM","TATAMOTORS","TATASTEEL","TECHM","TITAN","TORNTPHARM","UPL","ULTRACEMCO","MCDOWELL-N","VEDL","WIPRO","ZOMATO","ZYDUSLIFE"};

	private static List<String> nifty200;
	public  Set<Equity> nifty200Equity;
	public DayTopGainers() {
		nifty200=Arrays.asList(niftyArr);
	}
	
	public static void main(String[] args) {
		DayTopGainers dtg=new DayTopGainers();
		Set<Equity> nifty200Equity1 = dtg.getNIFTY200EquityDayBhavcopy(String.format(NIFTY_BHAVA_URL,"29062022"));
		Set<Equity> nifty200Equity2 = dtg.getNIFTY200EquityDayBhavcopy(String.format(NIFTY_BHAVA_URL,"30062022"));
		Set<Equity> nifty200Equity3 = dtg.getNIFTY200EquityDayBhavcopy(String.format(NIFTY_BHAVA_URL,"01072022"));
		Comparator<Equity> c = (s1, s2) -> s2.getChangePercentage().compareTo(s1.getChangePercentage());
	
		ThreeDayPND pnd=new ThreeDayPND(nifty200Equity1, nifty200Equity2, nifty200Equity3);
		Map<String,Double> threeDayPnd=pnd.getThreeDayPnD();
		
		Map<String,Double> topTen =
				threeDayPnd.entrySet().stream()
			       .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
			       .limit(10)
			       .collect(Collectors.toMap(
			          Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap<String,Double>::new));
		System.out.println(topTen);
	}

	
	public String handleRequest(Map<String, Object> input, Context context) {
		String response = "200 OK";
		// log execution details
		System.out.println("ENVIRONMENT VARIABLES: X0016 " + gson.toJson(System.getenv()));
		System.out.println("CONTEXT: " + gson.toJson(context));
		System.out.println("Running lambda handler input "+input);
		String day1=(String)input.get("day1");
		String day2=(String)input.get("day2");
		String day3=(String)input.get("day3");
		DayTopGainers dtg=new DayTopGainers();
		Set<Equity> nifty200Equity1 = dtg.getNIFTY200EquityDayBhavcopy(String.format(NIFTY_BHAVA_URL,day1));
		Set<Equity> nifty200Equity2 = dtg.getNIFTY200EquityDayBhavcopy(String.format(NIFTY_BHAVA_URL,day2));
		Set<Equity> nifty200Equity3 = dtg.getNIFTY200EquityDayBhavcopy(String.format(NIFTY_BHAVA_URL,day3));
		Comparator<Equity> c = (s1, s2) -> s2.getChangePercentage().compareTo(s1.getChangePercentage());
		ThreeDayPND pnd=new ThreeDayPND(nifty200Equity1, nifty200Equity2, nifty200Equity3);
		Map<String,Double> threeDayPnd=pnd.getThreeDayPnD();
		Map<String,Double> topTen =
				threeDayPnd.entrySet().stream()
			       .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
			       .limit(10)
			       .collect(Collectors.toMap(
			          Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap<String,Double>::new));
		System.out.println(topTen);
		
		return topTen.toString();
	}
	private  Set<Equity> getNIFTY200EquityDayBhavcopy(String url) {
		String content = getNIFTY200Data(url);
		nifty200Equity=convertStringToNIFTY200(content);
		System.out.println("niftyEquities is "+nifty200Equity);
		System.out.println("niftyEquities length is "+nifty200Equity.size());
		return nifty200Equity;
	}

	private static Set<Equity> convertStringToNIFTY200(String content) {
		String equityLines[]=content.split("\n");
		Set<Equity> nifty200Equity=new TreeSet<Equity>();
		int i=0;
		for(String equityLine:equityLines) {
		//System.out.println("Line is "+equityLine);
			if(i==0) {
				i++;
				continue;
			}
			try {
				String field[]=equityLine.split(",");
				//System.out.println("Fields length is "+field.length);
				String symbol=field[0].trim();
				String series=field[1].trim();
				//System.out.println("symbol "+symbol);
				//System.out.println("series "+series);
				//System.out.println("nifty200 "+nifty200);
				
				if(series.equals("EQ") && nifty200.contains(symbol)) {//FILERTIN ONLY EQUITY MARKET
					Equity eq = new Equity(symbol, series, field[2], Double.parseDouble(field[3].trim()),
							Double.parseDouble(field[4].trim()), Double.parseDouble(field[5].trim()), Double.parseDouble(field[6].trim()),
							Double.parseDouble(field[7].trim()), Double.parseDouble(field[8].trim()), Integer.parseInt(field[10].trim()),
							Double.parseDouble(field[11].trim()), Integer.parseInt(field[13].trim()), Double.parseDouble(field[14].trim()));
					nifty200Equity.add(eq);
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
				System.out.println("Ignoring the line: Error while parsing the line"+equityLine);
			}
		}
		return nifty200Equity;
	}

	private static String getNIFTY200Data(String url) {
		StringBuilder sb=new StringBuilder();

		try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream())) {
			byte dataBuffer[] = new byte[1024];
			int bytesRead;
			while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
				sb.append(new String(dataBuffer, 0, bytesRead));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

}
