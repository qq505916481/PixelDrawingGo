package pixel;

import java.io.*;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.*;

public class PixelDrawingGo {
	
	//�е㳤233
	public static String[] cookies;
	//public static String[] cookies = {"sid=4v7c5r0u; fts=1499662552; UM_distinctid=15d2add0dc027e-02a686c7de1368-333f5902-1fa400-15d2add0dc1af5; buvid3=B407E6A8-4B1C-4115-8534-290AE8EBD33537233infoc; pgv_pvi=8638030848; rpdid=kmpssiqlkidopllxxksqw; biliMzIsnew=1; biliMzTs=0; finger=edc6ecda; LIVE_BUVID=4966d78c3400745cf2bda9595235effe; LIVE_BUVID__ckMd5=b238ef1769c480ec; Hm_lvt_8a6e55dbd2870f0f5bc9194cddf32a02=1501300369,1501321878,1501321883,1502290543; _cnt_pm=0; _cnt_notify=4; DedeUserID=8742966; DedeUserID__ckMd5=5f5f90e71ea35d9f; SESSDATA=38240fb3%2C1505206502%2Cea58f457; bili_jct=65dfef502edb4afbc52e85e0870a2cc2; _dfcaptcha=eda8d74c04559fd5e71daa8971788c45"};
	public static String userAgent = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36";
	

	//private static boolean initialized = false; //��һ��ѭ��
	public final static int dozeTime = 180; //��ȴʱ��
	public final static int minTime = 5; //������ȴʱ��
	public final static int cpuCores = Runtime.getRuntime().availableProcessors();
	
	private static Thread[] threads;
	private static ExecutorService checkThreadPool;
	private CountDownLatch cdl = new CountDownLatch(cpuCores);
	
	private static Vector<String> failedMark = new Vector<>();
	private static BitmapParser bp = new BitmapParser();
	
	private static String bitmapDataCache = null;
	
	//public static int lastresult;
	//public static String key; //���ص�ȫ��ָ��
	
	public static JSONObject json;
	private static Iterator<String> it;
	
	// ���н�������
	private final static int maxOreos = 5;
	private final static int oreosRemain = 5;
	private static volatile int oreoCount = 0;
	private static volatile boolean oreoIsRunning; // �Ƿ����й�orea����
	
	
	private static int start_x;
	private static int start_y;
	
	public static void main(String[] args) {
		// TODO �Զ����ɵķ������
		/*System.out.println(new BitmapParser().getCharIndexOf(527274));
		*try {
		*	Thread.sleep(6000);
		*} catch (InterruptedException e) {
		*	e.printStackTrace();
		}
		*/
		try {
		start_x = Integer.valueOf(args[0]);
		start_y = Integer.valueOf(args[1]);
		} catch(Exception e) {
			start_x = 0;
			start_y = 0;
			e.printStackTrace();
			System.out.println("������쳣˵����û�а���Լ���������,��ͨ���ᵼ�����ֱ�ӻ�������Ͻǿ�ʼ����(Ȼ��ѱ��˵Ļ����� \n ������ʹ��: java -jar test.jar [��ʼ������] [��ʼ������] , \n ���� java -jar test.jar 1250 700      (����ߴ�1279 x 719) \n ============================== \n ����Ϊdebug��־,���Բ���");
		}
		new PixelDrawingGo().go();
	}

	public void getCookieLines(String file) throws Exception {
		Vector<String> vec = new Vector<>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String s;
			
			while((s = br.readLine()) != null) {
				vec.add(s);
			}
			br.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if(new File(file).length() == 0) {
			throw new Exception("��ѽ, �����޷���ȡcookies, ����cookies.txt");
		}
		
		cookies = new String[vec.size()];
		Iterator<String> it2 = vec.iterator();
		int i = 0;
		while(it2.hasNext()) {
			cookies[i] = it2.next();
			i++;
			
		}
	}
	
