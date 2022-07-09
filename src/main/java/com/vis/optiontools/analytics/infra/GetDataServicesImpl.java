package com.vis.optiontools.analytics.infra;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.vis.optiontools.util.Util;

public class GetDataServicesImpl implements GetDataServices {

	@Override
	public String getURLData(String url) {
		Map<String,String> headers=new HashMap<String, String>();
		headers.put("authority", "www.nseindia.com");
		headers.put("scheme", "/api/quote-derivative?symbol=BANKNIFTY");
		headers.put("path", "https");
		headers.put("dnt", "1");
		headers.put("sec-ch-ua", "\".Not/A)Brand\";v=\"99\", \"Google Chrome\";v=\"103\", \"Chromium\";v=\"103\"");
		headers.put("upgrade-insecure-requests", "1");
		headers.put("sec-fetch-dest", "document");
		headers.put("accept", "accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
		
		headers.put("cookie", "_ga=GA1.1.13649374.1652708079; _ga_PJSKY6CFJH=GS1.1.1656182610.3.1.1656182611.0; RT=\"z=1&dm=nseindia.com&si=eb620fc1-7393-475b-a0eb-da4d66bd4930&ss=l4u5qlrn&sl=0&tt=0&bcn=%2F%2F684d0d43.akstat.io%2F&ul=2mjgg&hd=2mjkl\"; AKA_A2=A; ak_bmsc=AAA9D9875C0DA01AD98E8A924AF39AC9~000000000000000000000000000000~YAAQjHMsMdTfC4+BAQAAspl0rRA8/S3DzOuY+1bbvj6ec480TmI9x0CJ/ltVpQP8PaLk4rv1WDC9BQFONKRJqsAYgUp9zTUnFg7MMerT9lgyDQTWfNXCg8HckZiDJTh+LIZyKT+akw+eI1Rb02EkbfNkmmLO4Bn1w9rwHMlfY8lL6vWQEqpv6Ret3nKraszV6HxtldJkkDnxB3sUWu3SXHWVFXGsVRXlbYXCQW1HEzWVFCh8lDwS4KPUN9IczJAjx3paOk6p6wTXzuMZMPsKZTiutQt1LMHaL35JHnE6erDvthPFuIAic5Ug38NaurdHM2J65b2IUSpFbaT1dkSg5xm1fQveUSPbZ4lTHW0VnOAPjkVTErU/5c2BNEmGJHIxCFJm3FrlNbq4pihA; bm_sv=48B631589134FE75AC354BCA0048654A~YAAQjHMsMcPiC4+BAQAAXc10rRDSGbrI1g6WEPFZGdQV1f8fxzMuU/yDIe5WLWh0HI1R9o6IPH0Vfrpp6GhYvH1MzLYWdPe77LLtPuJO/m/AEi3DCqPA1LqBObihrCeLQN2KOWppGP0d33AURomWbV1/RN8b6Vq3aQvF7G8SrB9s+NqJVdcNDdnDwYpUT6S8JXX81DpAI7H6Pxq0Wvs+oJcdq+00q9cQXG/3kIzdgDl+GuGJrxLzGs1I61tEE4DB/TQ=~1");
		headers.put("user-agen", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");
		//headers.put("Referer", "https://www.nseindia.com/market-data/live-equity-market");
		headers.put("Referrer-Policy", "strict-origin-when-cross-origin");
		System.out.println("Reading data from X0001"+url);
		try {
			return Util.getURLDataWithHeaders(url, headers);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public String getFileData(String path) {
		try {
			return Util.readString(path, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getS3Data(String bucketName, String key) {
		AmazonS3 s3=new AmazonS3Client();
		S3Object object = s3.getObject(new GetObjectRequest(bucketName, key));
		try {
			return getTextInputStream(object.getObjectContent());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static String getTextInputStream(InputStream input) throws IOException {
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

}
