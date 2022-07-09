package com.vis.optiontools.analytics.domain;

public class ExpiryDirectionPrediction {
	OptionChain openingOptionChain;
	OptionChain midDayOptionChain;
	OptionChain closingOptionChain;
	double diffMidDay;
	double diffMidDayToClosing;
	
	
	
	public ExpiryDirectionPrediction(OptionChain openingOptionChain, OptionChain midDayOptionChain,
			OptionChain closingOptionChain) {
		super();
		this.openingOptionChain = openingOptionChain;
		this.midDayOptionChain = midDayOptionChain;
		this.closingOptionChain = closingOptionChain;
	}

	public void build() {
		diffMidDayToClosing=closingOptionChain.getUnderlyingIndex()-midDayOptionChain.getUnderlyingIndex();
		diffMidDay=midDayOptionChain.getUnderlyingIndex()-openingOptionChain.getUnderlyingIndex();
		
	}

	public static void main(String[] args) {
		//ExpiryDirectionPrediction p=new ExpiryDirectionPrediction();
		//p.build();
	}

	public OptionChain getClosingOptionChain() {
		return closingOptionChain;
	}

	public void setClosingOptionChain(OptionChain closingOptionChain) {
		this.closingOptionChain = closingOptionChain;
	}

	public double getDiffMidDay() {
		return diffMidDay;
	}

	public void setDiffMidDay(double diffMidDay) {
		this.diffMidDay = diffMidDay;
	}

	public void formatData() {
		System.out.println("_________________________________"
				+ "\n Opening Index "+openingOptionChain.getUnderlyingIndex());
		System.out.println("Expiry "+openingOptionChain.getExpiryDate());
		System.out.println("Time "+openingOptionChain.getsTime());
		System.out.println("Miday Index "+midDayOptionChain.getUnderlyingIndex());
		System.out.println("Midday Time "+midDayOptionChain.getsTime());
		System.out.println("Mid Day Option Chain");
		String header="\nCall OI Value,Change OI Value,OI,Change OI,Last Price,strikePrice,Last Price,OI Value,Change OI Value,OI,Change OI,Underlying,Time";

		System.out.println(header);
		System.out.println(midDayOptionChain.getRowStrikes());
		
		System.out.println("Closing Index "+closingOptionChain.getUnderlyingIndex());
		System.out.println("Closing Time "+closingOptionChain.getsTime());
		System.out.println("Closing Option Chain");
				
		System.out.println(header);
		System.out.println(closingOptionChain.getRowStrikes());
	}
	
	public String printSummary() {
		String header="\nSum Call OI,Sum Call Value OI,Underlying,Sum Put OI,Sum Put Value OI,Time,Sum Call OI,Sum Call Value OI,Underlying,Sum Put OI,Sum Put Value OI,Time,Expiry";
		StringBuilder sb=new StringBuilder();
		sb.append(header);
		sb.append("\n"+midDayOptionChain.getSumCallOI()+","+midDayOptionChain.getSumCallValueOI()+","+midDayOptionChain.getUnderlyingIndex()+","+midDayOptionChain.getSumPutOI()+","+midDayOptionChain.getSumCallValueOI()+midDayOptionChain.getsTime());
		sb.append(closingOptionChain.getSumCallOI()+","+closingOptionChain.getSumCallValueOI()+","+closingOptionChain.getUnderlyingIndex()+","+closingOptionChain.getSumPutOI()+","+closingOptionChain.getSumCallValueOI()+closingOptionChain.getsTime()+closingOptionChain.getExpiryDate());
		return sb.toString();
	}
	
}
