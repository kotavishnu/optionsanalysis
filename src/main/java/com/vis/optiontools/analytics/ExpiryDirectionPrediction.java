package com.vis.optiontools.analytics;

import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vis.optiontools.analytics.domain.services.ExpiryServices;
import com.vis.optiontools.analytics.infra.ExpiryServicesImpl;
import com.vis.optiontools.util.S3Util;

public class ExpiryDirectionPrediction {
	Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	public String handleRequest(Map<String, Object> input, Context context) {
		String response = "200 OK";
		System.out.println("ENVIRONMENT VARIABLES: X009 " + gson.toJson(System.getenv()));
		System.out.println("CONTEXT: " + gson.toJson(context));
		System.out.println("Running lambda handler input "+input);
		String expiry="07-Jul-2022";
		String bucketName=(String)input.get("bucketName");
		S3Util.setBucketName(bucketName);
		String folderName=(String)input.get("folderName");
		S3Util.setFolderName(folderName);
		expiry=(String)input.get("expiry");
		ExpiryServices es=new ExpiryServicesImpl(expiry);
		es.predictDirection();
		return response;
	}
}
