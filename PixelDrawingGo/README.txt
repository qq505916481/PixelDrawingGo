画板颜色序号
网页版从上往下依次是(从左至右):0,1,2,3,4,5,6,7,8,9,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V

画板接口
apibody: https://api.live.bilibili.com/activity/v1/SummerDraw/draw
POST数据:
	x_min = 1065
	y_min = 410
	x_max = 1065
	y_max = 410
	color = 1
示列:在点(1065,410)处涂上白色
https://api.live.bilibili.com/activity/v1/SummerDraw/draw?x_min=1065&y_min=410&x_max=1065&y_max=410&color=1

返回值
成功{"code":0,"msg":"success","message":"success","data":{"time":180}}
code=0成功
code=-400在冷却时间
code=-101未登录

Bitmap分辨率:1280x720(1279x719)

cookies存放cookies,可以chrome浏览器f12用提取,一行一个,你有多少个cookies就有几个线程,具体cookies怎么提取自行百度解决吧

test.json文件储存了图像信息,可以用字符画生成器转换
	大概就是画一只萌萌的盟军鹰


cmd下用java -jar test.jar [起始横坐标] [起始纵坐标] 启动本程序

alpha1:
		首版测试
alpha2:
		增加像素检测,相同颜色不再着色,提高效率
		bugfix:
			修复了可能的线程安全问题
			修复了网络问题造成的线程阻塞
alpha3:
		优化了逻辑判断,检测已存在的像素点不再有滞后
		bugfix:
			修复了网络问题造成json解析失败导致的线程中断
alpha4:
		bugfix:
			修复了可能由于服务器中断连接等原因导致的数组下标越界异常
alpha5:
		bugfix:
			修复了http连接和输入流未正常关闭可能引发的问题
alpha6:
		使用Apache的HttpClient包来实现http协议,重写drawpixel方法
		把并没有什么卵用的BufferedReader换成InputStream
		显示详细的线程ID
		一些逻辑优化
		bugfix:
			修复了jar包本体过小的bug#滑稽
alpha7:
		优化了该程序逻辑,速度提升120倍#震惊, 之前的很傻, 真的
		由于运行过快,导致log日志可能会塞满硬盘和命令行刷屏, 所以去除了一些多余的提示信息, 并更新了提示文本
		准备下一版更新附带的windows cmd脚本
		
alpha8:	
		更新start.cmd
		更新start.sh
		bugfix:
			修复了很多bug, 提高了稳定性(具体的忘了)
			
alpha9:
		bugfix:
			修复一个导致线程被分配到相同的key的问题
			提示信息修正
			
alpha10:
		准正式版, 对发布做了些微小的工作
alpha11(过渡):
		解决循环遍历单核占用过高的问题
alpha12:
		重新使用readline()读取网络流的数据