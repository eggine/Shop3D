package jfinal.config;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.activerecord.tx.TxByActionMethods;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.druid.DruidStatViewHandler;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import jfinal.model.Custom;
import jfinal.model.Item;
import jfinal.model.User;
import org.apache.log4j.Logger;

public class AserverConfig extends JFinalConfig
{
  private static Logger log = Logger.getLogger(AserverConfig.class);

  public void configConstant(Constants me)
  {
    log.info("读取配置数据库文件");
    loadPropertyFile("db_config.txt");
    me.setDevMode(true);
  }

  public void configRoute(Routes me) {
    me.add(new FrontRoute());
    me.add(new AdminRoute());
  }

  public void configPlugin(Plugins me)
  {
    log.info("初始化缓存");
    me.add(new EhCachePlugin());

    log.info("初始化Driud");

    DruidPlugin dp = new DruidPlugin(getProperty("jdbcUrl"), getProperty("user"), getProperty("password"));

    dp.addFilter(new StatFilter());
    WallFilter wall = new WallFilter();
    wall.setDbType("mysql");
    dp.addFilter(wall);
    me.add(dp);

    log.info("初始化ActiveRecord");
    ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
    arp.setDialect(new MysqlDialect()).setShowSql(true);
    me.add(arp);

    arp.addMapping("User", User.class);
    arp.addMapping("Item", Item.class);
    arp.addMapping("Custom", Custom.class);
  }

  public void configInterceptor(Interceptors me)
  {
    me.add(new TxByActionMethods(new String[] { "save", "update", "delete", "batch" }));
  }

  public void configHandler(Handlers me)
  {
    DruidStatViewHandler dvh = new DruidStatViewHandler("/druid");
    me.add(dvh);

    me.add(new ContextPathHandler("path"));
  }
}