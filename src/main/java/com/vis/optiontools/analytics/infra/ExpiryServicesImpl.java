package com.vis.optiontools.analytics.infra;

import java.util.Set;
import java.util.TreeSet;

import com.vis.optiontools.analytics.domain.ExpiryDirectionPrediction;
import com.vis.optiontools.analytics.domain.OCTimestamps;
import com.vis.optiontools.analytics.domain.OptionChain;
import com.vis.optiontools.analytics.domain.Strike;
import com.vis.optiontools.analytics.domain.services.ExpiryServices;
import com.vis.optiontools.util.S3Util;

public class ExpiryServicesImpl implements ExpiryServices {
	String expiry;
	
	
	public ExpiryServicesImpl(String expiry) {
		super();
		this.expiry = expiry;
	}

	public static void main(String[] args) {
		String expiry="07-Jul-2022";
		ExpiryServicesImpl es=new ExpiryServicesImpl(expiry);
		es.predictDirection();
	}

	@Override
	public Set<Strike> getExpiryGetProbableStrikes() {
		return null;
	}

	@Override
	public ExpiryDirectionPrediction predictDirection() {
		Set<OCTimestamps> sortedTimestamps= S3Util.getOCTimestamps();
		System.out.println("Sorted timestamps"+sortedTimestamps);
		TreeSet<OCTimestamps> s=new TreeSet<OCTimestamps>(sortedTimestamps);
		OptionChain openingOptionChain=getOptionChain(s.first());
		try {
			openingOptionChain.buildRowStrikes();
			System.out.println("Opening row size "+openingOptionChain.getRowStrikes().size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Opening option chain "+openingOptionChain);
		System.out.println("geeting Option chain for closing "+s.last());
		OptionChain closingOptionChain=getOptionChain(s.last());
		try {
			closingOptionChain.buildRowStrikes();
			System.out.println("Closing row size "+closingOptionChain.getRowStrikes().size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println("Closing opiont chain "+closingOptionChain);
		OptionChain midDayOptionChain=getOptionChain(getMidDay(s));
		try {
			midDayOptionChain.buildRowStrikes();
			System.out.println("Mid day row size "+midDayOptionChain.getRowStrikes().size());
			ExpiryDirectionPrediction edp=new ExpiryDirectionPrediction(openingOptionChain,midDayOptionChain,closingOptionChain);
			edp.formatData();
			System.out.println(edp.printSummary());
			return edp;
		} catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println("Mid day opiont chain "+midDayOptionChain);
		
		return null;
	}
	private OCTimestamps getMidDay(TreeSet<OCTimestamps> s) {
		for(OCTimestamps oc:s) {
			if(oc.getTimeStamp().getHours()>=14 && oc.getTimeStamp().getMinutes()>15)
				return oc;
		}
		return null;
	}

	private OptionChain getOptionChain(OCTimestamps first) {
		//String expiry="07-Jul-2022";
		try {
			return S3Util.getOptionChain(first.getFileName(), expiry);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
