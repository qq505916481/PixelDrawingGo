package pixel;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.*;

public class DrawUtils {
	public String  drawApiBody = "https://api.live.bilibili.com/activity/v1/SummerDraw/draw";
	
	public Result drawPixel(int x, int y, String color, String cookie, String userAgent) {
		try {
			URL url = new URL(drawApiBody + "?x_min=" + x + "&y_min=" + y + "&x_max=" + x + "&y_max=" + y + "&color=" + color);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(15*1000); //15秒超时
			conn.setReadTimeout(15*1000); //同楼上防止线程堵塞
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Cookie", cookie);
			conn.setRequestProperty("User-Agent", userAgent);
			//conn.connect();
			
			
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			String s;
			StringBuilder sb = new StringBuilder();
			while((s=br.readLine()) != null) {
				sb.append(s);
			}
			br.close();
			conn.disconnect();
			return new Result().parse(new JSONObject(sb.toString()));
		} catch(Exception e) {
			e.printStackTrace();
			Result r = new Result();
			r.code = -666;
			return r;
		}
		
	}

	public Result drawPixelReborn(int x, int y , String color, String cookie, String userAgent) {
		String x1 = String.valueOf(x);
		String y1 = String.valueOf(y);
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(drawApiBody);
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(10 * 1000)
				.setConnectionRequestTimeout(10 * 1000)
				.setSocketTimeout(10 * 1000)
				.setRedirectsEnabled(true)
				.build();
		
		httpPost.setConfig(requestConfig);
		httpPost.setHeader("User-Agent", userAgent);
		httpPost.setHeader("Cookie", cookie);
		httpPost.setHeader("Accept-Encoding", "identity");
		
		List<BasicNameValuePair> list = new ArrayList<>();
		list.add(new BasicNameValuePair("x_min", x1));
		list.add(new BasicNameValuePair("y_min", y1));
		list.add(new BasicNameValuePair("x_max", x1));
		list.add(new BasicNameValuePair("y_max", y1));
		list.add(new BasicNameValuePair("color", color));
		
			try {
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
				httpPost.setEntity(entity);
				
				CloseableHttpResponse response = httpClient.execute(httpPost);
				
				HttpEntity entity2 = response.getEntity();
				
				InputStream in = entity2.getContent();
				int dataLength = 0;
				while(dataLength == 0) {
					dataLength = Integer.valueOf(String.valueOf(entity2.getContentLength()));
				}
				byte[] b = new byte[dataLength];
				
				int readDataLength = 0;
				while(readDataLength < dataLength) {
					readDataLength += in.read(b, readDataLength, dataLength - readDataLength);
				}
				
				in.close();
				return new Result().parse(new JSONObject(new String(b, 0, dataLength)));
				
				
				
			} catch (UnsupportedEncodingException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} finally {
				try {
					httpClient.close();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
			Result r = new Result();
			r.code = -666;
			return null;
	}
	
}
class Result{
	public int code;
	
	public Result parse(JSONObject json) {
		code = json.optInt("code");
		return this;
	}
}
