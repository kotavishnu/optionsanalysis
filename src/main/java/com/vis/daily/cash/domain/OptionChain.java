package com.vis.daily.cash.domain;

import java.util.Date;
import java.util.Set;

public class OptionChain {
	Set<Strike> callStrikes;
	Set<Strike> putStrikes;
	double underlyingValue;
	Date timeStamp;
	String sTimestamp;
	public OptionChain(Set<Strike> callStrikes, Set<Strike> putStrikes, double underlyingValue, Date timeStamp,
			String sTimestamp) {
		super();
		this.callStrikes = callStrikes;
		this.putStrikes = putStrikes;
		this.underlyingValue = underlyingValue;
		this.timeStamp = timeStamp;
		this.sTimestamp = sTimestamp;
	}
	public double getUnderlyingValue() {
		return underlyingValue;
	}
	public void setUnderlyingValue(double underlyingValue) {
		this.underlyingValue = underlyingValue;
	}
	public Date getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getsTimestamp() {
		return sTimestamp;
	}
	public void setsTimestamp(String sTimestamp) {
		this.sTimestamp = sTimestamp;
	}
	
	public void addCallStrike(Strike s) {
		callStrikes.add(s);		
	}
	
	public void addPutStrike(Strike s) {
		putStrikes.add(s);		
	}
}
