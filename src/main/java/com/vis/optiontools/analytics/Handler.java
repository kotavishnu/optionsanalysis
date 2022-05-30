package com.vis.optiontools.analytics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vis.optiontools.analytics.domain.DayFutures;
import com.vis.optiontools.analytics.domain.Future;
import com.vis.optiontools.analytics.domain.MonthlyFutures;

// Handler value: example.Handler
public class Handler {
	Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public static boolean evaluateRuleOpen(DayFutures dayFutures) {
		return dayFutures.isOpenVolumeGreaterThan(300000);
	}
	
	
	public String handleRequest(Map<String, Object> input, Context context) {
		String response = "200 OK";
		// log execution details
		System.out.println("ENVIRONMENT VARIABLES: X0002" + gson.toJson(System.getenv()));
		System.out.println("CONTEXT: " + gson.toJson(context));
		System.out.println("Running lambda handler");
		
		DayFutures dayFutures=getDayFutures();
		
		return response;
	}
	
	
private static void sendVolumeNotification(DayFutures dayFutures) {
		
		if(NotifyFutureVolume.evaluateRuleOpen(dayFutures)) {
			//sentEmail("BANKNIFTY Open Volume greater than 300");
		}
	}
	private static DayFutures getDayFutures() {
		AWSCredentials credentials = null;
		DayFutures dayFutures = new DayFutures(new Date());
		/*
		 * try { credentials = new
		 * ProfileCredentialsProvider("default").getCredentials(); } catch (Exception e)
		 * { throw new
		 * AmazonClientException("Cannot load the credentials from the credential profiles file. "
		 * + "Please make sure that your credentials file is at the correct " +
		 * "location (C:\\Users\\kotav\\.aws\\credentials), and is in valid format.",
		 * e); }
		 * 
		 * AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new
		 * AWSStaticCredentialsProvider(credentials)) .withRegion("ap-south-1").build();
		 */
		AmazonS3 s3=new AmazonS3Client();
		String bucketName = "daytrading.stock";
		//String key = "28_5_2022/BN_FUT_28_5_2022_8_8.json";

		try {
			System.out.println("Listing objects");
			ObjectListing objectListing = s3
					.listObjects(new ListObjectsRequest().withBucketName(bucketName).withPrefix("28_5_2022/"));
			
			for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
				String key = objectSummary.getKey();
				System.out.println(" - " + key + "  " + "(size = " + objectSummary.getSize() + ")");
				if (key.endsWith("json")) {
					S3Object object = s3.getObject(new GetObjectRequest(bucketName, key));
					System.out.println("Content-Type: " + object.getObjectMetadata().getContentType());
					String content = displayTextInputStream(object.getObjectContent());
					System.out.println("content is " + content);
					MonthlyFutures monFutures = getMonthlyFuturesFromJson(content);
					dayFutures.addFuture(monFutures);
				}
			}
			System.out.println("Futures of the Day "+dayFutures);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dayFutures;
	}

	private static String displayTextInputStream(InputStream input) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		StringBuilder sb=new StringBuilder();
		while (true) {
			String line = reader.readLine();
			if (line == null)
				break;
			sb.append(line);
			//System.out.println("    " + line);
		}
		System.out.println();
		return sb.toString();
	}
	
	private static MonthlyFutures getMonthlyFuturesFromJson(String json) {
		MonthlyFutures futures=null;
		try {
			Object obj = new JSONParser().parse(json);

			JSONObject jo = (JSONObject) obj;
			String futTimestamp=(String)jo.get("fut_timestamp");
			
			JSONArray ja = (JSONArray) jo.get("stocks");
			futures=new MonthlyFutures(futTimestamp);
			for(Object o:ja) {
				JSONObject o1 = (JSONObject) o;
				JSONObject metadata=(JSONObject)o1.get("metadata");
				
				if("Index Futures".equals(metadata.get("instrumentType"))) {
					JSONObject marketDeptOrderBook=(JSONObject)o1.get("marketDeptOrderBook");
					JSONObject tradeInfo=(JSONObject)marketDeptOrderBook.get("tradeInfo");
					
					double closePrice=getDoubleValue(metadata.get("closePrice"));
					double lastPrice=getDoubleValue(metadata.get("lastPrice"));
					double change=getDoubleValue(metadata.get("change"));
					double underlyingValue=getDoubleValue(o1.get("underlyingValue"));
					double pChange=getDoubleValue(metadata.get("pChange"));
					double pchangeinOpenInterest=getDoubleValue(tradeInfo.get("pchangeinOpenInterest"));
					Future f=new Future((String)metadata.get("expiryDate"), closePrice, lastPrice,change,pChange,
							(long)metadata.get("numberOfContractsTraded"), underlyingValue, (long)tradeInfo.get("tradedVolume"), (long)tradeInfo.get("openInterest"),
							(long)tradeInfo.get("changeinOpenInterest"), pchangeinOpenInterest,futTimestamp);
					futures.addMonthlyFuture(f);
				}
			}
			System.out.println("Monthly Futures in order are "+futures);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return futures;
	}

	private static double getDoubleValue(Object object) {
		double v=0.0;
		System.out.println("Object value is "+object);
		if(object.getClass().equals("java.lang.Double")) {
			v=(double)object;
		}
		if(object.getClass().equals("java.lang.Long")) {
			Long l=(Long)object;
			v=l.doubleValue();
		}
		return 0;
	}
}