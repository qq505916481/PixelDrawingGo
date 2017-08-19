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
			System.out.println("��ȡλͼʧ��,�������ص���");
			return null;
		}
	}

	public String getBitmapDirtyDataReborn() {
		String readData = null;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(bitmapBody);
		
		// �������ӳ�ʱ, ��������ʱ, �׽��ֳ�ʱ
		RequestConfig resquestConfig = RequestConfig.custom()
				.setConnectTimeout(10 * 1000)
				.setConnectionRequestTimeout(10 * 1000)
				.setSocketTimeout(10 * 1000)
				.setRedirectsEnabled(true)
				.build();
		
		httpGet.setConfig(resquestConfig);
		
		// ����gzipѹ��
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
			while (dataLength == 0) { //ֱ����ȡ���ݳ��ȳɹ���ż���
				// ������������ת����ŭ���!
				dataLength = Integer.parseInt(String.valueOf((entity.getContentLength())));
			}
			
			byte[] b = new byte[dataLength];
			// in.read(b);

			// �������������������͵ø��Ҷ������
			int readDataLength = 0;
			//long startTime = System.currentTimeMillis();
			// ���Ҫʹ�ö��߳�, ����readDataLength����volatile���η�
			while (readDataLength < dataLength) {
				readDataLength += in.read(b, readDataLength, dataLength - readDataLength); // �����byte[]����, ƫ����, ��ȡ����
				Thread.sleep(4 *1000);
				System.out.println(readDataLength);
			}
			readData = new String(b, 0, readDataLength);
			//long endTime = System.currentTimeMillis();
			//System.out.println("����ʱ��:" + (endTime - startTime) / 1000.000 + "s" );
			
			*/
			
			in.close();
			response.close();
			httpGet.releaseConnection();
			httpClient.close();
			
			return readData;

		} catch (ClientProtocolException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
			System.out.println("�쳣:�������⵼�µĻ�ȡλͼ����ʧ��");
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
			System.out.println("IO���쳣, �����Щ�����������ݺ���仰���������� \n ���˲��÷���Ӧ��ֻ������ԭ��ʱ233");
		} finally {
			try {
				httpClient.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		return readData; // ֻ����null
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

		if(res == null)return '?'; //Ȼ���ٷ�bitmap��û�������ַ������Բ��ü���Ƿ�ɹ�
		try {
			c = res.charAt(index);
			return c;
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("�ƺ���ȡ�����ص㳬����Χ����, ����");
			return '?';
		}
		
		
	}

}

