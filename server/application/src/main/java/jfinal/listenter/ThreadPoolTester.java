package jfinal.listenter;
public class ThreadPoolTester {

	public static void main(String[] args) {
		
		int numTasks = 20;      //任务数量
		int poolSize = 3;       //线程池里线程的数量
		
		ThreadPool threadPool = new ThreadPool(poolSize);  //创建线程池
		/*
		 * 运行任务
		 */
		for(int i = 0; i<10; i++) {
			threadPool.execute(createTask(i));
		}		
		threadPool.join();   //等待工作线程完成所有的任务
		//
		System.out.println("ddddd");
		threadPool.close();
	}
	
	/*
	 * 定义了一个简单的任务(打印ID)
	 */
	private static Runnable createTask(final int i) {
		return new Runnable() {
			public void run() {
				try {
					Thread.sleep(200);                 //增加执行一个任务的时间
					System.out.println("createTask:" + i + ":start");
				} catch(InterruptedException e) {
					System.out.println("Task end");
				}
			}
		};
	}
}