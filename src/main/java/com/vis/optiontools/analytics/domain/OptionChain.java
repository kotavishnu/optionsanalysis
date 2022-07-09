package com.vis.optiontools.analytics.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class OptionChain implements Comparable<OptionChain>{
	private SortedSet<Strike> calls=new TreeSet<Strike>();
	private SortedSet<Strike> puts=new TreeSet<Strike>();
	private SortedSet<RowStrike> rowStrikes=new TreeSet<RowStrike>();
	private Double underlyingIndex;
	private Date time;
	private String sTime;
	private String expiryDate;
	double sumCallOI;
	double sumPutOI;
	double sumCallValueOI;
	double sumPutOIValue;
	
	public SortedSet<RowStrike> getRowStrikes() {
		return rowStrikes;
	}

	public void setRowStrikes(SortedSet<RowStrike> rowStrikes) {
		this.rowStrikes = rowStrikes;
	}

	public OptionChain(Double underlyingIndex, String sTime, String expiryDate) {
		super();
		this.underlyingIndex = underlyingIndex;
		this.sTime = sTime;
		try {
			time=new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").parse(sTime);
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		this.setExpiryDate(expiryDate);
	}

	public void buildRowStrikes() throws Exception{
		if(calls.size()==0) {
			System.out.println("Not enough Data");
			throw new Exception("Not enough Data in calls");
		}
		if(puts.size()==0) {
			System.out.println("Not enough Data");
			throw new Exception("Not enough Data in puts");
		}
		if(puts.size() != calls.size()) {
			System.out.println("Puts and calls strikes does not match");
			System.out.println("Puts "+puts.size());
			System.out.println("calls "+calls.size());
			//throw new Exception("Puts and calls strikes does not match");
		}
		for(Strike call : calls) {
			for(Strike put : puts) {
				if(call.getStrikePrice()==put.getStrikePrice()) {
					rowStrikes.add(new RowStrike(call.getStrikePrice(), call, put));
					sumCallOI=sumCallOI+call.getOpenIntrest();
					sumPutOI=sumPutOI+put.getOpenIntrest();
					sumCallValueOI=sumCallValueOI+call.getCallValueOI();
					sumPutOIValue=sumPutOIValue+put.getPutValueOI();
				}
			}
		}
	}
	
	public double getSumCallOI() {
		return sumCallOI;
	}

	public void setSumCallOI(double sumCallOI) {
		this.sumCallOI = sumCallOI;
	}

	public double getSumPutOI() {
		return sumPutOI;
	}

	public void setSumPutOI(double sumPutOI) {
		this.sumPutOI = sumPutOI;
	}

	public double getSumCallValueOI() {
		return sumCallValueOI;
	}

	public void setSumCallValueOI(double sumCallValueOI) {
		this.sumCallValueOI = sumCallValueOI;
	}

	public double getSumPutOIValue() {
		return sumPutOIValue;
	}

	public void setSumPutOIValue(double sumPutOIValue) {
		this.sumPutOIValue = sumPutOIValue;
	}

	public SortedSet<Strike> getCalls() {
		return calls;
	}

	public void setCalls(SortedSet<Strike> calls) {
		this.calls = calls;
	}

	public SortedSet<Strike> getPuts() {
		return puts;
	}

	public void setPuts(SortedSet<Strike> puts) {
		this.puts = puts;
	}

	public Double getUnderlyingIndex() {
		return underlyingIndex;
	}

	public void setUnderlyingIndex(Double underlyingIndex) {
		this.underlyingIndex = underlyingIndex;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getsTime() {
		return sTime;
	}

	public void setsTime(String sTime) {
		this.sTime = sTime;
	}

	@Override
	public boolean equals(Object obj) {
		OptionChain o1=(OptionChain)obj;
		return sTime.equals(o1.getsTime());
	}

	@Override
	public int compareTo(OptionChain o) {
		return time.compareTo(o.getTime());
	}
	
	public void addStrike(long strikePrice, String identifier, long openInterest, long changeinOpenInterest,
			double pchangeinOpenInterest, long numberOfContractsTraded, double lastPrice, double change, double pChange,
			String optionType, double impliedVolatility,double openPrice, double highPrice,
			double lowPrice, double closePrice, double prevClose,long tradedVolume,double totalTurnover) {
		
		Strike strike=new Strike( optionType, identifier, strikePrice,  openPrice,  highPrice,
				 lowPrice,  closePrice,  prevClose,  lastPrice,  change,  pChange,
				 numberOfContractsTraded, totalTurnover, tradedVolume,  openInterest,
				 changeinOpenInterest,  pchangeinOpenInterest);
		
		
		if(optionType.equals("Call")) calls.add(strike);
		if(optionType.equals("Put")) puts.add(strike);
	}
	
	
	public class RowStrike implements Comparable<RowStrike>{
		long strikePrice;
		Strike call;
		Strike put;
		
		
		public long getStrikePrice() {
			return strikePrice;
		}

		public void setStrikePrice(long strikePrice) {
			this.strikePrice = strikePrice;
		}

		public Strike getCall() {
			return call;
		}

		public void setCall(Strike call) {
			this.call = call;
		}

		public Strike getPut() {
			return put;
		}

		public void setPut(Strike put) {
			this.put = put;
		}

		public RowStrike(long strikePrice, Strike call, Strike put) {
			super();
			if(put.getStrikePrice() !=call.getStrikePrice()) new Exception("Miss match strik wrong data");
			this.strikePrice = strikePrice;
			this.call = call;
			this.put = put;
		}

		@Override
		public int compareTo(RowStrike o) {
			if(strikePrice == o.getStrikePrice()) return 0;
			if(strikePrice< o.getStrikePrice()) return -1;
			if(strikePrice > o.getStrikePrice()) return 1;
			return -1;
		}
		
		@Override
		public boolean equals(Object obj) {
			Strike s=(Strike)obj;
			return strikePrice==s.getStrikePrice();
		}

		@Override
		public String toString() {
			return "\n" +call.getCallValueOI()+
					","+call.getCallValueChangeOI()+","+call.getOpenIntrest()+
					","+call.getChangeinOpenInterest()+ "," + call.getLastPrice()+ "," + 
					strikePrice +
					","+put.getLastPrice()+ 
					","+put.getPutValueOI()+
					","+put.getPutValueChangeOI() +","+put.getOpenIntrest()+
					","+put.getChangeinOpenInterest()+
					","+underlyingIndex+","+time;
		}
		
		
	}
	
	
	public void addStrikeToCalls(Strike strike) throws Exception {
		if(strike.getOptionType().equals("Call")) {
			calls.add(strike);
		}else {
			throw new Exception("Other than call type is added to calls");
		}
	}
	
	public void addStrikeToPuts(Strike strike) throws Exception  {
		if(strike.getOptionType().equals("Put")) {
			puts.add(strike);
		}else {
			throw new Exception("Other than put type is added to puts");
		}
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append("\n underlyingIndex "+underlyingIndex);
		sb.append("\n expiryDate "+expiryDate);
		sb.append("\n time "+time);
		sb.append(underlyingIndex+" \t"+time);
		sb.append("\n Open Interst \t Change OI \t Last Price \t Low \t High \t Strike \t High \t Low \t Last Price \t Change OI \t Open Interst");
		for(Strike call:calls) {
			for(Strike put:calls) {
				if(call.getStrikePrice()==put.getStrikePrice() ) {
					String callrow=String.format("\n %d \t %d \t %f \t %f \t %f", call.getOpenIntrest(),call.getChangeinOpenInterest(),call.getLastPrice(),call.getLowPrice(),call.getHighPrice());
					String putrow=String.format("\t %d \t %f \t %f \t %f \t %d \t %d",put.getStrikePrice(),put.getHighPrice(),put.getLowPrice(),put.getLastPrice(), put.getOpenIntrest(),put.getChangeinOpenInterest());
					sb.append(callrow+putrow);
				}
			}
		}
		return sb.toString();
	}
	

	
	public OptionChain getFilteredOptionChain(int count) {
		SortedSet<Strike> newCalls=new TreeSet<Strike>();
		SortedSet<Strike> newPuts=new TreeSet<Strike>();
		OptionChain oc=new OptionChain(underlyingIndex,sTime,expiryDate);
		for(Strike s:calls) {
			if(s.getStrikePrice()<=(underlyingIndex+(100*count)) && s.getStrikePrice()>=(underlyingIndex-300)) {
				newCalls.add(s);
			}
		}
		System.out.println("Calls size is "+newCalls.size());
		
		oc.setCalls(newCalls);
		for(Strike s:puts) {
			if(s.getStrikePrice()>=(underlyingIndex-(100*count)) && s.getStrikePrice()<=(underlyingIndex+300)) {
				newPuts.add(s);
			}
		}
		System.out.println("Puts size is "+newPuts.size());
		oc.setPuts(newPuts);
		return oc;
	}
	
	public Set<Strike> getProbableStrikes(){
		Set<Strike> newCalls=new TreeSet<Strike>();
		Set<Strike> newPuts=new TreeSet<Strike>();
		for(Strike s:calls) {
			newCalls=extracted(newCalls, s);
		}
		for(Strike s:puts) {
			newPuts=extracted(newPuts, s);
		}
		newCalls.addAll(newPuts);
		return newCalls;
	}
	
	public Set<Strike> getNearestStrikes(){
		Set<Strike> strikes=new TreeSet<Strike>();
		for(Strike s:calls) {
			if(s.getStrikePrice()>(underlyingIndex-(100*3)) && s.getStrikePrice()<(underlyingIndex)) {
				strikes.add(s);
			}
		}
		
		for(Strike s:puts) {
			if(s.getStrikePrice()<(underlyingIndex+(100*3)) && s.getStrikePrice()>(underlyingIndex)) {
				strikes.add(s);
			}
		}
		return strikes;
	}

	private Set<Strike> extracted(Set<Strike> strikes, Strike s) {
		if(s.getLowPrice()>18 && s.getLowPrice()>38) {
			strikes.add(s);
		}
		if(s.getLowPrice()<60 && s.getLowPrice()>40) {
			strikes.add(s);
		}
		if(s.getLowPrice()>80 && s.getLowPrice()<120) {
			strikes.add(s);
		}
		if(s.getLowPrice()>100 && s.getLowPrice()<220) {
			strikes.add(s);
		}
		if(s.getLowPrice()>20 && s.getLowPrice()<60) {
			strikes.add(s);
		}
		return strikes;
	}
	
	public void getOIValue() {
		
	}
}
