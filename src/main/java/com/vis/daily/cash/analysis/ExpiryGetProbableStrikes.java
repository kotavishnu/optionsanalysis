package com.vis.daily.cash.analysis;

import com.vis.optiontools.analytics.infra.GetDataServices;
import com.vis.optiontools.analytics.infra.GetDataServicesImpl;

public class ExpiryGetProbableStrikes {
	public static void main(String[] args) {
		String url = "https://www.nseindia.com/api/quote-derivative?symbol=BANKNIFTY";
		GetDataServices ds=new GetDataServicesImpl();
		String content=ds.getURLData(url);
		System.out.println("Content is "+content);
	}
}
