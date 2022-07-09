package com.vis.daily.cash.domain;

import java.util.Date;
import java.util.Objects;

public class Equity implements Comparable<Equity> {
	String symbol;
	String series;
	Date date;
	String sDate;
	double prevPrice;
	double openPrice;
	double highPrice;
	double lowPrice;
	double lastPrice;
	double closePrice;
	int volumeTraded;
	double totalTurnover;
	int deliveryQuantity;
	double deliveryPercentage;
	Double changePercentage;
	public Equity(String symbol, String series, String sDate, double prevPrice, double openPrice, double highPrice,
			double lowPrice, double lastPrice, double closePrice, int volumeTraded, double totalTurnover,
			int deliveryQuantity, double deliveryPercentage) {
		super();
		this.symbol = symbol;
		this.series = series;
		this.sDate = sDate;
		this.prevPrice = prevPrice;
		this.openPrice = openPrice;
		this.highPrice = highPrice;
		this.lowPrice = lowPrice;
		this.lastPrice = lastPrice;
		this.closePrice = closePrice;
		this.volumeTraded = volumeTraded;
		this.totalTurnover = totalTurnover;
		this.deliveryQuantity = deliveryQuantity;
		this.deliveryPercentage = deliveryPercentage;
		this.changePercentage=(closePrice-prevPrice)/prevPrice;
	}
	public Double getChangePercentage() {
		return changePercentage;
	}
	public void setChangePercentage(double changePercentage) {
		this.changePercentage = changePercentage;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	public double getPrevPrice() {
		return prevPrice;
	}
	public void setPrevPrice(double prevPrice) {
		this.prevPrice = prevPrice;
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
	public double getLastPrice() {
		return lastPrice;
	}
	public void setLastPrice(double lastPrice) {
		this.lastPrice = lastPrice;
	}
	public double getClosePrice() {
		return closePrice;
	}
	public void setClosePrice(double closePrice) {
		this.closePrice = closePrice;
	}
	public int getVolumeTraded() {
		return volumeTraded;
	}
	public void setVolumeTraded(int volumeTraded) {
		this.volumeTraded = volumeTraded;
	}
	public double getTotalTurnover() {
		return totalTurnover;
	}
	public void setTotalTurnover(double totalTurnover) {
		this.totalTurnover = totalTurnover;
	}
	public int getDeliveryQuantity() {
		return deliveryQuantity;
	}
	public void setDeliveryQuantity(int deliveryQuantity) {
		this.deliveryQuantity = deliveryQuantity;
	}
	public double getDeliveryPercentage() {
		return deliveryPercentage;
	}
	public void setDeliveryPercentage(double deliveryPercentage) {
		this.deliveryPercentage = deliveryPercentage;
	}

	
	
	@Override
	public String toString() {
		return "Equity [symbol=" + symbol + ", series=" + series + ", date=" + date + ", sDate=" + sDate
				+ ", prevPrice=" + prevPrice + ", openPrice=" + openPrice + ", highPrice=" + highPrice + ", lowPrice="
				+ lowPrice + ", lastPrice=" + lastPrice + ", closePrice=" + closePrice + ", volumeTraded="
				+ volumeTraded + ", totalTurnover=" + totalTurnover + ", deliveryQuantity=" + deliveryQuantity
				+ ", deliveryPercentage=" + deliveryPercentage + ", changePercentage=" + changePercentage + "]";
	}
	@Override
	public int hashCode() {
		return Objects.hash(closePrice, date, deliveryPercentage, deliveryQuantity, highPrice, lastPrice, lowPrice,
				openPrice, prevPrice,  sDate,  symbol, totalTurnover, volumeTraded);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Equity other = (Equity) obj;
		return Objects.equals(sDate, other.sDate)
				&& Objects.equals(symbol, other.symbol);
	}
	@Override
	public int compareTo(Equity o) {
		return symbol.compareTo(o.getSymbol());
	}
}
