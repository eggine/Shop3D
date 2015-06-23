package config;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.plugin.ehcache.EhCachePlugin;

import controller.IndexController;
import controller.LookDirectory;
import controller.UploadFileController;

public class PServerConfig extends JFinalConfig {

    public void configConstant(Constants me) {
        me.setDevMode(true);
    }

    public void configRoute(Routes me) {
        me.add("/", IndexController.class);
        me.add("/file",LookDirectory.class);
        me.add("/upload",UploadFileController.class);
    }

    public void configPlugin(Plugins me) {
    	 //缓存插件
        /*String ehcacheConf = PathKit.getWebRootPath() + File.separator + "WEB-INF" + File.separator + "ehcache.xml";
        me.add(new EhCachePlugin(ehcacheConf)); */
//        me.add(new EhCachePlugin());
    }

    public void configInterceptor(Interceptors me) {
    }

    public void configHandler(Handlers me) {
    }


}
