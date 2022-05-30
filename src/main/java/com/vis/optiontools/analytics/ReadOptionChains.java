package com.vis.optiontools.analytics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.vis.optiontools.analytics.domain.MonthlyFutures;
import com.vis.optiontools.analytics.domain.OptionChain;
import com.vis.optiontools.analytics.domain.OptionChain.Strike;

public class ReadOptionChains {

	public static void main(String[] args) {
		
		String ext=".json";
		String prefix="BN_";
		String bucketName="daytrading.stock";
		String folderName="27_5_2022/";
		readJSONFiles(bucketName,folderName,prefix,ext);
	}

	private static void readJSONFiles(String bucketName, String folderName, String prefix, String ext) {
		AmazonS3 s3=getS3Client();
		
		readFileFromS3(s3,bucketName,folderName,  prefix, ext);
		
	}

	private static void readFileFromS3(AmazonS3 s3,String bucketName, String folderName, String prefix, String ext) {
		System.out.println("Listing objects");
		ObjectListing objectListing = s3
				.listObjects(new ListObjectsRequest().withBucketName(bucketName).withPrefix(folderName));
		
		for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
			String key = objectSummary.getKey();
			//System.out.println("JSON FILE - " + key + "  " + "(size = " + objectSummary.getSize() + ")");
			if (key.contains(prefix) && key.endsWith(ext)) {
				System.out.println("JSON FILE - " + key + "  " + "(size = " + objectSummary.getSize() + ")");
				S3Object object = s3.getObject(new GetObjectRequest(bucketName, key));
				System.out.println("Content-Type: " + object.getObjectMetadata().getContentType());
				try {
					String content = displayTextInputStream(object.getObjectContent());
					System.out.println("content is " + content);
					getOptionChain(content);
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
			
		}
	}

	private static void getOptionChain(String content) {
		try {
			Object obj = new JSONParser().parse(content);

			JSONObject jo = (JSONObject) obj;
			JSONObject recs = (JSONObject)jo.get("records");
			JSONObject filtered = (JSONObject)jo.get("filtered");
			JSONArray data = (JSONArray) filtered.get("data");
			JSONObject firstRow=(JSONObject)data.get(0);
			String expiryDate=(String)firstRow.get("expiryDate");
			System.out.println("expirty is "+firstRow.get("expiryDate"));
			String time = (String)recs.get("timestamp");
			Long underlyingValue = (Long)recs.get("underlyingValue");
			System.out.println("timestamp "+time);
			OptionChain optionChain=new OptionChain(underlyingValue,time,expiryDate);

			for(Object o:data) {
				JSONObject row = (JSONObject) o;
				Long strikePrice=(Long)row.get("strikePrice");
				if(strikePrice>underlyingValue+500) continue;//ignore above 11 strikes
				if(strikePrice<underlyingValue-500) continue;//ignore below 11 strikes
				
				JSONObject call = (JSONObject)row.get("CE");
				addStrikesToOptionChain(strikePrice,call,"CE",optionChain);
				
				JSONObject put = (JSONObject)row.get("PE");
				addStrikesToOptionChain(strikePrice,put,"PE",optionChain);
			}
			optionChain.buildRowStrikes();
			System.out.println("Option chain built is "+optionChain);
		}catch (Exception e) {
			e.printStackTrace();
		}
				
	}

	private static void addStrikesToOptionChain(long strikePrice, JSONObject call, String optionType,
			OptionChain optionChain) {
		String identifier = (String) call.get("identifier");
		long openInterest = (long) call.get("openInterest");
		long changeinOpenInterest = (long) call.get("changeinOpenInterest");
		double pchangeinOpenInterest = getDoubleValue(call.get("pchangeinOpenInterest"));
		double lastPrice = getDoubleValue(call.get("lastPrice"));
		double change = getDoubleValue(call.get("change"));
		double pChange = getDoubleValue(call.get("pChange"));
		long totalTradedVolume = (long) call.get("totalTradedVolume");

		optionChain.addStrike(strikePrice, identifier, openInterest, changeinOpenInterest, pchangeinOpenInterest, 0,
					lastPrice, change, pChange, optionType, totalTradedVolume);
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
	
	private static String displayTextInputStream(InputStream input) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		StringBuilder sb=new StringBuilder();
		while (true) {
			String line = reader.readLine();
			if (line == null)
				break;
			sb.append(line);
		}
		return sb.toString();
	}
	private static AmazonS3 getS3Client() {
		AmazonS3 s3 =null;
		try {
			AWSCredentials credentials = new ProfileCredentialsProvider("default").getCredentials();
			s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
					.withRegion("ap-south-1").build();
		} catch (Exception e) {
			throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
					+ "Please make sure that your credentials file is at the correct "
					+ "location (C:\\Users\\kotav\\.aws\\credentials), and is in valid format.", e);
		}

		return s3;
		 		
	}

}
