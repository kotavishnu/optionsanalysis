package com.vis.optiontools.analytics.domain;

import java.util.Date;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public class DayFutures {
	SortedSet <Future> futures=new TreeSet<Future>();
	Date analysisData;
	public DayFutures(Date date) {
		super();
		this.analysisData = date;
	}
	
	public void addFuture(MonthlyFutures monFutures) {
		futures.add(monFutures.getFutures().first());
	}

	@Override
	public String toString() {
		return "DayFutures [futures=" + futures + ", data=" + analysisData + "]";
	}
	
	public boolean isOpenVolumeGreaterThan(int volume) {
		Future last=futures.last();
		System.out.println("Open future of the day is "+last);
		System.out.println("Volume is "+last.getTradedVolume()+" time is "+last.getFutureTimeStamp());
		return last.getTradedVolume()*25>volume;
	}
	
	public boolean isLast5MinsVolumeGreaterThan(int volume) {
		Future latestFut=futures.first();
		Future prevFut=getSecondLast(futures);
		System.out.println("latestFut "+latestFut);
		System.out.println("prevFut "+prevFut);
		long diffvolume=latestFut.getTradedVolume()-prevFut.getTradedVolume();
		return diffvolume * 25 > volume;
	}

	private Future getSecondLast(SortedSet<Future> futures2) {
		Iterator<Future> it = futures2.iterator();
		it.next();
		return it.next();
	}
	
	public int size() {
		return futures.size();
	}
	
	public void validate() {
		if(futures.size()<2)return;
		Future last = futures.last();
		Future first = futures.first();
		System.out.println(" last is " + last);
		System.out.println(" first is " + first);
		if (last.getFutureTimeStamp().getDate() != first.getFutureTimeStamp().getDate()) {
			System.out.println("Previous day future found removing "+last);
			futures.remove(last);
			System.out.println("new first element is "+futures.last());
		}
		
		
	}
}
