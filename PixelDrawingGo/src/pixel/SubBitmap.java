package pixel;
import java.io.*;
import org.json.*;
public class SubBitmap {

	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		String s1 = null;
		String s2 = null;
		
		try {
			BufferedReader	br = new BufferedReader(new FileReader("bitmap"));
			String s = null;
			StringBuilder sb = new StringBuilder();
			while((s = br.readLine()) != null) {
				sb.append(s);
			}
			s1 = new JSONObject(sb.toString().trim()).optJSONObject("data").optString("bitmap");
			br.close();
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("imagec.txt"));
			while(true) {
				if(s1.length() > 1280) {
					s2 = s1.substring(0, 1280);
					s1 = s1.substring(1280);
					bw.write(s2);
					bw.newLine();
				} else {
					bw.write(s1);
					break;
				}
			}
			bw.close();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
			
		
	}

}
