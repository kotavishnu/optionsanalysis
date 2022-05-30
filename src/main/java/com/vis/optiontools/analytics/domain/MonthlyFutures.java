package com.vis.optiontools.analytics.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

public class MonthlyFutures {
	SortedSet<Future> futures=new TreeSet<Future>();
	Date timestamp;
	String futTimestamp;
	
	
	public MonthlyFutures(String futTimestamp) {
		super();
		this.futTimestamp = futTimestamp;//27-May-2022 15:30:01
		try {
			timestamp=new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").parse(futTimestamp);
		} catch (ParseException e) {
			e.printStackTrace();
		} 
	}

	@Override
	public String toString() {
		return "MonthlyFutures [futures=" + futures + ", timestamp=" + timestamp + ", futTimestamp=" + futTimestamp
				+ "]";
	}

	public void addMonthlyFuture(Future f) {
		futures.add(f);
	}

	public SortedSet<Future> getFutures(){
		return futures;
	}
	
}
