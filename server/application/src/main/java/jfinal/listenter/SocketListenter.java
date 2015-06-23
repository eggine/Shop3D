package jfinal.listenter;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import ymd.UserServer.UserServer;


/**
 * 监听端口
 * @author jiangshao
 *
 */
public class SocketListenter implements ServletContextListener {
	public void contextInitialized(ServletContextEvent event) {
			//随应用服务器启动监听线程的
		 new Thread(UserServer.getinstance()).start();
	}

	public void contextDestroyed(ServletContextEvent sce) {
			///停止线程
		UserServer.getinstance().closeThread();
	} 
}
