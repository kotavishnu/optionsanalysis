package com.vis.optiontools.analytics.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Future implements Comparable<Future>{
	String sExpiryDate;
	Date expiryDate=null;
	double closePrice;
	double lastPrice;
	double change;
	double pChange;
	long numberOfContractsTraded;
	double underlyingValue;
	long tradedVolume;
	long openInterest;
	long changeinOpenInterest;
	double pchangeinOpenInterest;
	Date futureTimeStamp;
	String sFutureTimestamp;
	
	public Future(String sExpiryDate, double closePrice, double lastPrice, double change, double pChange,
			long numberOfContractsTraded, double underlyingValue, long tradedVolume, long openInterest,
			long changeinOpenInterest, double pchangeinOpenInterest,String sFutTimestamp) {
		super();
		this.sExpiryDate = sExpiryDate;//"30-Jun-2022"
		try {
			expiryDate=new SimpleDateFormat("dd-MMM-yyyy").parse(sExpiryDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		this.closePrice = closePrice;
		this.lastPrice = lastPrice;
		this.change = change;
		this.pChange = pChange;
		this.numberOfContractsTraded = numberOfContractsTraded;
		this.underlyingValue = underlyingValue;
		this.tradedVolume = tradedVolume;
		this.openInterest = openInterest;
		this.changeinOpenInterest = changeinOpenInterest;
		this.pchangeinOpenInterest = pchangeinOpenInterest;
		
		try {
			sFutureTimestamp=sFutTimestamp;
			futureTimeStamp=new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").parse(sFutTimestamp);
		} catch (ParseException e) {
			e.printStackTrace();
		} 
	}
	
	
	
	public String getsExpiryDate() {
		return sExpiryDate;
	}



	public void setsExpiryDate(String sExpiryDate) {
		this.sExpiryDate = sExpiryDate;
	}



	public Date getExpiryDate() {
		return expiryDate;
	}



	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}



	public double getClosePrice() {
		return closePrice;
	}



	public void setClosePrice(double closePrice) {
		this.closePrice = closePrice;
	}



	public double getLastPrice() {
		return lastPrice;
	}



	public void setLastPrice(double lastPrice) {
		this.lastPrice = lastPrice;
	}



	public double getChange() {
		return change;
	}



	public void setChange(double change) {
		this.change = change;
	}



	public double getpChange() {
		return pChange;
	}



	public void setpChange(double pChange) {
		this.pChange = pChange;
	}



	public long getNumberOfContractsTraded() {
		return numberOfContractsTraded;
	}



	public void setNumberOfContractsTraded(long numberOfContractsTraded) {
		this.numberOfContractsTraded = numberOfContractsTraded;
	}



	public double getUnderlyingValue() {
		return underlyingValue;
	}



	public void setUnderlyingValue(double underlyingValue) {
		this.underlyingValue = underlyingValue;
	}



	public long getTradedVolume() {
		return tradedVolume;
	}



	public void setTradedVolume(long tradedVolume) {
		this.tradedVolume = tradedVolume;
	}



	public long getOpenInterest() {
		return openInterest;
	}



	public void setOpenInterest(long openInterest) {
		this.openInterest = openInterest;
	}



	public long getChangeinOpenInterest() {
		return changeinOpenInterest;
	}



	public void setChangeinOpenInterest(long changeinOpenInterest) {
		this.changeinOpenInterest = changeinOpenInterest;
	}



	public double getPchangeinOpenInterest() {
		return pchangeinOpenInterest;
	}



	public void setPchangeinOpenInterest(double pchangeinOpenInterest) {
		this.pchangeinOpenInterest = pchangeinOpenInterest;
	}



	public Date getFutureTimeStamp() {
		return futureTimeStamp;
	}



	public void setFutureTimeStamp(Date futureTimeStamp) {
		this.futureTimeStamp = futureTimeStamp;
	}



	@Override
	public String toString() {
		return "Future [sExpiryDate=" + sExpiryDate + ", expiryDate=" + expiryDate + ", closePrice=" + closePrice
				+ ", lastPrice=" + lastPrice + ", change=" + change + ", pChange=" + pChange
				+ ", numberOfContractsTraded=" + numberOfContractsTraded + ", underlyingValue=" + underlyingValue
				+ ", tradedVolume=" + tradedVolume + ", openInterest=" + openInterest + ", changeinOpenInterest="
				+ changeinOpenInterest + ", pchangeinOpenInterest=" + pchangeinOpenInterest+ ", sFutureTimeStamp "+sFutureTimestamp + ", futureTimeStamp "+futureTimeStamp+"]";
	}
	
	@Override
	public boolean equals(Object obj) {
		Future f=(Future)obj;
		return futureTimeStamp.equals(f.futureTimeStamp);
	}
	@Override
	public int compareTo(Future o) {
		if(futureTimeStamp.getTime()==o.getFutureTimeStamp().getTime())return 0;
		if(futureTimeStamp.getTime()>o.getFutureTimeStamp().getTime())return -1;
		if(futureTimeStamp.getTime()<o.getFutureTimeStamp().getTime())return 1;
		
		return -1;
	}
	
}
