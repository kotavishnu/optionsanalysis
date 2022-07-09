package com.vis.optiontools.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Util {

	public static void main(String[] args) {
		String url="https://www.nseindia.com/api/equity-stockIndices?index=NIFTY^%^20100";
		Map<String,String> headers=new HashMap<String, String>();
		headers.put("authority", "www.nseindia.com");
		headers.put("cookie", "_ga=GA1.2.13649374.1652708079; _gid=GA1.2.717987851.1655573675; nsit=o3WmM8x60B_j_6OmoNxDUXk7; AKA_A2=A; ak_bmsc=98E637DEB3FEAEC2F477104024FE47B4~000000000000000000000000000000~YAAQNSCwe6IMJieBAQAAIVoXexA4zyCybhPhMt6mN7gGbtA68EGEBi5fJVLHRKfj20FFJy+iFD7F1RBxRsZSzaI910HURwEcyaQY16pOsWpxMbkLBz1ZkNUWu9e4n/CruF8RgIpcfKaitBj/hNC/iTXCZLgwLPnGvPbN1MhBZdd/9lJCIRTJjEjqGUFOiVa3M9U7n047YhbH45uTfryeAMa5Zb5tLNo30CEqyJrq+l7bvzFDPCKIyq4GdjOomUeasbouaLA0RwfQqVATHzck2VN2/QMj5AliAahlxZCPuF2pb9QNSwudw2OUARCWu+Bkr5AKZq7janZOyEF0Mvoh5+CfnPV64H5HUky8FuFTxVEVNk8rheZw9dg2RMnHv27vAfa7uFTRpEIzMHC8Rp7pZIesZwfdICTE1siMlERtQ7lEUk12cyh1/OIyuIj5Zpp+SHw/AMG/LSlu13x71vMoI1kwisJjYhleE1vO3P3hyyirT1hznqRua2ikLnSF; _gat_UA-143761337-1=1; nseappid=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhcGkubnNlIiwiYXVkIjoiYXBpLm5zZSIsImlhdCI6MTY1NTYyNzcyNywiZXhwIjoxNjU1NjMxMzI3fQ.4UZZyFZayvc-adN6Bcty4MmljB0DxD_4WtPqWmLI5Zc; RT=^\\^\\\"z=1&dm=nseindia.com&si=eb620fc1-7393-475b-a0eb-da4d66bd4930&ss=l4l1vttu&sl=2&tt=8k7&bcn=^%^2F^%^2F684d0d4b.akstat.io^%^2F&ld=4c1y^\\^\\\"; bm_sv=6D217D9673EC619952F0FEC1EE16B6B8~YAAQpwxStzKDDDuBAQAA6YIaexD1ctC7hcuhff3VvkiCmiWH0hkUtJjfcs/Y5bkLmR+YHApOEScJSgys4yelB1dNQSaLegg+XSjBJniswWhXJbvw2aJrpXChf45c1fe7gemN1hz+fEs/a15PDqMUnl6/+PjHOQ5sX5OA0vQmhXXO9uM1SO38YCuHwGRbM+xovN0fEVcTiCIHFKl3f41NCNvamaTY/ZiYVtD3kmsi//NwAZl3xmkHsHnidaK9bXTc9dI=~1");
		headers.put("user-agen", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36");
		headers.put("Referer", "https://www.nseindia.com/market-data/live-equity-market");
		headers.put("Referrer-Policy", "strict-origin-when-cross-origin");
		try {
			//Util.getURLDataWithHeaders(url, headers);
			String json=Util.readString("f:/tmp/t/Deriv_BANKNIFTY_9_25.json", StandardCharsets.UTF_8);
			System.out.println("Jons is "+json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String readString(String fileName,Charset encoding) throws IOException {
		if(encoding==null)encoding=StandardCharsets.UTF_8;
		byte[] encoded = Files.readAllBytes(Paths.get(fileName));
        return new String(encoded, encoding);
	}
	
	public static String getURLData(String url) {
		StringBuilder sb=new StringBuilder();

		try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream())) {
			byte dataBuffer[] = new byte[1024];
			int bytesRead;
			while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
				sb.append(new String(dataBuffer, 0, bytesRead));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	public static String getURLDataWithHeaders(String urlStr,Map<String,String> headers) throws IOException {
		URL myURL = new URL(urlStr);
		HttpURLConnection myURLConnection = (HttpURLConnection)myURL.openConnection();
		myURLConnection.setRequestMethod("GET");
		setHeaders(myURLConnection,headers);
		myURLConnection.setUseCaches(false);
		myURLConnection.setDoInput(true);
		myURLConnection.setDoOutput(false);
		
		myURLConnection.connect();
		Object o=myURLConnection.getContent();
		System.out.println("Content is "+o);
		return urlStr;
	}

	private static void setHeaders(HttpURLConnection con,Map<String,String> headers) {
		headers.forEach((k,v) -> con.setRequestProperty(k,v));
	}
	
	
}