	public String getImgJson(String file) {
		// ���׳��쳣����Ϊ����ǿյ�֮��ʵ�л�JSONObject�϶��ᱨ��
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			StringBuilder sb = new StringBuilder();
			String s;
			while((s = br.readLine()) != null) {
				sb.append(s);
			}
			br.close();
			return sb.toString();
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// �����Ժ�ת�ɹ��췽��
	public void go() {
		oreoIsRunning = false;
		try {
			getCookieLines("cookies.txt");
		} catch (Exception e2) {
			// TODO �Զ����ɵ� catch ��
			e2.printStackTrace();
			// exit
			System.exit(-233);
		}

		json = new JSONObject(getImgJson("test.json"));
		it = json.keys();
		System.out.println("��ӭʹ��PixelDrawingGo, ��ʼ��...");
		do {
			bitmapDataCache = bp.getBitmapData(bp.getBitmapDirtyDataReborn());
		} while(bitmapDataCache == null);
		
		
		// һ����������̵�ζ��
		checkThreadsGo();
		
		threads = new Thread[cookies.length];
		for(int j = 0; j < cookies.length; j++) {
			final int jb = j; 
			threads[jb] = new Thread(new Runnable() {

				@Override
				public void run() {
					// ������Ȱ����صȻ�
					try {
						cdl.await();
					} catch (InterruptedException e1) {
						// TODO �Զ����ɵ� catch ��
						e1.printStackTrace();
					}
					
					// �رռ�����̳߳�
					checkThreadPool.shutdown();
					
					System.out.println("�����߳�:" + Thread.currentThread().getName() + "��ʼ����");
					
					int name =jb;
					int lastresult = 0;
					String key = null;
					// �����Ժ�ȥ�������������
					Boolean isClean = true;
					
					// TODO �Զ����ɵķ������
					/*if(!initialized) {
					*	lastresult = 0;
					*	initialized = true;
					*	System.out.println("��ʼ�����");
					*}
					*/
					while(!oreoIsRunning) {
						//if(it.hasNext() || (!failedMark.isEmpty() && isClean)) {
						if(lastresult == 0 || !isClean) { //�ɹ���Ž�����һ�������,�������֮ǰ��, ������cleanģʽ����֮ǰ������Ч
							key = keySelect(isClean);
						} 
						if(key != null) {
							
							
							int x = json.optJSONObject(key).optInt("x") + start_x;
							int y = json.optJSONObject(key).optInt("y") + start_y;
							String color = json.optJSONObject(key).optString("color");
							if(isClean) System.out.println(Thread.currentThread().getName() + ":" + x+ "" + y+ "" + color);
							
							// long startTime = System.currentTimeMillis();
							int index = x + y * 1280;
							String color2;
							
							color2 = String.valueOf(bp.getCharIndexOf(bp.getBitmapData(bp.getBitmapDirtyDataReborn()), index)); //ÿ����滭֮ǰ���һ��
							// if(color2.equals(color))continue;
							if(color2.equals(color)) {
								System.out.println("����������Ѿ�����������ͬ����ɫ��, ����");
								lastresult = 0;
								oreoCount++;
								if(oreoCount >= maxOreos && failedMark.size() >= oreosRemain) {
									oreoRestart(); // �¸��ֻؿ�ʼ��,��Ҳ������
									System.out.println("My work has been completed, now I will go die");
									break;
								}
								continue;
							}
							
							//long endTime = System.currentTimeMillis();
							//System.out.println("��ȡ���ص�����ʱ��" + (endTime - startTime) / 1000.000 + "s");
							
							// ��ʼ����, ������Ӧ��Result����
							Result result = new DrawUtils().drawPixelReborn(x, y, color, cookies[name], userAgent);
							lastresult = result.code;
							
							
							if(lastresult == 0) {
								System.out.println(Thread.currentThread().getName() + "�ɹ�");
								
								//failedMark.remove(key);
								
								if(failedMark.isEmpty() && !oreoIsRunning) { //�������,ֱ����һѭ������, ����ȫ�ر�������ͬ�߳�
									// ˫����
									oreoIsRunning = true;
									for(Thread t : threads) {
										if(t == Thread.currentThread()) continue;
										t.interrupt();
									}
									continue;
								}
								
								//������ȴʱ��
								try {
									Thread.sleep(dozeTime*1000);
								} catch(InterruptedException e) {
									e.printStackTrace();
									break;
								}
								
							} else if(lastresult == -400) {
								System.out.println("������ȴʱ�仹û�й�����" + minTime + "s֮������");
								
								try {
									Thread.sleep(minTime*1000);
								} catch(InterruptedException e) {
									e.printStackTrace();
								}
								
								
							} else if(lastresult == -101) {
								System.out.println("���cookie��Ч���߹�����,���߳̽���������,������ʧ�ܵ�λ�����ݽ��������̴߳���");
								System.out.println("��cookie:" +cookies[name]);
								System.out.println("�õ�����" + key);
								failedMark.add(key); //���ڿ��Ա�֤ÿ���̷߳��䵽��key�ǲ�һ����,���в��ÿ���ͬ������
								break;
							} else if(lastresult == -666) { //��д��233
								System.out.println("���������˵�������" + minTime + "s֮������");
								try {
									Thread.sleep(minTime*1000);
								} catch(InterruptedException e) {
									e.printStackTrace();
								}
							}
						} else {
							System.out.println(Thread.currentThread().getName() + "�����������ֶ��Ѿ������, ���߳̽��ر�");
							break;
						}
						
					}
					
					System.out.println(Thread.currentThread().getName() + "�����߳̽���");
				}
				
			});
			threads[jb].start();
		}
		
		/*while(it.hasNext()) {
		*	String key = it.next();
		*	int x = json.optJSONObject(key).optInt("x");
		*	int y = json.optJSONObject(key).optInt("y");
		*	String color = json.optJSONObject(key).optString("color");
		*	System.out.println(x+ "" + y+ "" + color);
		*	Result result = new DrawUtils().drawPixel(x, y, color, cookies[0], userAgent);
		*	System.out.println("���" + result.code);
		*	//break;
		*	}
		*/	
			
		
	}

	public void checkThreadsGo() {
		checkThreadPool = Executors.newFixedThreadPool(cpuCores);
		for(int i = 0; i < cpuCores; i++) {
			checkThreadPool.execute(new Runnable() {

				@Override
				public void run() {
					// TODO �Զ����ɵķ������
					
					String key = null;
					
					System.out.println(Thread.currentThread().getName() + "��ʼ����, ��Ů����");
					while(true) {
						key = keySelect(false);
						
						if(key != null) {
							int x = json.optJSONObject(key).optInt("x") + start_x;
							int y = json.optJSONObject(key).optInt("y") + start_y;
							String color = json.optJSONObject(key).optString("color");
							
							int index = x + y * 1280;
							String color2;
							
							color2 = String.valueOf(bp.getCharIndexOf(bitmapDataCache, index)); //�ȴ�����һ��
							if(color2.equals(color)) {
								// lastresult = -667; // ֱ������,������һѭ��
								// key = keySelect(isClean); // ��������ʵ����ע�͵�
								// System.out.println("��⵽��ͬ��ɫ���ص�,����");
								continue;
							} else {
								// lastresult = -668;
								//System.out.println("����һ����ͽ");
								failedMark.addElement(key);
								continue;
							}
							
						} else {
							System.out.println(Thread.currentThread().getName() + "������, ��ʼ��������ħ���");
							break;
						}
					}
					cdl.countDown();
				}
				
			});
		}
	}
	
	public synchronized String keySelect(Boolean isClean) { //ͬ����ֹ�쳣
		if(it.hasNext()) { //isClean
			return it.next();
		} else if(!failedMark.isEmpty() && isClean) {
			System.out.println("���ͳ��"+failedMark.size()); // ���ǻ��ɴ�ӡʸ�����Ĵ�С�Ƚ�ֱ��
			String key = failedMark.lastElement();
			failedMark.removeElement(key);
			return key;
		} else {
			return null;
		}
	}

	private synchronized void oreoRestart() {
		if(oreoIsRunning == true)return;
		
		System.out.println("��ͬ����������ֵ, ����...");
		
		oreoIsRunning = true;
		// Initialize
		oreoCount = 0;
		cdl = new CountDownLatch(cpuCores);
		failedMark.clear(); // �����߳�: GG
		// bitmapDataCache = null; �ɼӿɲ���
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO �Զ����ɵķ������
				for(Thread t : threads) {
					while(t.isAlive()) {
						
					}
				}
				System.out.println("���������");
				go();
			}
			
		}).start();
		
	}

}
