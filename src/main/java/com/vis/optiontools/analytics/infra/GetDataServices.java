package com.vis.optiontools.analytics.infra;

public interface GetDataServices {
	public String getURLData(String url);
	public String getFileData(String path);
	public String getS3Data(String bucketName,String objectName);
	
}
