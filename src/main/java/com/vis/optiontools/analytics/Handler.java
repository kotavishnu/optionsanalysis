package com.vis.optiontools.analytics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.s3.AmazonS3;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vis.optiontools.analytics.domain.DayFutures;
import com.vis.optiontools.analytics.domain.Future;
import com.vis.optiontools.analytics.domain.MonthlyFutures;
import com.vis.optiontools.util.AmazonSESSample;

// Handler value: example.Handler
public class Handler {
	Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private static Set<String> timestamps=new TreeSet<String>();
	String bucketName = "daytrading.stock";
	
	
	public static boolean evaluateRuleOpen(DayFutures dayFutures) {
		return dayFutures.isOpenVolumeGreaterThan(300000);
	}
	public static void main(String[] args) {
		System.out.println("Running lambda handler");
		
		//DayFutures dayFutures=getDayFutures();
		
	}
	
	public String handleRequest(Map<String, Object> input, Context context) {
		String response = "200 OK";
		// log execution details
		System.out.println("ENVIRONMENT VARIABLES: X0008" + gson.toJson(System.getenv()));
		System.out.println("CONTEXT: " + gson.toJson(context));
		System.out.println("Running lambda handler");
		String folder="3_6_2022/";
		final ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
		System.out.println("Print date "+now);
		System.out.println("Print hour  "+now.getHour()+" mins "+now.getMinute());
		folder=getFolder(now);
		System.out.println("Folder is "+folder);
		
		
		if(isMarketClosed(now)) {
			System.out.println("Market closed, exiting");
			response="200 OK,Market Closed";
			return response;
		}
		NotifyFutureVolume futures=new NotifyFutureVolume(gson,bucketName,folder);
		DayFutures dayFutures=futures.getDayFutures();
		dayFutures.validate();
		sendVolumeNotification(dayFutures);
		futures.saveDayFutures(dayFutures);
		return response;
	}
	
	
	private String getFolder(ZonedDateTime now) {
		return now.getDayOfMonth()+"_"+now.getMonthValue()+"_"+now.getYear()%2000;
	}
	private boolean isMarketClosed(ZonedDateTime now) {
		return (now.getHour() > 16 || now.getHour() < 9 || (now.getHour() > 15 && now.getMinute() > 31) )
				|| (now.getHour() > 9 && now.getMinute() < 15) ? true : false;
	}

	private static void sendVolumeNotification(DayFutures dayFutures) {
		String notifyMessage = "";
		if (NotifyFutureVolume.evaluateRuleOpen(dayFutures)) {
			notifyMessage = "BANKNIFTY Open Volume greater than 300k";
		}
		if (NotifyFutureVolume.evaluateRuleLastVolGT100(dayFutures)) {
			notifyMessage += "\n <br> BANKNIFTY Recent Volume greater than 100k";
		}

		if (notifyMessage.length() > 0)
			AmazonSESSample.sendEmail(notifyMessage, notifyMessage, "vishnukoptions@yahoo.com");
		else {
			System.out.println("No notifications triggered");
		}
	}

	private static String displayTextInputStream(InputStream input) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		StringBuilder sb = new StringBuilder();
		while (true) {
			String line = reader.readLine();
			if (line == null)
				break;
			sb.append(line);
			// System.out.println(" " + line);
		}
		return sb.toString();
	}

	private static MonthlyFutures getMonthlyFuturesFromJson(String json, AmazonS3 s3, String bucketName, String key) {
		MonthlyFutures futures = null;
		try {
			Object obj = new JSONParser().parse(json);

			JSONObject jo = (JSONObject) obj;
			String futTimestamp = (String) jo.get("fut_timestamp");
			if (timestamps.contains(futTimestamp)) {
				System.out.println("timestamps already contains " + futTimestamp);
				s3.deleteObject(bucketName, key);
				System.out.println("Deleted " + key + " already exists");
				return null;
			}
			timestamps.add(futTimestamp);
			System.out.println("fut_timestamp " + futTimestamp);
			Date timestamp = null;

			try {
				timestamp = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").parse(futTimestamp);
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}

			if (timestamp == null) {
				System.out.println("Cannot parse future time stamp " + futTimestamp);
				System.out.println("Could be Opening quote, so can skip");
				return null;
			}
			JSONArray ja = (JSONArray) jo.get("stocks");
			futures = new MonthlyFutures(futTimestamp);
			for (Object o : ja) {
				JSONObject o1 = (JSONObject) o;
				JSONObject metadata = (JSONObject) o1.get("metadata");

				if ("Index Futures".equals(metadata.get("instrumentType"))) {
					JSONObject marketDeptOrderBook = (JSONObject) o1.get("marketDeptOrderBook");
					JSONObject tradeInfo = (JSONObject) marketDeptOrderBook.get("tradeInfo");

					double closePrice = getDoubleValue(metadata.get("closePrice"));
					double lastPrice = getDoubleValue(metadata.get("lastPrice"));
					double change = getDoubleValue(metadata.get("change"));
					double underlyingValue = getDoubleValue(o1.get("underlyingValue"));
					double pChange = getDoubleValue(metadata.get("pChange"));
					double pchangeinOpenInterest = getDoubleValue(tradeInfo.get("pchangeinOpenInterest"));
					Future f = new Future((String) metadata.get("expiryDate"), closePrice, lastPrice, change, pChange,
							(long) metadata.get("numberOfContractsTraded"), underlyingValue,
							(long) tradeInfo.get("tradedVolume"), (long) tradeInfo.get("openInterest"),
							(long) tradeInfo.get("changeinOpenInterest"), pchangeinOpenInterest, futTimestamp);
					futures.addMonthlyFuture(f);
				}
			}
			System.out.println("Monthly Futures in order are " + futures);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return futures;
	}

	private static double getDoubleValue(Object object) {
		double v = 0.0;
		// System.out.println("Object value is "+object);
		if (object.getClass().equals("java.lang.Double")) {
			v = (double) object;
		}
		if (object.getClass().equals("java.lang.Long")) {
			Long l = (Long) object;
			v = l.doubleValue();
		}
		return 0;
	}
}