package com.vis.daily.cash.domain;

import java.util.Objects;

public class Strike implements Comparable<Strike>{
	String expiry;
	String optionType;
	String identifier;
	int strikePrice;
	double openPrice;
	double highPrice;
	double lowPrice;
	double closePrice;
	double prevClose;
	double lastPrice;
	double change;
	double pChange;
	int numberOfContractsTraded;
	double totalTurnover;
	int tradedVolume;
	int openIntrest;
	int changeinOpenInterest;
	double pchangeinOpenInterest;
	public Strike(String expiry, String optionType, String identifier,int strikePrice, double openPrice, double highPrice,
			double lowPrice, double closePrice, double prevClose, double lastPrice, double change, double pChange,
			int numberOfContractsTraded, double totalTurnover, int tradedVolume, int openIntrest,
			int changeinOpenInterest, double pchangeinOpenInterest) {
		super();
		this.expiry = expiry;
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
	}
	public int getStrikePrice() {
		return strikePrice;
	}
	public void setStrikePrice(int strikePrice) {
		this.strikePrice = strikePrice;
	}
	public String getExpiry() {
		return expiry;
	}
	public void setExpiry(String expiry) {
		this.expiry = expiry;
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
	public int getNumberOfContractsTraded() {
		return numberOfContractsTraded;
	}
	public void setNumberOfContractsTraded(int numberOfContractsTraded) {
		this.numberOfContractsTraded = numberOfContractsTraded;
	}
	public double getTotalTurnover() {
		return totalTurnover;
	}
	public void setTotalTurnover(double totalTurnover) {
		this.totalTurnover = totalTurnover;
	}
	public int getTradedVolume() {
		return tradedVolume;
	}
	public void setTradedVolume(int tradedVolume) {
		this.tradedVolume = tradedVolume;
	}
	public int getOpenIntrest() {
		return openIntrest;
	}
	public void setOpenIntrest(int openIntrest) {
		this.openIntrest = openIntrest;
	}
	public int getChangeinOpenInterest() {
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
	@Override
	public int hashCode() {
		return Objects.hash(expiry, optionType, strikePrice);
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
		return Objects.equals(expiry, other.expiry) && Objects.equals(optionType, other.optionType)
				&& strikePrice == other.strikePrice;
	}
	@Override
	public int compareTo(Strike o)  {
		if(expiry.equals(o.getExpiry())) {
			if(strikePrice==o.getStrikePrice()) return 0;
			if(strikePrice>o.getStrikePrice()) return 1;
			if(strikePrice>o.getStrikePrice()) return -1;
		}else {
			System.out.println("Strike prices of 2 different expiries are compared, could data issue");
		}
		return -1;
	}
	
	
	
}
