package com.vis.optiontools.analytics.domain;

import java.util.Objects;
import java.util.Set;

public class Strike implements Comparable<Strike>{
	String optionType;
	String identifier;
	long strikePrice;
	double openPrice;
	double highPrice;
	double lowPrice;
	double closePrice;
	double prevClose;
	double lastPrice;
	double change;
	double pChange;
	long numberOfContractsTraded;
	double totalTurnover;
	long tradedVolume;
	long openIntrest;
	long changeinOpenInterest;
	double pchangeinOpenInterest;
	double callValueOI;
	double callValueChangeOI;
	double putValueOI;
	double putValueChangeOI;
	
	public Strike( String optionType, String identifier,long strikePrice, double openPrice, double highPrice,
			double lowPrice, double closePrice, double prevClose, double lastPrice, double change, double pChange,
			long numberOfContractsTraded, double totalTurnover, long tradedVolume, long openIntrest,
			long changeinOpenInterest, double pchangeinOpenInterest) {
		super();
		this.optionType = optionType;
		this.identifier = identifier;
		this.strikePrice=strikePrice;
		this.openPrice = openPrice;
		this.highPrice = highPrice;
		this.lowPrice = lowPrice;
		this.closePrice = closePrice;
		this.prevClose = prevClose;
		this.lastPrice = lastPrice;
		this.change = change;
		this.pChange = pChange;
		this.numberOfContractsTraded = numberOfContractsTraded;
		this.totalTurnover = totalTurnover;
		this.tradedVolume = tradedVolume;
		this.openIntrest = openIntrest;
		this.changeinOpenInterest = changeinOpenInterest;
		this.pchangeinOpenInterest = pchangeinOpenInterest;
		if(optionType.equals("Put")) {
			putValueOI=openIntrest*lastPrice;
			putValueChangeOI=changeinOpenInterest*lastPrice;
		}
		if(optionType.equals("Call")) {
			callValueOI=openIntrest*lastPrice;
			callValueChangeOI=changeinOpenInterest*lastPrice;
		}
	}
	
	public double getCallValueOI() {
		return callValueOI;
	}

	public void setCallValueOI(double callValueOI) {
		this.callValueOI = callValueOI;
	}

	public double getCallValueChangeOI() {
		return callValueChangeOI;
	}

	public void setCallValueChangeOI(double callValueChangeOI) {
		this.callValueChangeOI = callValueChangeOI;
	}

	public double getPutValueOI() {
		return putValueOI;
	}

	public void setPutValueOI(double putValueOI) {
		this.putValueOI = putValueOI;
	}

	public double getPutValueChangeOI() {
		return putValueChangeOI;
	}

	public void setPutValueChangeOI(double putValueChangeOI) {
		this.putValueChangeOI = putValueChangeOI;
	}

	public long getStrikePrice() {
		return strikePrice;
	}
	public void setStrikePrice(long strikePrice) {
		this.strikePrice = strikePrice;
	}
	
	public String getOptionType() {
		return optionType;
	}
	public void setOptionType(String optionType) {
		this.optionType = optionType;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public double getOpenPrice() {
		return openPrice;
	}
	public void setOpenPrice(double openPrice) {
		this.openPrice = openPrice;
	}
	public double getHighPrice() {
		return highPrice;
	}
	public void setHighPrice(double highPrice) {
		this.highPrice = highPrice;
	}
	public double getLowPrice() {
		return lowPrice;
	}
	public void setLowPrice(double lowPrice) {
		this.lowPrice = lowPrice;
	}
	public double getClosePrice() {
		return closePrice;
	}
	public void setClosePrice(double closePrice) {
		this.closePrice = closePrice;
	}
	public double getPrevClose() {
		return prevClose;
	}
	public void setPrevClose(double prevClose) {
		this.prevClose = prevClose;
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
	public double getTotalTurnover() {
		return totalTurnover;
	}
	public void setTotalTurnover(double totalTurnover) {
		this.totalTurnover = totalTurnover;
	}
	public long getTradedVolume() {
		return tradedVolume;
	}
	public void setTradedVolume(long tradedVolume) {
		this.tradedVolume = tradedVolume;
	}
	public long getOpenIntrest() {
		return openIntrest;
	}
	public void setOpenIntrest(long openIntrest) {
		this.openIntrest = openIntrest;
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
	@Override
	public int hashCode() {
		return Objects.hash(optionType, strikePrice);
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
		return  Objects.equals(optionType, other.optionType)
				&& strikePrice == other.strikePrice;
	}
	
	@Override
	public int compareTo(Strike o) {
		if(!optionType.equals(o.getOptionType())) {
			//System.out.println("Comparing wrong strikes of puts/calls");
			return -1;
		}
		if (strikePrice > o.getStrikePrice())
			return 1;
		if (strikePrice > o.getStrikePrice())
			return -1;

		return strikePrice == o.getStrikePrice()?0:-1;
	}
	@Override
	public String toString() {
		return "Strike [optionType=" + optionType + ", identifier=" + identifier + ", strikePrice=" + strikePrice
				+ ", openPrice=" + openPrice + ", highPrice=" + highPrice + ", lowPrice=" + lowPrice + ", closePrice="
				+ closePrice + ", prevClose=" + prevClose + ", lastPrice=" + lastPrice + ", change=" + change
				+ ", pChange=" + pChange + ", numberOfContractsTraded=" + numberOfContractsTraded + ", totalTurnover="
				+ totalTurnover + ", tradedVolume=" + tradedVolume + ", openIntrest=" + openIntrest
				+ ", changeinOpenInterest=" + changeinOpenInterest + ", pchangeinOpenInterest=" + pchangeinOpenInterest
				+ "]";
	}
	
	public static void print(Set<Strike> strikes) {
		System.out.println("Option Type\tStrike \tLast Price\t Low Price \t High Price");
		for(Strike s:strikes) {
			System.out.println(s.getOptionType()+"\t\t"+s.getStrikePrice()+"\t"+s.getLastPrice()+"\t"+s.getLowPrice()+"\t"+s.getHighPrice());
		}
	}
	
}
