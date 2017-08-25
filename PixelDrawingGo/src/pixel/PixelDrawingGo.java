package pixel;

import java.io.*;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.*;

public class PixelDrawingGo {
	
	//有点长233
	public static String[] cookies;
	//public static String[] cookies = {"sid=4v7c5r0u; fts=1499662552; UM_distinctid=15d2add0dc027e-02a686c7de1368-333f5902-1fa400-15d2add0dc1af5; buvid3=B407E6A8-4B1C-4115-8534-290AE8EBD33537233infoc; pgv_pvi=8638030848; rpdid=kmpssiqlkidopllxxksqw; biliMzIsnew=1; biliMzTs=0; finger=edc6ecda; LIVE_BUVID=4966d78c3400745cf2bda9595235effe; LIVE_BUVID__ckMd5=b238ef1769c480ec; Hm_lvt_8a6e55dbd2870f0f5bc9194cddf32a02=1501300369,1501321878,1501321883,1502290543; _cnt_pm=0; _cnt_notify=4; DedeUserID=8742966; DedeUserID__ckMd5=5f5f90e71ea35d9f; SESSDATA=38240fb3%2C1505206502%2Cea58f457; bili_jct=65dfef502edb4afbc52e85e0870a2cc2; _dfcaptcha=eda8d74c04559fd5e71daa8971788c45"};
	public static String userAgent = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36";
	

	//private static boolean initialized = false; //第一次循环
	public final static int dozeTime = 180; //冷却时间
	public final static int minTime = 5; //错误冷却时间
	public final static int cpuCores = Runtime.getRuntime().availableProcessors();
	
	private static Thread[] threads;
	private static ExecutorService checkThreadPool;
	private CountDownLatch cdl = new CountDownLatch(cpuCores);
	
	private static Vector<String> failedMark = new Vector<>();
	private static BitmapParser bp = new BitmapParser();
	
	private static String bitmapDataCache = null;
	
	//public static int lastresult;
	//public static String key; //象素点全局指针
	
	public static JSONObject json;
	private static Iterator<String> it;
	
	// 颇有节日气氛
	private final static int maxOreos = 5;
	private final static int oreosRemain = 5;
	private static volatile int oreoCount = 0;
	private static volatile boolean oreoIsRunning; // 是否运行过orea方法
	
	
	private static int start_x;
	private static int start_y;
	
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
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
			System.out.println("上面的异常说明你没有按照约定输入参数,这通常会导致你从直接画板的左上角开始绘制(然后把别人的画覆盖 \n 建议你使用: java -jar test.jar [起始横坐标] [起始纵坐标] , \n 列如 java -jar test.jar 1250 700      (画板尺寸1279 x 719) \n ============================== \n 以下为debug日志,可以不看");
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
			throw new Exception("哎呀, 程序无法读取cookies, 请检查cookies.txt");
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
		// 不抛出异常是因为如果是空的之后实列化JSONObject肯定会报错
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

