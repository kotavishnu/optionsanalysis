package com.vis.optiontools.analytics.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

public class OptionChain implements Comparable<OptionChain>{
	private SortedSet<Strike> calls=new TreeSet<Strike>();
	private SortedSet<Strike> puts=new TreeSet<Strike>();
	private SortedSet<RowStrike> rowStrikes=new TreeSet<RowStrike>();
	private long underlyingIndex;
	private Date time;
	private String sTime;
	private String expiryDate;
	
	public OptionChain(long underlyingIndex, String sTime, String expiryDate) {
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
			throw new Exception("Puts and calls strikes does not match");
		}
		for(Strike call : calls) {
			for(Strike put : puts) {
				if(call.getStrikePrice()==put.getStrikePrice()) {
					rowStrikes.add(new RowStrike(call.getStrikePrice(), call, put));
				}
			}
		}
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

	public long getUnderlyingIndex() {
		return underlyingIndex;
	}

	public void setUnderlyingIndex(long underlyingIndex) {
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
			double pchangeinOpenInterest, long totalTradedVolume, double lastPrice, double change, double pChange,
			String optionType, double impliedVolatility) {
		
		Strike strike=new Strike(strikePrice, identifier, openInterest, changeinOpenInterest,
				pchangeinOpenInterest, totalTradedVolume, lastPrice, change, pChange,
				optionType, impliedVolatility);
		if(optionType.equals("CE")) calls.add(strike);
		if(optionType.equals("PE")) puts.add(strike);
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
			return "\n OpenInterest " + call.getOpenInterest() + " ChangeinOpenInterest" + call.getChangeinOpenInterest() + " price "
					+ call.getLastPrice() + " Strike " + strikePrice + " OpenInterest " + put.getOpenInterest() + " ChangeinOpenInterest"
					+ put.getChangeinOpenInterest() + " Price " + put.getLastPrice();
			
		}
		
	}
	
	public class Strike implements Comparable<Strike>{
		long strikePrice;
		String identifier;
		long openInterest;
		long changeinOpenInterest;
		double pchangeinOpenInterest;
		long totalTradedVolume;
		double lastPrice;
		double change;
		double pChange;
		public Strike(long strikePrice, String identifier, long openInterest, long changeinOpenInterest,
				double pchangeinOpenInterest, long totalTradedVolume, double lastPrice, double change, double pChange,
				String optionType, double impliedVolatility) {
			super();
			this.strikePrice = strikePrice;
			this.identifier = identifier;
			this.openInterest = openInterest;
			this.changeinOpenInterest = changeinOpenInterest;
			this.pchangeinOpenInterest = pchangeinOpenInterest;
			this.totalTradedVolume = totalTradedVolume;
			this.lastPrice = lastPrice;
			this.change = change;
			this.pChange = pChange;
			this.optionType = optionType;
			this.impliedVolatility = impliedVolatility;
		}
		String optionType;
		double impliedVolatility;
		public double getImpliedVolatility() {
			return impliedVolatility;
		}
		public void setImpliedVolatility(double impliedVolatility) {
			this.impliedVolatility = impliedVolatility;
		}
		public long getStrikePrice() {
			return strikePrice;
		}
		public void setStrikePrice(long strikePrice) {
			this.strikePrice = strikePrice;
		}
		public String getIdentifier() {
			return identifier;
		}
		public void setIdentifier(String identifier) {
			this.identifier = identifier;
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
		public void setChangeinOpenInterest(int changeinOpenInterest) {
			this.changeinOpenInterest = changeinOpenInterest;
		}
		public double getPchangeinOpenInterest() {
			return pchangeinOpenInterest;
		}
		public void setPchangeinOpenInterest(double pchangeinOpenInterest) {
			this.pchangeinOpenInterest = pchangeinOpenInterest;
		}
		public long getTotalTradedVolume() {
			return totalTradedVolume;
		}
		public void setTotalTradedVolume(int totalTradedVolume) {
			this.totalTradedVolume = totalTradedVolume;
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
		public String getOptionType() {
			return optionType;
		}
		public void setOptionType(String optionType) {
			this.optionType = optionType;
		}
		@Override
		public String toString() {
			return "Strike [strikePrice=" + strikePrice + ", identifier=" + identifier + ", openInterest="
					+ openInterest + ", changeinOpenInterest=" + changeinOpenInterest + ", pchangeinOpenInterest="
					+ pchangeinOpenInterest + ", totalTradedVolume=" + totalTradedVolume + ", lastPrice=" + lastPrice
					+ ", change=" + change + ", pChange=" + pChange + ", optionType=" + optionType + "]";
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + Objects.hash(optionType, strikePrice);
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Strike other = (Strike) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			return Objects.equals(optionType, other.optionType) && strikePrice == other.strikePrice;
		}
		private OptionChain getEnclosingInstance() {
			return OptionChain.this;
		}
		@Override
		public int compareTo(Strike o) {
			System.out.println("strikePrice " +strikePrice+" other strikePrice "+o.getStrikePrice());
			System.out.println("optionType " +optionType+" other strikePrice "+o.getOptionType());
			if (o == null)	return 1;
			if (this == o)	return 0;
			if (getClass() != o.getClass())	return -1;
			if(strikePrice>o.strikePrice) return 1;
			if(strikePrice<o.strikePrice) return -1;
			return optionType.compareTo(o.getOptionType());
		}
		
		
	}
	
	public void addStrikeToCalls(Strike strike) throws Exception {
		if(strike.getOptionType().equals("CE")) {
			calls.add(strike);
		}else {
			throw new Exception("Other than call type is added to calls");
		}
	}
	
	public void addStrikeToPuts(Strike strike) throws Exception  {
		if(strike.getOptionType().equals("CE")) {
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
		return "OptionChain [calls=" + calls + ", puts=" + puts + ", rowStrikes=" + rowStrikes + ", underlyingIndex="
				+ underlyingIndex + ", time=" + time + ", sTime=" + sTime + ", expiryDate=" + expiryDate + "]";
	}
	
	
}
