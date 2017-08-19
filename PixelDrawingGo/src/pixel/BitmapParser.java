package pixel;
import java.net.*;
import java.io.*;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.*;

public class BitmapParser {
	String userAgent = PixelDrawingGo.userAgent;
	public String bitmapBody = "http://api.live.bilibili.com/activity/v1/SummerDraw/bitmap";
	
	public String getBitmapDirtyData(String userAgent) {
		try {
			URL url = new URL(bitmapBody);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(10 * 1000);
			conn.setReadTimeout(10 * 1000);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("User-Agent", userAgent);

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String s;
			StringBuffer sb = new StringBuffer();

			while((s=br.readLine()) != null) {
				sb.append(s);
			}	

			br.close();
			conn.disconnect();
			return sb.toString();
			
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("获取位图失败,忽略象素点检测");
			return null;
		}
	}

	public String getBitmapDirtyDataReborn() {
		String readData = null;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(bitmapBody);
		
		// 设置连接超时, 连接请求超时, 套接字超时
		RequestConfig resquestConfig = RequestConfig.custom()
				.setConnectTimeout(10 * 1000)
				.setConnectionRequestTimeout(10 * 1000)
				.setSocketTimeout(10 * 1000)
				.setRedirectsEnabled(true)
				.build();
		
		httpGet.setConfig(resquestConfig);
		
		// 禁用gzip压缩
		httpGet.setHeader("Accept-Encoding", "identity");
		
		try {
			CloseableHttpResponse response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			BufferedReader in = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
			StringBuilder sb = new StringBuilder();
			String s;
			while((s = in.readLine()) != null) {
				sb.append(s.trim());
			}
			readData = sb.toString();
			
			/*
			 * BufferedWriter bw = new BufferedWriter(new FileWriter("response.txt"));
			* bw.write(readData);
			* bw.newLine();
			* bw.close();
			*/
			
			//InputStream in = entity.getContent();
			/*
			int dataLength = 0;
			while (dataLength == 0) { //直到获取数据长度成功后才继续
				// 感受数据类型转换的怒火吧!
				dataLength = Integer.parseInt(String.valueOf((entity.getContentLength())));
			}
			
			byte[] b = new byte[dataLength];
			// in.read(b);

			// 不不不我让你读多少你就得给我读入多少
			int readDataLength = 0;
			//long startTime = System.currentTimeMillis();
			// 如果要使用多线程, 建议readDataLength加上volatile修饰符
			while (readDataLength < dataLength) {
				readDataLength += in.read(b, readDataLength, dataLength - readDataLength); // 读入的byte[]数组, 偏移量, 读取长度
				Thread.sleep(4 *1000);
				System.out.println(readDataLength);
			}
			readData = new String(b, 0, readDataLength);
			//long endTime = System.currentTimeMillis();
			//System.out.println("所花时间:" + (endTime - startTime) / 1000.000 + "s" );
			
			*/
			
			in.close();
			response.close();
			httpGet.releaseConnection();
			httpClient.close();
			
			return readData;

		} catch (ClientProtocolException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			System.out.println("异常:网络问题导致的获取位图数据失败");
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			System.out.println("IO流异常, 请把这些看不懂的内容和这句话发给开发者 \n 算了不用发了应该只是网络原因超时233");
		} finally {
			try {
				httpClient.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		return readData; // 只会是null
	}
	
	public String getBitmapData(String bitmapDirtyData) throws JSONException {
		if(bitmapDirtyData == null)return null;
		
		JSONObject json = new JSONObject(bitmapDirtyData);
		if(json.optInt("code") == 0) {
			String bitmapData = json.optJSONObject("data").optString("bitmap");
			return bitmapData;
		} else {
			return null;
		}
	}
	
	public char getCharIndexOf(String bitmapData, int index) {
		char c;
		String res = null;
			try {
				// res = getBitmapData(getBitmapDirtyData(this.userAgent));
				res = bitmapData;
			} catch (JSONException e) {
				e.printStackTrace();
		}

		if(res == null)return '?'; //然而官方bitmap并没有这种字符，所以不用检测是否成功
		try {
			c = res.charAt(index);
			return c;
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("似乎获取的象素点超出范围了呢, 跳过");
			return '?';
		}
		
		
	}

}