	// 将在以后转成构造方法
	public void go() {
		oreoIsRunning = false;
		try {
			getCookieLines("cookies.txt");
		} catch (Exception e2) {
			// TODO 自动生成的 catch 块
			e2.printStackTrace();
			// exit
			System.exit(-233);
		}

		json = new JSONObject(getImgJson("test.json"));
		it = json.keys();
		System.out.println("欢迎使用PixelDrawingGo, 初始化...");
		do {
			bitmapDataCache = bp.getBitmapData(bp.getBitmapDirtyDataReborn());
		} while(bitmapDataCache == null);
		
		
		// 一股子面向过程的味道
		checkThreadsGo();
		
		threads = new Thread[cookies.length];
		for(int j = 0; j < cookies.length; j++) {
			final int jb = j; 
			threads[jb] = new Thread(new Runnable() {

				@Override
				public void run() {
					// 你给我先安静地等会
					try {
						cdl.await();
					} catch (InterruptedException e1) {
						// TODO 自动生成的 catch 块
						e1.printStackTrace();
					}
					
					// 关闭检测用线程池
					checkThreadPool.shutdown();
					
					System.out.println("暴走线程:" + Thread.currentThread().getName() + "开始运行");
					
					int name =jb;
					int lastresult = 0;
					String key = null;
					// 将在以后去除这个布尔变量
					Boolean isClean = true;
					
					// TODO 自动生成的方法存根
					/*if(!initialized) {
					*	lastresult = 0;
					*	initialized = true;
					*	System.out.println("初始化完成");
					*}
					*/
					while(!oreoIsRunning) {
						//if(it.hasNext() || (!failedMark.isEmpty() && isClean)) {
						if(lastresult == 0 || !isClean) { //成功后才进行下一个点绘制,否则继续之前的, 并且在clean模式开启之前保持生效
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
							
							color2 = String.valueOf(bp.getCharIndexOf(bp.getBitmapData(bp.getBitmapDirtyDataReborn()), index)); //每个点绘画之前检测一次
							// if(color2.equals(color))continue;
							if(color2.equals(color)) {
								System.out.println("看来这个点已经被人填上相同的颜色了, 跳过");
								lastresult = 0;
								oreoCount++;
								if(oreoCount >= maxOreos && failedMark.size() >= oreosRemain) {
									oreoRestart(); // 下个轮回开始后,它也就死了
									System.out.println("My work has been completed, now I will go die");
									break;
								}
								continue;
							}
							
							//long endTime = System.currentTimeMillis();
							//System.out.println("获取象素点所花时间" + (endTime - startTime) / 1000.000 + "s");
							
							// 开始画点, 返回相应的Result对象
							Result result = new DrawUtils().drawPixelReborn(x, y, color, cookies[name], userAgent);
							lastresult = result.code;
							
							
							if(lastresult == 0) {
								System.out.println(Thread.currentThread().getName() + "成功");
								
								//failedMark.remove(key);
								
								if(failedMark.isEmpty() && !oreoIsRunning) { //如果空了,直接下一循坏结束, 并安全关闭其他相同线程
									// 双保险
									oreoIsRunning = true;
									for(Thread t : threads) {
										if(t == Thread.currentThread()) continue;
										t.interrupt();
									}
									continue;
								}
								
								//进入冷却时间
								try {
									Thread.sleep(dozeTime*1000);
								} catch(InterruptedException e) {
									e.printStackTrace();
									break;
								}
								
							} else if(lastresult == -400) {
								System.out.println("好像冷却时间还没有归零呢" + minTime + "s之后再试");
								
								try {
									Thread.sleep(minTime*1000);
								} catch(InterruptedException e) {
									e.printStackTrace();
								}
								
								
							} else if(lastresult == -101) {
								System.out.println("你的cookie无效或者过期了,该线程将结束运行,并保存失败的位块数据交给其他线程处理");
								System.out.println("该cookie:" +cookies[name]);
								System.out.println("该点的序号" + key);
								failedMark.add(key); //由于可以保证每个线程分配到的key是不一样的,所有不用考虑同步问题
								break;
							} else if(lastresult == -666) { //乱写的233
								System.out.println("网络好像出了点问题呢" + minTime + "s之后再试");
								try {
									Thread.sleep(minTime*1000);
								} catch(InterruptedException e) {
									e.printStackTrace();
								}
							}
						} else {
							System.out.println(Thread.currentThread().getName() + "看来所有妖怪都已经纳入后宫, 本线程将关闭");
							break;
						}
						
					}
					
					System.out.println(Thread.currentThread().getName() + "暴力线程结束");
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
		*	System.out.println("结果" + result.code);
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
					// TODO 自动生成的方法存根
					
					String key = null;
					
					System.out.println(Thread.currentThread().getName() + "开始运行, 少女祈祷中");
					while(true) {
						key = keySelect(false);
						
						if(key != null) {
							int x = json.optJSONObject(key).optInt("x") + start_x;
							int y = json.optJSONObject(key).optInt("y") + start_y;
							String color = json.optJSONObject(key).optString("color");
							
							int index = x + y * 1280;
							String color2;
							
							color2 = String.valueOf(bp.getCharIndexOf(bitmapDataCache, index)); //先大体检查一遍
							if(color2.equals(color)) {
								// lastresult = -667; // 直接跳过,进入下一循环
								// key = keySelect(isClean); // 这两句其实可以注释掉
								// System.out.println("检测到相同颜色像素点,跳过");
								continue;
							} else {
								// lastresult = -668;
								//System.out.println("发现一个叛徒");
								failedMark.addElement(key);
								continue;
							}
							
						} else {
							System.out.println(Thread.currentThread().getName() + "祈祷结束, 开始退治理妖魔鬼怪");
							break;
						}
					}
					cdl.countDown();
				}
				
			});
		}
	}
	
	public synchronized String keySelect(Boolean isClean) { //同步防止异常
		if(it.hasNext()) { //isClean
			return it.next();
		} else if(!failedMark.isEmpty() && isClean) {
			System.out.println("最后统计"+failedMark.size()); // 还是换成打印矢量集的大小比较直观
			String key = failedMark.lastElement();
			failedMark.removeElement(key);
			return key;
		} else {
			return null;
		}
	}

	private synchronized void oreoRestart() {
		if(oreoIsRunning == true)return;
		
		System.out.println("相同次数超过阈值, 重启...");
		
		oreoIsRunning = true;
		// Initialize
		oreoCount = 0;
		cdl = new CountDownLatch(cpuCores);
		failedMark.clear(); // 爆走线程: GG
		// bitmapDataCache = null; 可加可不加
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO 自动生成的方法存根
				for(Thread t : threads) {
					while(t.isAlive()) {
						
					}
				}
				System.out.println("热重启完成");
				go();
			}
			
		}).start();
		
	}

}
