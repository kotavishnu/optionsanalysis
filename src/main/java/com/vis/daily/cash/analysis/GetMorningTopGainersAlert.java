package com.vis.daily.cash.analysis;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.vis.optiontools.util.HttpClient;

public class GetMorningTopGainersAlert {
	private static String NIFTY_BHAVA_URL = "https://www.nseindia.com/api/equity-stockIndices?index=NIFTY%20200";

	public static void main(String[] args) {
		try {
			String content=HttpClient.getUrlData(NIFTY_BHAVA_URL);
			System.out.println("Data is "+content);
			getNIFTYTopGainers(content);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void getNIFTYTopGainers(String content) throws ParseException {
		Object obj = new JSONParser().parse(content);

		JSONObject jo = (JSONObject) obj;
		String timestamp=(String)jo.get("timestamp");		
		System.out.println("timestamp"+timestamp);
		
	}

}
