package com.vis.optiontools.util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpClient {

    // one instance, reuse
    private static final CloseableHttpClient httpClient = HttpClients.createDefault();

    public static void main(String[] args) throws Exception {

        HttpClient obj = new HttpClient();

        try {
            System.out.println("Testing 1 - Send Http GET request");
            String url="https://www.nseindia.com/api/equity-stockIndices?index=NIFTY%20200";
            obj.getUrlData(url);

            System.out.println("Testing 2 - Send Http POST request");
            //obj.sendPost();
        } finally {
            obj.close();
        }
    }

    private void close() throws IOException {
        httpClient.close();
    }

    public static String getUrlData(String url) throws Exception {

        HttpGet request = new HttpGet(url);

        // add request headers
        //request.addHeader("sec-ch-ua", "\\\" Not A;Brand\\\";v=\\\"99\\\", \\\"Chromium\\\";v=\\\"102\\\", \\\"Google Chrome\\\";v=\\\"102\\\"");
        request.addHeader("cookie","_ga=GA1.2.13649374.1652708079; _gid=GA1.2.717987851.1655573675; ak_bmsc=98E637DEB3FEAEC2F477104024FE47B4~000000000000000000000000000000~YAAQNSCwe6IMJieBAQAAIVoXexA4zyCybhPhMt6mN7gGbtA68EGEBi5fJVLHRKfj20FFJy+iFD7F1RBxRsZSzaI910HURwEcyaQY16pOsWpxMbkLBz1ZkNUWu9e4n/CruF8RgIpcfKaitBj/hNC/iTXCZLgwLPnGvPbN1MhBZdd/9lJCIRTJjEjqGUFOiVa3M9U7n047YhbH45uTfryeAMa5Zb5tLNo30CEqyJrq+l7bvzFDPCKIyq4GdjOomUeasbouaLA0RwfQqVATHzck2VN2/QMj5AliAahlxZCPuF2pb9QNSwudw2OUARCWu+Bkr5AKZq7janZOyEF0Mvoh5+CfnPV64H5HUky8FuFTxVEVNk8rheZw9dg2RMnHv27vAfa7uFTRpEIzMHC8Rp7pZIesZwfdICTE1siMlERtQ7lEUk12cyh1/OIyuIj5Zpp+SHw/AMG/LSlu13x71vMoI1kwisJjYhleE1vO3P3hyyirT1hznqRua2ikLnSF; AKA_A2=A; nsit=1xC1r_ma0uXiETZij9N-Rrpf; nseappid=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhcGkubnNlIiwiYXVkIjoiYXBpLm5zZSIsImlhdCI6MTY1NTYzMjA1NSwiZXhwIjoxNjU1NjM1NjU1fQ.pFk6dbBe_4d_Mu1u74p4K7QcOB04Y60nl7ebE4uulOI; bm_sv=6D217D9673EC619952F0FEC1EE16B6B8~YAAQNSCwe1mEKCeBAQAA11VcexC/ss+Afz5g6PIs5o8oALgnNEl56ldRQTZqS4On8zQ/udCOi3XlQWwG9m0bPRHCj5/v6Z8Do8Fo6JaAPTPCesS6M+Z6fh7HI7ewdOCXiIlooalModxy/mygoTxn5+WgVOaxYDnFSQSs8iqr1e09VPGCwwzM/uADRxnH8lM1KXdd1ARXhlUO+69sAaKpHRvTxbPaZ/RdrGLbR5JHPyXXl58KxL06r5YjMicx+vR7jhSY~1; RT=\"z=1&dm=nseindia.com&si=eb620fc1-7393-475b-a0eb-da4d66bd4930&ss=l4l20310&sl=1&tt=18m&bcn=%2F%2F684d0d4b.akstat.io%2F&ld=2kuzr\"");
        request.addHeader("sec-ch-ua-platform","\"Windows\"");
        request.addHeader("sec-fetch-mode","cors");
        request.addHeader("sec-fetch-site","same-origin");
        request.addHeader("Referer", "https://www.nseindia.com/market-data/live-equity-market");
        request.addHeader("Referrer-Policy", "strict-origin-when-cross-origin");
        request.addHeader("accept", "*/*");
        request.addHeader("accept-language","en-IN,en-GB;q=0.9,en-US;q=0.8,en;q=0.7,hi;q=0.6");
        request.addHeader("sec-ch-ua-mobile","?0");
        request.addHeader("sec-fetch-dest","empty");
        request.addHeader("credentials","include");
        request.addHeader("body","null");
        request.addHeader("user-agent"," Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36");
        try (CloseableHttpResponse response = httpClient.execute(request)) {

            // Get HttpResponse Status
            System.out.println(response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();
            Header headers = entity.getContentType();
            System.out.println(headers);

            if (entity != null) {
                // return it as a String
                String result = EntityUtils.toString(entity);
             
                System.out.println(result);
                return result;
            }

        }
        return null;
    }

    private void sendPost() throws Exception {

        HttpPost post = new HttpPost("https://httpbin.org/post");

        // add request parameter, form parameters
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("username", "abc"));
        urlParameters.add(new BasicNameValuePair("password", "123"));
        urlParameters.add(new BasicNameValuePair("custom", "secret"));

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {

            System.out.println(EntityUtils.toString(response.getEntity()));
        }

    }

}