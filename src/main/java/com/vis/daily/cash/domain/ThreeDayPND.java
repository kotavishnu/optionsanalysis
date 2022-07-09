package com.vis.daily.cash.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ThreeDayPND {
	Set<Equity> day1;
	Set<Equity> day2;
	Set<Equity> day3;
	
	public void addDay1(Set<Equity> day1) {
		this.day1=day1;
	}
	
	public void addDay2(Set<Equity> day2) {
		this.day2=day2;
	}
	
	public void addDay3(Set<Equity> day3) {
		this.day3=day3;
	}
	
	
	public ThreeDayPND(Set<Equity> day1, Set<Equity> day2, Set<Equity> day3) {
		super();
		this.day1 = day1;
		this.day2 = day2;
		this.day3 = day3;
	}

	public Map<String,Double> getThreeDayPnD() {
		HashMap<String,Double> threeDaySumm=new HashMap<String, Double>();
		for(Equity e1:day1) {
			for(Equity e2:day2) {
				if(e1.getSymbol().equals(e2.getSymbol())) {
					for(Equity e3:day3) {
						if(e2.getSymbol().equals(e3.getSymbol())) {
							threeDaySumm.put(e1.getSymbol(), e1.getChangePercentage()+e2.getChangePercentage()+e3.getChangePercentage());
						}
					}
				}
			}
		}
		return threeDaySumm;
	}

	
}
