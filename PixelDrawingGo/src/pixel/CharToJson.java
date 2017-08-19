package pixel;
import org.json.*;
import java.io.*;

public class CharToJson {
	public static int size;
	public static void main(String[] args) {
		// TODO �Զ����ɵķ������
		try {
			size = args.length == 0?53:Integer.valueOf(args[0]);
			BufferedWriter bw = new BufferedWriter(new FileWriter("test.json"));
			bw.write(createJson());
			bw.close();
			System.out.println("���");
		} catch(Exception e) {
			e.printStackTrace();
		}		
		

	}
	public static String createJson() {
		try {
			
			BufferedReader br = new BufferedReader(new FileReader("imagec.txt"));
			String s;
			StringBuilder sb =new StringBuilder();
			while((s = br.readLine()) != null) {
				sb.append(s);
			}
			br.close();
			//String s1 = String.valueOf(sb.charAt(0));
			//����һ��ss������,����char���������v
			char[] cs =sb.toString().toCharArray();
			String[] ss = new String[cs.length]; //����color������
			for(int i = 0;i < cs.length; i++) {
				ss[i] = String.valueOf(cs[i]);
			}
			
			/*
			String[] ii = new String[ss.length]; // ���潫Ҫ���JSONObject������
			
			for(int k =0;k < ss.length; k++) ii[k] = String.valueOf(k);
			for(String aa : ii) {
				System.out.println(aa);
			}
			*/
			
			StringBuilder sb2 = new StringBuilder();
			JSONObject json = new JSONObject();
			for(int j =0;j <ss.length; j++) {
				
				JSONObject member = new JSONObject();
				member.put("x", j % size);
				member.put("y", j / size);
				member.put("color", ss[j]);
				
				json.put(String.valueOf(j), member);
				
				
				
			}
			sb2.append(json.toString());
			return sb2.toString();
			
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
			return null;
		}
	}

}
