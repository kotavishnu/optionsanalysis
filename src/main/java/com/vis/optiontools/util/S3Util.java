package com.vis.optiontools.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.vis.optiontools.analytics.ReadOptionChains;
import com.vis.optiontools.analytics.domain.OCTimestamps;
import com.vis.optiontools.analytics.domain.OptionChain;

public class S3Util  {
	private static AmazonS3 s3 = new AmazonS3Client();
	static String bucketName="daytrading.stock";


	static String folderName="7_7_2022/";
	public static void main(String[] args) {
		String folder="7_7_2022/";
		String bucketName = "daytrading.stock";
		//S3Util.getOCTimestamps(bucketName, folder);
		//String s="{\"info\": \"\", \"filter\": \"\", \"underlyingValue\": 34948.5, \"vfq\": 1201, \"fut_timestamp\": \"07-Jul-2022 15:29:52\", \"opt_timestamp\": \"07-Jul-2022 15:29:54\", \"stocks\": ";
		//System.out.println("get time stapmp "+S3Util.getTimestamp(s));
	}

	public static String getBucketName() {
		return bucketName;
	}

	public static void setBucketName(String bucketName) {
		S3Util.bucketName = bucketName;
	}

	public static String getFolderName() {
		return folderName;
	}

	public static void setFolderName(String folderName) {
		S3Util.folderName = folderName;
	}
	
	public static Set<OCTimestamps> getOCTimestamps() {
		Set<OCTimestamps> fileTimestamps = new TreeSet<OCTimestamps>();
		System.out.println("Listing objects");
		try {
			ObjectListing objectListing = s3
					.listObjects(new ListObjectsRequest().withBucketName(bucketName).withPrefix(folderName));
			int i = 0;
			for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
				String key = objectSummary.getKey();
				System.out.println(" - " + key + "  " + "(size = " + objectSummary.getSize() + ")");
				if (key.contains("Deriv_BANKNIFTY") && key.endsWith("json")) {
					String content = getS3ObjectContent(bucketName, key);
					// System.out.println("Content "+content);
					String sTimestamp = S3Util.getTimestamp(content);
					fileTimestamps.add(new OCTimestamps(key, sTimestamp));
					i++;
					//if (i > 20)	break;
				}
			}
			System.out.println("List of timestamps " + fileTimestamps);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return fileTimestamps;
	}

	public static String getS3ObjectContent(String bucketName, String key) throws IOException {
		S3Object object = s3.getObject(new GetObjectRequest(bucketName, key));
		String content = displayTextInputStream(object.getObjectContent());
		return content;
	}
	
	private static Date convertStringToDate(String sTimestamp) {
		//sTimestamp ="07-Jul-2022 15:29:54"
		try {
			return new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").parse(sTimestamp);
		} catch (ParseException e) {
			e.printStackTrace();
		}  
		return null;
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
	
	public static String getTimestamp(String s) {
		int ind=s.indexOf("opt_timestamp");
		return s.substring(ind+17,ind+37);
	}

	public static OptionChain getOptionChain(String fileName,String expiryDate) throws Exception{
		try {
			String content = S3Util.getS3ObjectContent(bucketName, fileName);
			//System.out.println("String content "+content);
			ReadOptionChains roc=new ReadOptionChains();
			Map<String, OptionChain> chains= roc.getOptionChain(content);
			OptionChain oc=chains.get(expiryDate);
			//System.out.println(oc);
			OptionChain filteredOC=oc.getFilteredOptionChain(12);
			//System.out.println(filteredOC);
			return filteredOC;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
