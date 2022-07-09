package com.vis.optiontools.analytics.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class OCTimestamps implements Comparable<OCTimestamps>{
	String fileName;
	String sTimeStamp;
	Date timeStamp;
	public OCTimestamps(String fileName,String sTimeStamp) {
		super();
		this.fileName = fileName;
		this.sTimeStamp=sTimeStamp;
		try {
			timeStamp= new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").parse(sTimeStamp);
		} catch (ParseException e) {
			System.out.println("Error while parsing the timestamp "+sTimeStamp);
			e.printStackTrace();
		}
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getsTimeStamp() {
		return sTimeStamp;
	}
	public void setsTimeStamp(String sTimeStamp) {
		this.sTimeStamp = sTimeStamp;
	}
	public Date getTimeStamp() {
		return timeStamp;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(sTimeStamp);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OCTimestamps other = (OCTimestamps) obj;
		return Objects.equals(sTimeStamp, other.sTimeStamp);
	}
	
	@Override
	public int compareTo(OCTimestamps o) {
		return timeStamp.compareTo(o.getTimeStamp());
	}
	@Override
	public String toString() {
		return "OCTimestamps [fileName=" + fileName + ", sTimeStamp=" + sTimeStamp + ", timeStamp=" + timeStamp + "]";
	}
	
	
}
