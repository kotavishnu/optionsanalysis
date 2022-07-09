package com.vis.optiontools.analytics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.google.gson.Gson;
import com.vis.optiontools.analytics.domain.DayFutures;
import com.vis.optiontools.analytics.domain.Future;
import com.vis.optiontools.analytics.domain.MonthlyFutures;
import com.vis.optiontools.util.AmazonSESSample;

public class NotifyFutureVolume {
	private static Set<String> timestamps=new TreeSet<String>();
	private static AmazonS3 s3=new AmazonS3Client();
	private static Gson gson;
	private static String bucketName;
	private static String folder;
	public NotifyFutureVolume(Gson gson,String bucketName,String folder) {
		this.gson=gson;
		this.bucketName=bucketName;
		this.folder=folder;
	}

	public static boolean evaluateRuleOpen(DayFutures dayFutures) {
		return dayFutures.isOpenVolumeGreaterThan(300000);
	}
	
	public static boolean evaluateRuleLastVolGT100(DayFutures dayFutures) {
		return dayFutures.isLast5MinsVolumeGreaterThan(100000);
	}
	public static void main(String[] args) {
		NotifyFutureVolume futures=new NotifyFutureVolume(gson,bucketName,folder);
		DayFutures dayFutures=futures.getDayFutures();
		dayFutures.validate();
		sendVolumeNotification(dayFutures);
		//saveDayFutures(dayFutures);
	}

	

	private static void sendVolumeNotification(DayFutures dayFutures) {
		if(NotifyFutureVolume.evaluateRuleOpen(dayFutures)) {
			AmazonSESSample.sendEmail("BANKNIFTY Open Volume greater than 300"," BANKNIFTY","vishnukoptions@yahoo.com");
		}else {
			System.out.println("Open volume is less than 300000");
		}
	}
	public  DayFutures getDayFutures() {
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
		
		
		//String key = "28_5_2022/BN_FUT_28_5_2022_8_8.json";

		try {
			System.out.println("Listing objects");
			ObjectListing objectListing = s3
					.listObjects(new ListObjectsRequest().withBucketName(bucketName).withPrefix(folder));
			int i=0;
			for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
				String key = objectSummary.getKey();
				System.out.println(" - " + key + "  " + "(size = " + objectSummary.getSize() + ")");
				if (key.contains("Deriv_BANKNIFTY") && key.endsWith("json")) {
					S3Object object = s3.getObject(new GetObjectRequest(bucketName, key));
					//System.out.println("Content-Type: " + object.getObjectMetadata().getContentType());
					String content = displayTextInputStream(object.getObjectContent());
					//System.out.println("content is " + content);
					try {
						MonthlyFutures monFutures = getMonthlyFuturesFromJson(content,s3,bucketName,key);
						if(monFutures!= null) {
							dayFutures.addFuture(monFutures);
							moveS3ObjectToFolder(key,content);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					i++;
					if(dayFutures.size()>5)break;
				}
				
			}
			System.out.println("Futures of the Day "+dayFutures);
		} catch (IOException e) {
			System.out.println("Error in reading folder "+bucketName+" folder name "+folder);
			e.printStackTrace();
			return null;
		}
		return dayFutures;
	}

	private static void moveS3ObjectToFolder(String key, String content) {
		try {
			String arr[]=key.split("/");
			String newFile=arr[0]+"/processed/"+arr[1];
			s3.putObject(bucketName,newFile,content);
			s3.deleteObject(bucketName, key);
		} catch (Exception e) {
			System.out.println("error whilem= moving the file "+key);
			e.printStackTrace();
		} 
		
	}
	
	public void saveDayFutures(DayFutures dayFutures) {
		String strDayFutures= gson.toJson(dayFutures);
		String newFile=folder+"dayfutures.json";
		s3.putObject(bucketName,newFile,strDayFutures);
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
	
	private static MonthlyFutures getMonthlyFuturesFromJson(String json, AmazonS3 s3, String bucketName, String key) {
		MonthlyFutures futures=null;
		try {
			Object obj = new JSONParser().parse(json);

			JSONObject jo = (JSONObject) obj;
			String futTimestamp=(String)jo.get("fut_timestamp");
			if(timestamps.contains(futTimestamp)) {
				System.out.println("timestamps already contains "+futTimestamp);
				s3.deleteObject(bucketName, key);
				System.out.println("Deleted "+key+" already exists");
				return null;
			}
			timestamps.add(futTimestamp);
			System.out.println("fut_timestamp "+futTimestamp);
			Date timestamp=null;
		
				try {
					timestamp=new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").parse(futTimestamp);
				} catch (java.text.ParseException e) {
					e.printStackTrace();
				}
			 
			if(timestamp==null) {
				System.out.println("Cannot parse future time stamp "+futTimestamp);
				System.out.println("Could be Opening quote, so can skip");
				return null;
			}
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
		//System.out.println("Object value is "+object);
		if(object.getClass().equals("java.lang.Double")) {
			v=(double)object;
		}
		if(object.getClass().equals("java.lang.Long")) {
			Long l=(Long)object;
			v=l.doubleValue();
		}
		return v;
	}
}
