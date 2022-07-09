package com.vis.optiontools.analytics;

import java.util.Map;
import java.util.Set;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vis.optiontools.analytics.domain.OptionChain;
import com.vis.optiontools.analytics.domain.Strike;
import com.vis.optiontools.analytics.infra.GetDataServices;
import com.vis.optiontools.analytics.infra.GetDataServicesImpl;

// Handler value: example.Handler
public class ExpiryHandler {
	Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	

	public static void main(String[] args) {
		System.out.println("Running lambda handler");
		String bucketName = "daytrading.stock";
		String key="23_6_2022/Deriv_BANKNIFTY_9_25.json";
		GetDataServices ds=new GetDataServicesImpl();
		String content=ds.getS3Data(bucketName,key);
		System.out.println("Content is "+content);
		ReadOptionChains roc=new ReadOptionChains();
		Map<String, OptionChain> chains= roc.getOptionChain(content);
		OptionChain oc=chains.get("23-Jun-2022");
		//System.out.println(oc);
		OptionChain filteredOC=oc.getFilteredOptionChain(10);
		System.out.println(filteredOC);
		System.out.println("____________________\nUnderlying index"+filteredOC.getUnderlyingIndex());
		Set<Strike> strikes=filteredOC.getProbableStrikes();
		System.out.println("\nPrinting Probable Strikes\n________________________");
		Strike.print(strikes);
		Set<Strike> nearestStrikes=filteredOC.getNearestStrikes();
		System.out.println("\nPrinting Nearest Strikes\n________________________");
		Strike.print(nearestStrikes);
		
	}
	
	public String handleRequest(Map<String, Object> input, Context context) {
		String response = "200 OK";
		// log execution details
		System.out.println("ENVIRONMENT VARIABLES: X0016 " + gson.toJson(System.getenv()));
		System.out.println("CONTEXT: " + gson.toJson(context));
		System.out.println("Running lambda handler input "+input);
		
		//String url = "https://www.nseindia.com/api/quote-derivative?symbol=BANKNIFTY";
		GetDataServices ds=new GetDataServicesImpl();
		//String bucketName = "daytrading.stock";
		String bucketName=(String)input.get("bucketName");
		System.out.println("bucketName "+bucketName);
		//String key="23_6_2022/Deriv_BANKNIFTY_9_25.json";
		String key=(String)input.get("key");
		System.out.println("key "+key);
		String content=ds.getS3Data(bucketName,key);
		//System.out.println("Content is "+content);
		ReadOptionChains roc=new ReadOptionChains();
		Map<String, OptionChain> chains= roc.getOptionChain(content);
		String expiry=(String)input.get("expiry");
		OptionChain oc=chains.get(expiry);
		//System.out.println(oc);
		OptionChain filteredOC=oc.getFilteredOptionChain(10);
		System.out.println(filteredOC);
		System.out.println("____________________\nUnderlying index"+filteredOC.getUnderlyingIndex());
		Set<Strike> strikes=filteredOC.getProbableStrikes();
		System.out.println("\nPrinting Probable Strikes\n________________________");
		Strike.print(strikes);
		Set<Strike> nearestStrikes=filteredOC.getNearestStrikes();
		System.out.println("\nPrinting Nearest Strikes\n________________________");
		Strike.print(nearestStrikes);
		return response;
	}
	
	
	
}