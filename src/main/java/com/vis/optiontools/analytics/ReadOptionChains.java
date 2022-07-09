package com.vis.optiontools.analytics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
import com.vis.optiontools.analytics.domain.OptionChain;
import com.vis.optiontools.analytics.domain.Strike;
import com.vis.optiontools.util.S3Util;

public class ReadOptionChains {
	public static void main(String[] args) {
		
		
		try {
			ReadOptionChains roc=new ReadOptionChains();
			String bucketName="daytrading.stock";
			String folderName="7_7_2022/";
			//String content=Util.readString("f:/tmp/t/Deriv_BANKNIFTY_9_25.json", StandardCharsets.UTF_8);
			String content=S3Util.getS3ObjectContent(bucketName, folderName+"Deriv_BANKNIFTY_9_0.json");
			System.out.println("String content "+content);
			Map<String, OptionChain> chains= roc.getOptionChain(content);
			OptionChain oc=chains.get("07-Jul-2022");
			//System.out.println(oc);
			OptionChain filteredOC=oc.getFilteredOptionChain(10);
			System.out.println(filteredOC);
			Set<Strike> strikes=filteredOC.getProbableStrikes();
			System.out.println("Get Probalble strikes"+strikes);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//readJSONFiles(bucketName,folderName,prefix,ext);
		
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
					//getOptionChain(content);
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
			
		}
	}

	public  Map<String, OptionChain> getOptionChain(String content) {
		 Map<String, OptionChain> optionChains=new HashMap<String, OptionChain>();
		try {
			Object obj = new JSONParser().parse(content);

			JSONObject jo = (JSONObject) obj;
			String time = (String)jo.get("opt_timestamp");
			System.out.println("opt_timestamp" +time);
			Double underlyingValue = getDoubleValue(jo.get("underlyingValue"));
			System.out.println("underlyingValue" +underlyingValue);
			JSONArray stocks = (JSONArray) jo.get("stocks"); 
			for(Object row:stocks) {
				JSONObject o1 = (JSONObject) row;
				JSONObject metadata=(JSONObject)o1.get("metadata");
				String instrumentType=(String)metadata.get("instrumentType");
				if(!"Index Options".equals(instrumentType))continue;
				String expiryDate=(String)metadata.get("expiryDate");
				OptionChain optionChain=optionChains.get(expiryDate);
				if(optionChain==null) {
					optionChain=new OptionChain(underlyingValue,time,"");
					optionChains.put(expiryDate, optionChain);
				}
				optionChain.setExpiryDate(expiryDate);
				String optionType=(String)metadata.get("optionType");
				String identifier=(String)metadata.get("identifier");
				long strikePrice=(Long)metadata.get("strikePrice");
				if(checkStrikeMoreThan(underlyingValue,strikePrice,optionType,5)) continue;
				double openPrice=getDoubleValue(metadata.get("openPrice"));
				//System.out.println("openPrice "+openPrice);
				double highPrice=getDoubleValue(metadata.get("highPrice"));
				//System.out.println("highPrice "+highPrice);
				double lowPrice=getDoubleValue(metadata.get("lowPrice"));
				double lastPrice=getDoubleValue(metadata.get("lastPrice"));
				//System.out.println("openPrice "+openPrice);
				double closePrice=0.0,prevClose=0.0,change=0.0,pChange=0.0;
				double totalTurnover=0.0;
				
				
				long numberOfContractsTraded=0,tradedVolume=0;
				
				JSONObject marketDeptOrderBook=(JSONObject)o1.get("marketDeptOrderBook");
				JSONObject tradeInfo=(JSONObject)marketDeptOrderBook.get("tradeInfo");
				long openInterest=(Long)tradeInfo.get("openInterest");
				long changeinOpenInterest=(Long)tradeInfo.get("changeinOpenInterest");
				double pchangeinOpenInterest=getDoubleValue(tradeInfo.get("pchangeinOpenInterest"));
				Strike s=new Strike( optionType, identifier, strikePrice,  openPrice,  highPrice,
						 lowPrice,  closePrice,  prevClose, lastPrice,  change, pChange,
						 numberOfContractsTraded, totalTurnover, tradedVolume, openInterest,
						 changeinOpenInterest,  pchangeinOpenInterest);
				//System.out.println("Strie is "+s);
				if(optionType.equals("Put")) 
					optionChain.addStrikeToPuts(s);
				else if(optionType.equals("Call")) 
					optionChain.addStrikeToCalls(s);
			}
			 
			System.out.println("Option chain built is "+optionChains);
			return optionChains;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;		
	}

	private boolean checkStrikeMoreThan(Double underlyingValue, long strikePrice, String optionType, int i) {
		int numStrikes=i*100;
		if(optionType.equals("Put")) {
			return (strikePrice <= (underlyingValue - (numStrikes))) && (strikePrice>= underlyingValue+2*100);
		}
		if(optionType.equals("Call")) {
			return (strikePrice >= (underlyingValue + (numStrikes))) && (strikePrice<= underlyingValue-2*100);
		}
		return false;
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

		/*
		 * optionChain.addStrike(strikePrice, identifier, openInterest,
		 * changeinOpenInterest, pchangeinOpenInterest, 0, lastPrice, change, pChange,
		 * optionType, totalTradedVolume);
		 */
	}

	private static double getDoubleValue(Object object) {
		double v=0.0;
		//System.out.println("Object value is "+object+" class is "+object.getClass());
		if(object.getClass().getSimpleName().equals("Double") && object!=null) {
			v=(Double)object;
			//System.out.println("Double value is "+v);
		}
		if(object.getClass().getSimpleName().equals("Long") && object!=null) {
			Long l=(Long)object;
			//System.out.println("Long value is "+l);
			v=l.doubleValue();
		}
		//System.out.println(";returning value is "+v);
		return v;
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
