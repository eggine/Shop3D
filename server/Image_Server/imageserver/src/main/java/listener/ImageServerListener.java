 package listener;
 
 import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import ymd.ImageServer.ImageServer;
 
 public class ImageServerListener
   implements ServletContextListener
 {
   public void contextInitialized(ServletContextEvent event)
   {
     new Thread(ImageServer.getInstance()).start();
   }
 
   public void contextDestroyed(ServletContextEvent sce)
   {
	   ImageServer.getInstance().cloaseImageServer();
	   
   }
 }
